package com.anit.fastpallet4.presentaition.presenter.createpallet.pallet


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.metaobj.Box
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Pallet
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.domain.utils.getWeightByBarcode
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
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
        inputParamObj.indexProd,
        inputParamObj.indexPallet
    )

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun getViewModelFlowable() = model.behaviorSubjectViewModel
        .toFlowable(BackpressureStrategy.BUFFER)

    fun readBarcode(barcode: String?) {
        barcode.let {
            var weight = getWeightByBarcode(
                barcode = it!!,
                start = model.stringProduct.weightStartProduct,
                finish = model.stringProduct.weightEndProduct,
                coff = model.stringProduct.weightCoffProduct
            )

            if (weight == 0f) {
                viewState.showSnackbarViewError("Не верный вес!")
            } else if (it.length != model.stringProduct.barcode?.length) {
                viewState.showSnackbarViewError("Не верная длинна штрихкода!")
            } else {
                model.addBox(weight, it)
            }
        }
    }

    fun onClickItem(index: Int?) {
        viewState.showDialogBox(
            title = model.stringProduct?.nameProduct ?: "",
            weight = model.getBox(index!!).weight,
            date = model.getBox(index!!).data ?: Date(),
            barcode = model.getBox(index!!).barcode,
            index = index

        )
    }

    fun onClickInfo() {

        viewState.showDialogProduct(
            title = model.stringProduct.nameProduct?: "",
            weightStartProduct = model.stringProduct.weightStartProduct,
            weightEndProduct = model.stringProduct.weightEndProduct,
            weightCoffProduct = model.stringProduct.weightCoffProduct,
            barcode = model.stringProduct.barcode
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

    fun saveBox(barcode: String?, weight: Float, index: Int?) {
        if(weight == 0f){
            viewState.showSnackbarViewError("Не верный вес!")
        }else{
            model.saveBox(
                index = index,
                barcode = barcode,
                weight = weight
            )
        }

    }




}

class Model(
    guid: String,
    indexProduct: Int
    , indexPallet: Int
) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: CreatePallet
    var stringProduct: StringProduct
    var pallet: Pallet
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()

    init {
        App.appComponent.inject(this)
        doc = interactorGetDoc.get(guid) as CreatePallet
        stringProduct = doc.stringProducts.get(indexProduct)
        pallet = stringProduct.pallets.get(indexPallet)

        refreshViewModel()
    }

    fun refreshViewModel() {
        behaviorSubjectViewModel.onNext(
            ViewModel(
                info = "${pallet?.number}",
                list = pallet.boxes.map {
                    ItemList(
                        info = it.weight.toString()
                    )
                }
            )
        )
    }


    fun dellBox(index: Int) {
        pallet.dellBox(index)
        doc.save()
        refreshViewModel()
    }

    fun getBox(index: Int): Box {
        return pallet.boxes.get(index)
    }

    fun saveProduct(
        barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float
    ) {

        stringProduct.barcode = barcode
        stringProduct.weightStartProduct = weightStartProduct
        stringProduct.weightEndProduct = weightEndProduct
        stringProduct.weightCoffProduct = weightCoffProduct
        doc.save()
        refreshViewModel()

    }

    fun saveBox(index: Int?, weight: Float, barcode: String?) {
        var box:Box?
        if (index != null){
            box = getBox(index)
        }else{
            box = Box()
            pallet.boxes.add(box)
        }

        box.barcode = barcode
        box.data = Date()
        box.weight = weight

        doc.save()
        refreshViewModel()
    }

    fun addBox(weight: Float, barcode: String?) {
        var box = Box()

        box.barcode = barcode
        box.data = Date()
        box.weight = weight

        pallet.boxes.add(box)
        doc.save()
        refreshViewModel()
    }


}

class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




