package com.anit.fastpallet4.presentaition.presenter.action.product


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.common.formatDate
import com.anit.fastpallet4.domain.intity.metaobj.*
import com.anit.fastpallet4.domain.usecase.UseCaseGetInfoPallet
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.domain.utils.getNumberDocByBarCode
import com.anit.fastpallet4.domain.utils.getWeightByBarcode
import com.anit.fastpallet4.domain.utils.isPallet
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.action.product.ProductActionPalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.inventory.ActionPalletProductView

import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class ProductActionPalletPresenter(
    router: Router,
    private val inputParamObj: ProductActionPalletFrScreen.InputParamObj?

) : BasePresenter<ActionPalletProductView>(router) {

    init {
        App.appComponent.inject(this)
    }

    var isShowDialog = false

    private val model = Model(
        inputParamObj!!.guid,
        inputParamObj.guidStringProduct
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


    fun saveProduct(
        barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float
    ) {
        if (model.doc!!.onlyRead()) {
            viewState.showSnackbarViewError("Нельзя Изменять!")
            return
        }

        model.saveProduct(
            barcode = barcode,
            weightStartProduct = weightStartProduct,
            weightEndProduct = weightEndProduct,
            weightCoffProduct = weightCoffProduct
        )

    }

    fun saveBox(barcode: String?, weight: Float, index: Int?, countBox: Int) {
        if (model.doc!!.onlyRead()) {
            viewState.showSnackbarViewError("Нельзя Изменять!")
            return
        }

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

    fun readBarcode(barcode: String) {
        if (model.doc!!.onlyRead()) {
            viewState.showSnackbarViewError("Нельзя Изменять!")
            return
        }


        if (isPallet(barcode)) {
            //Если это паллета

            model.createPaletByBarcode(barcode)
                .subscribe({
                    //viewState.showSnackbarViewMess("Ок")
                }, {
                    viewState.showSnackbarViewError(it.message.toString())
                })
        } else {
            //Если это не паллета
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

    }

    fun onClickInfo() {
        var stringProduct = model.stringProduct
        viewState.showDialogProduct(
            title = stringProduct!!.nameProduct ?: "",
            weightStartProduct = stringProduct.weightStartProduct,
            weightEndProduct = stringProduct.weightEndProduct,
            weightCoffProduct = stringProduct.weightCoffProduct,
            barcode = stringProduct.barcode
        )
    }

    fun onClickItem(index: Int) {
        var box = model.getBoxByIndex(index)

        if (box != null) {
            viewState.showDialogBox(
                title = model.stringProduct!!.nameProduct ?: "",
                weight = box.weight,
                date = box.data ?: Date(),
                barcode = box.barcode,
                index = index,
                countBox = box.countBox
            )
        }
    }

    fun onClickDell(index: Int) {
        if (model.doc!!.onlyRead()) {
            viewState.showSnackbarViewError("Нельзя Изменять!")
            return
        }

        viewState.showDialogConfirmDell(index, "Удалить?!")
    }


    fun dellPallet(id: Int) {
        if (model.doc!!.onlyRead()) {
            viewState.showSnackbarViewError("Нельзя Изменять!")
            return
        }
        model.dell(id)
    }

    fun loadInfoPallets() {

        if (model.doc!!.onlyRead()) {
            viewState.showSnackbarViewError("Нельзя Изменять!")
            return
        }

        model.getInfoPalletFromServer()
            .subscribe({
                viewState.showSnackbarViewMess("Ок")
            },
                { viewState.showSnackbarViewError(it.message.toString()) }
            )
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
}

class Model(
    var guidDoc: String,
    var guidStringProduct: String
) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: ActionPallet? = null
    var stringProduct: StringProduct? = null
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()
    var viewModel: ViewModel? = null

    @Inject
    lateinit var interactorGetInfoPallet: UseCaseGetInfoPallet

    init {
        App.appComponent.inject(this)
    }


    fun refreshViewModel() {
        doc = interactorGetDoc.get(guidDoc) as ActionPallet
        stringProduct = doc!!.getStringProductByGuid(guidStringProduct)


        var listBox = stringProduct!!.boxes.map {
            ItemList(
                info = "${it.weight} / ${it.countBox}",
                left = "${formatDate(it.data)}",
                right = "${it.weight} / ${it.countBox}",
                guid = it.guid,
                data = it.data,
                type = 2
            )
        }

        var listPallet = stringProduct!!.pallets.map {

            var errorString = ""

            if (!it.guidProduct.isNullOrEmpty()) {
                if (!it.guidProduct.equals(stringProduct!!.guidProduct, true)) {
                    errorString = "!!! Товар не тот ${it.nameProduct}"
                }
            }

            ItemList(
                info = "${it.number} $errorString",
                left = "${formatDate(it.dataChanged)}",
                right = "${it.count} / ${it.countBox} / 1 ",
                data = it.dataChanged,
                guid = it.guid,
                type = 1
            )
        }


        var list = listBox + listPallet


        var summPalletInfo = stringProduct!!.getSummPalletInfoForAction()

        viewModel = ViewModel(
            info = "${stringProduct?.nameProduct}",
            right = "${summPalletInfo.count} / ${summPalletInfo.countBox} / ${summPalletInfo.countPallet}",
            left = "${stringProduct!!.count} / ${stringProduct!!.countBox}",
            list = list.sortedWith(compareBy<ItemList> { it.type }.thenByDescending { it.data })
        )


        behaviorSubjectViewModel.onNext(viewModel!!)
    }

    fun getPalletByIndex(index: Int): Pallet? {
        var guid = viewModel!!.list.get(index).guid!!
        return stringProduct!!.getPalletByGuid(guid)
    }

    fun getBoxByIndex(index: Int): Box? {
        if (viewModel!!.list.get(index).type == 1) {
            return null
        }
        var guid = viewModel!!.list.get(index).guid
        return stringProduct!!.boxes.find { it.guid == guid }!!
    }

    fun dell(index: Int) {

        if (viewModel!!.list.get(index).type == 1) {
            var guid = getPalletByIndex(index)!!.guid
            stringProduct!!.dellPalletByGuid(guid!!)
        } else {
            var guid = getBoxByIndex(index)!!.guid
            stringProduct!!.dellBoxByGuid(guid!!)
        }

        doc!!.save()
        refreshViewModel()
    }

    fun addBox(weight: Float, barcode: String?, countBox: Int = 1) {
        var box = Box()

        box.barcode = barcode
        box.data = Date()
        box.weight = weight
        box.countBox = countBox

        stringProduct!!.boxes.add(box)
        doc!!.save()
        refreshViewModel()
    }

    fun saveProduct(
        barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float
    ) {

        stringProduct!!.barcode = barcode
        stringProduct!!.weightStartProduct = weightStartProduct
        stringProduct!!.weightEndProduct = weightEndProduct
        stringProduct!!.weightCoffProduct = weightCoffProduct
        doc!!.save()
        refreshViewModel()
    }

    fun saveBox(index: Int?, weight: Float, barcode: String?, countBox: Int) {
        var box: Box?
        if (index != null) {
            box = getBoxByIndex(index)
        } else {
            box = Box()
            stringProduct!!.addBox(box)
        }

        box!!.barcode = barcode
        box.data = Date()
        box.weight = weight
        box.countBox = countBox

        doc!!.save()
        refreshViewModel()
    }

    fun getInfoPalletFromServer(): Completable {

        var listNumber =
            doc!!.stringProducts.flatMap {
                it.pallets
            }.map {
                it.number ?: ""
            }

        return interactorGetInfoPallet
            .load(listNumber)
            .toFlowable()
            .flatMap {
                Flowable.fromIterable(it)
            }.doOnNext { infoPall ->
                var pall = doc!!.stringProducts
                    .flatMap {
                        it.pallets
                    }
                    .find {
                        infoPall.code.equals(it.number, true)
                    }

                pall.let {
                    it!!.count = infoPall.count
                    it.countBox = infoPall.countBox
                    it.guidProduct = infoPall.guid
                    it.nameProduct = infoPall.nameProduct
                }
            }
            .doFinally {
                doc!!.save()
                refreshViewModel()
            }
            .ignoreElements()
    }

    fun createPaletByBarcode(barcode: String): Completable {
        return Flowable.just(barcode)
            .flatMap {
                if (isPallet(barcode)) {
                    return@flatMap Flowable.just(it)
                } else {
                    return@flatMap Flowable.error<Throwable>(Throwable("Это не паллета!"))
                }
            }
            .map {
                it as String
            }
            .map {
                var pallet = Pallet()
                pallet.barcode = it
                pallet.number = getNumberDocByBarCode(it)
                pallet.dataChanged = Date()

                return@map pallet
            }
            .flatMap { pallet ->
                var listPallet = doc!!.stringProducts
                    .flatMap {
                        it.pallets
                    }

                if (listPallet.filter { it.number.equals(pallet.number, true) }.size > 0) {
                    return@flatMap Flowable.error<Throwable>(Throwable("Эта паллета уже есть!"))
                } else {
                    return@flatMap Flowable.just(pallet)
                }
            }
            .doOnNext {
                stringProduct!!.addPallet(it as Pallet)
                doc!!.save()
                refreshViewModel()
            }
            .ignoreElements()
    }

}


class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




