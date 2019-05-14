package com.anit.fastpallet4.presentaition.presenter.inventory


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.common.formatDate
import com.anit.fastpallet4.domain.intity.extra.InfoPallet
import com.anit.fastpallet4.domain.intity.metaobj.Box
import com.anit.fastpallet4.domain.intity.metaobj.InventoryPallet
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.usecase.UseCaseGetInfoPallet
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.domain.utils.getNumberDocByBarCode
import com.anit.fastpallet4.domain.utils.getWeightByBarcode
import com.anit.fastpallet4.domain.utils.isPallet
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryView
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class InventoryPresenter(
    router: Router,
    private val inputParamObj: InventoryFrScreen.InputParamObj?

) : BasePresenter<InventoryView>(router) {

    private val model = Model(inputParamObj!!.guid)

    var isShowDialog = false

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun onStart() {
        model.refreshViewModel()
    }

    fun getViewModelFlowable() = model.behaviorSubjectViewModel
        .toFlowable(BackpressureStrategy.BUFFER)

    fun readBarcode(barcode: String?) {

        if (model.doc!!.onlyRead()) {
            viewState.showSnackbarViewError("Нельзя Изменять!")
            return
        }

        if (isPallet(barcode!!)) {
            if (!model.doc!!.numberPallet.isNullOrEmpty()) {
                viewState.showSnackbarViewError("Палета уже назначена!\n ${model.doc!!.numberPallet}")
            } else {
                try {
                    var number = getNumberDocByBarCode(barcode)
                    model.savePallet(number, barcode)
                } catch (e: Throwable) {
                    viewState.showSnackbarViewError("Ошибка в номере паллеты!")
                }
            }

            return
        }

        if (model.doc!!.numberPallet.isNullOrEmpty()) {
            viewState.showSnackbarViewError("Сканируйте паллету!")
        } else {
            var weight = getWeightByBarcode(
                barcode = barcode,
                start = model.doc!!.stringProduct!!.weightStartProduct,
                finish = model.doc!!.stringProduct!!.weightEndProduct,
                coff = model.doc!!.stringProduct!!.weightCoffProduct
            )

            if (weight == 0f) {
                viewState.showSnackbarViewError("Не верный вес!")
            } else if (barcode.length != model.doc!!.stringProduct!!.barcode?.length) {
                viewState.showSnackbarViewError("Не верная длинна штрихкода!")
            } else {
                model.addBox(weight, barcode)
            }
        }




    }

    fun onClickItem(index: Int) {
        var box = model.getBoxByIndex(index)
        viewState.showDialogBox(
            title = model.doc!!.stringProduct.nameProduct ?: "",
            weight = box.weight,
            date = box.data ?: Date(),
            barcode = box.barcode,
            index = index,
            countBox = box.countBox
        )
    }

    fun onClickInfo() {
        var doc = model.doc
        viewState.showDialogProduct(
            title = doc!!.stringProduct.nameProduct ?: "",
            weightStartProduct = doc.stringProduct.weightStartProduct,
            weightEndProduct = doc.stringProduct.weightEndProduct,
            weightCoffProduct = doc.stringProduct.weightCoffProduct,
            barcode = doc.stringProduct.barcode
        )
    }

    fun saveProduct(
        barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float
    ) {
        model.saveProduct(
            barcode = barcode,
            weightStartProduct = weightStartProduct,
            weightEndProduct = weightEndProduct,
            weightCoffProduct = weightCoffProduct
        )

    }

    fun saveBox(barcode: String?, weight: Float, index: Int?, countBox: Int) {
        if (weight == 0f) {
            viewState.showSnackbarViewError("Не верный вес!")
        } else {
            model.saveBox(
                index = index,
                barcode = barcode,
                weight = weight,
                countBox = countBox
            )
        }

    }

    fun onClickDell(id: Int) {
        when (model.doc!!.status) {
            Status.NEW, Status.LOADED -> {
                viewState.showDialogConfirmDell(id, "Удалить коробку?")
            }
            else -> viewState.showSnackbarViewError("Нельзя Удалять!")
        }

    }

    fun onClickAdd() {
        if (model.doc!!.onlyRead()) {
            viewState.showSnackbarViewError("Нельзя Изменять!")
            return
        }

        viewState.showDialogBox(
            title = model.doc!!.stringProduct.nameProduct ?: "",
            weight = 0f,
            date = Date(),
            barcode = null,
            index = null,
            countBox = 1
        )

    }

    fun dellBox(id: Int) {
        model.dellBoxById(id)
    }


    fun loadInfoPallet() {
        if (model.doc!!.numberPallet.isNullOrEmpty()) {
            viewState.showSnackbarViewError("Сканируйте паллету!")
        } else {
            model.getInfoPalletFromServer()
                .doOnSubscribe { viewState.showSnackbarViewMess("Запрос") }
                .doOnDispose {  viewState.showSnackbarViewMess("Конец") }
                .subscribe({
                    var info = it.find { it.code.equals(model.doc!!.numberPallet, true) }
                    info.let {
                        model.setInfoPallet(info!!)
                    }
                    viewState.showSnackbarViewMess("Ок")
                }, {
                    viewState.showSnackbarViewError(it.message.toString())
                })
        }

    }

    fun onClickLoad(index: Int) {
        loadInfoPallet()
    }
}

class Model(var guidDoc: String) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj

    @Inject
    lateinit var interactorGetInfoPallet: UseCaseGetInfoPallet

    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()
    var doc: InventoryPallet? = null
    var viewModel: ViewModel? = null

    init {
        App.appComponent.inject(this)
    }

    fun refreshViewModel() {
        doc = interactorGetDoc.get(guidDoc) as InventoryPallet

        var sumPalletInfo = doc!!.stringProduct.getSummPalletInfoForInventory()
        var sumPalletInfoServer = doc!!.stringProduct.getSummPalletInfoFromServer()

        var list = doc!!.stringProduct.boxes.map {
            ItemList(
                info = "${it.weight} / ${it.countBox}",
                left = "${formatDate(it.data)}",
                guid = it.guid,
                data = it.data
            )
        }.sortedByDescending { it.data }



        viewModel = ViewModel(
            info = "${doc!!.description}",
            right = "${sumPalletInfo.count} / ${sumPalletInfo.countBox} / ${sumPalletInfo.countPallet}",
            left = "${sumPalletInfoServer.count} / ${sumPalletInfoServer.countBox}",
            list = list
        )



        behaviorSubjectViewModel.onNext(viewModel!!)
    }

    fun addBox(weight: Float, barcode: String?) {
        var box = Box()

        box.barcode = barcode
        box.data = Date()
        box.weight = weight
        box.countBox = 1

        doc!!.stringProduct.addBox(box)
        doc!!.save()
        refreshViewModel()
    }

    fun getBoxByIndex(index: Int): Box {
        var guid = viewModel!!.list.get(index).guid
        return doc!!.stringProduct.boxes.find { it.guid == guid }!!
    }

    fun saveBox(index: Int?, weight: Float, barcode: String?, countBox: Int) {
        var box: Box?
        if (index != null) {
            box = getBoxByIndex(index)
        } else {
            box = Box()
            doc!!.stringProduct.addBox(box)
        }

        box.barcode = barcode
        box.data = Date()
        box.weight = weight
        box.countBox = countBox

        doc!!.save()
        refreshViewModel()
    }

    fun saveProduct(
        barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float
    ) {

        doc!!.stringProduct.barcode = barcode
        doc!!.stringProduct.weightStartProduct = weightStartProduct
        doc!!.stringProduct.weightEndProduct = weightEndProduct
        doc!!.stringProduct.weightCoffProduct = weightCoffProduct
        doc!!.save()
        refreshViewModel()

    }


    fun savePallet(number: String, barcode: String) {
        doc!!.barcodePallet = barcode
        doc!!.numberPallet = number
        doc!!.description = "Инвентаризация палеты ${doc!!.numberPallet} от ${formatDate(doc!!.date)}"
        doc!!.save()
        refreshViewModel()
    }

    fun dellBoxById(index: Int) {
        var box = getBoxByIndex(index)
        doc!!.stringProduct.dellBoxByGuid(box.guid!!)
        doc!!.save()
        refreshViewModel()
    }

    fun setInfoPallet(infoPallet: InfoPallet) {
        doc!!.stringProduct.apply {
            this.count = infoPallet.count
            this.countBox = infoPallet.countBox
            this.guidProduct = infoPallet.guid
            this.nameProduct = infoPallet.nameProduct

            this.weightBarcode = infoPallet.weightBarcode
            this.weightCoffProduct = infoPallet.weightCoffProduct
            this.weightStartProduct = infoPallet.weightStartProduct
            this.weightEndProduct  = infoPallet.weightEndProduct
        }

        doc!!.description = "Инвентаризация палеты ${doc!!.numberPallet} от ${formatDate(doc!!.date)}" +
                " ${infoPallet.sclad} ${infoPallet.state}"

        doc!!.save()
        refreshViewModel()
    }

    fun getInfoPalletFromServer(): Single<List<InfoPallet>> {
        return interactorGetInfoPallet.load(listOf(doc!!.numberPallet!!))
    }
}

class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




