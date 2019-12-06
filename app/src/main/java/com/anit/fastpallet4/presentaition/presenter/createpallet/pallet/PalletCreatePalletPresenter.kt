package com.anit.fastpallet4.presentaition.presenter.createpallet.pallet


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.common.formatDate
import com.anit.fastpallet4.domain.intity.metaobj.Box
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Pallet
import com.anit.fastpallet4.domain.intity.metaobj.Status.LOADED
import com.anit.fastpallet4.domain.intity.metaobj.Status.NEW
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.domain.utils.getWeightByBarcode
import com.anit.fastpallet4.domain.utils.isPallet
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.inventory.CreatePalletView
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class PalletCreatePalletPresenter(
    router: Router,
    private val inputParamObj: PalletCreatePalletFrScreen.InputParamObj?

) : BasePresenter<CreatePalletView>(router) {

    var isShowDialog = false

    init {
        App.appComponent.inject(this)
    }

    private val model = Model(
        inputParamObj!!.guid,
        inputParamObj.guidStringProduct,
        inputParamObj.guidPallet
    )

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

        if (isPallet(barcode ?: "")) {
            viewState.showSnackbarViewError("Это паллета!")
            return
        }


        barcode.let {
            var weight = getWeightByBarcode(
                barcode = it!!,
                start = model.stringProduct!!.weightStartProduct,
                finish = model.stringProduct!!.weightEndProduct,
                coff = model.stringProduct!!.weightCoffProduct
            )

            if (weight == 0f) {
                viewState.showSnackbarViewError("Не верный вес!")
            } else if (it.length != model.stringProduct!!.barcode?.length) {
                viewState.showSnackbarViewError("Не верная длинна штрихкода!")
            } else {
                model.addBox(weight, it)
            }
        }
    }

    fun onClickItem(index: Int) {
        var box = model.getBox(index)
        viewState.showDialogBox(
            title = model.stringProduct!!.nameProduct ?: "",
            weight = box!!.weight,
            date = box.data ?: Date(),
            barcode = box.barcode,
            index = index,
            countBox = box.countBox

        )
    }

    fun onClickInfo() {

        viewState.showDialogProduct(
            title = model.stringProduct!!.nameProduct ?: "",
            weightStartProduct = model.stringProduct!!.weightStartProduct,
            weightEndProduct = model.stringProduct!!.weightEndProduct,
            weightCoffProduct = model.stringProduct!!.weightCoffProduct,
            barcode = model.stringProduct!!.barcode
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
            NEW, LOADED -> {
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
            title = model.stringProduct!!.nameProduct ?: "",
            weight = 0f,
            date = Date(),
            barcode = null,
            index = null,
            countBox = 1
        )

    }

    fun dellBox(id: Int) {
        model.dellBox(id)
    }


}

class Model(
    var guidDoc: String,
    var guidStringProduct: String,
    var guidPallet: String
) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: CreatePallet? = null
    var stringProduct: StringProduct? = null
    var pallet: Pallet? = null
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()

    var viewModel: ViewModel? = null

    init {
        App.appComponent.inject(this)

    }

    fun getStringProducts(guid: String): StringProduct? {
        return doc!!.getStringProductByGuid(guid)
    }

    fun getPallet(guid: String): Pallet? {
        return stringProduct!!.getPalletByGuid(guid)
    }

    fun refreshViewModel() {

        doc = interactorGetDoc.get(guidDoc) as CreatePallet
        stringProduct = getStringProducts(guidStringProduct)
        pallet = getPallet(guidPallet)


        var list = pallet!!.boxes.map {
            ItemList(
                info = "${it.weight} / ${it.countBox}",
                left = "${formatDate(it.data)}",
                guid = it.guid,
                data = it.data
            )
        }
            .sortedByDescending { it.data }

        var summPalletInfoPall = pallet!!.getSummPalletInfoFromBoxes()

        viewModel = ViewModel(
            info = "${pallet?.number}",
            list = list,
            left = "${formatDate(pallet!!.dataChanged)}",
            right = "${summPalletInfoPall.count} / ${summPalletInfoPall.countBox} / ${summPalletInfoPall.countPallet} "
        )


        behaviorSubjectViewModel.onNext(viewModel!!)

    }

    fun dellBox(index: Int) {
        var guid = getBox(index)!!.guid
        pallet!!.dellBoxByGuid(guid!!)
        doc!!.save()
        refreshViewModel()
    }

    fun getBox(index: Int): Box? {
        var guid = viewModel!!.list.get(index).guid
        return pallet!!.getBoxByGuid(guid!!)
    }

    fun saveProduct(
        barcode: String?,
        weightStartProduct: Int,
        weightEndProduct: Int,
        weightCoffProduct: Float
    ) {

        stringProduct!!.barcode = barcode
        stringProduct!!.weightStartProduct = weightStartProduct
        stringProduct!!.weightEndProduct = weightEndProduct
        stringProduct!!.weightCoffProduct = weightCoffProduct
        doc!!.save()
        refreshViewModel()
    }

    fun saveBox(
        index: Int?,
        weight: Float,
        barcode: String?,
        countBox: Int
    ) {
        var box: Box?

        if (index != null) {
            box = getBox(index)
        } else {
            box = Box()
            pallet!!.boxes.add(box)
        }

        box!!.barcode = barcode
        box.data = Date()
        box.weight = weight
        box.countBox = countBox

        doc!!.save()
        refreshViewModel()
    }

    fun addBox(weight: Float, barcode: String?) {
        var box = Box()

        box.barcode = barcode
        box.data = Date()
        box.weight = weight
        box.countBox = 1

        pallet!!.boxes.add(box)
        doc!!.save()
        refreshViewModel()
    }


}

class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




