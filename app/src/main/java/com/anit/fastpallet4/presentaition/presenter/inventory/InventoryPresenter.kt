package com.anit.fastpallet4.presentaition.presenter.inventory


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.metaobj.Box
import com.anit.fastpallet4.domain.intity.metaobj.InventoryPallet
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryView
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class InventoryPresenter(
    router: Router,
    private val inputParamObj: InventoryFrScreen.InputParamObj?

) : BasePresenter<InventoryView>(router) {

    private val model = Model(inputParamObj!!.guid)

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun getViewModelFlowable() = model.behaviorSubjectViewModel
        .toFlowable(BackpressureStrategy.BUFFER)

    fun readBarcode(barcode: String) {
        model.addBarcode(barcode)
    }

    fun onClickItem(index: Int?) {
        index.let {
            model.dellBarcode(index!!)
        }
    }

    fun onClickInfo() {
        var doc = model.doc
        viewState.showDialogProduct(
            title = doc.guid ?: "",
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


}

class Model(guid: String) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: InventoryPallet
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()

    init {
        App.appComponent.inject(this)
        doc = interactorGetDoc.get(guid) as InventoryPallet
        refreshViewModel()

    }

    fun refreshViewModel() {
        behaviorSubjectViewModel.onNext(
            ViewModel(
                info = "${doc?.description} ${doc?.guid ?: ""}",
                list = doc.stringProduct.boxes.map {
                    ItemList(
                        info = it.weight.toString()
                    )
                }
            )
        )
    }

    fun addBarcode(barcode: String) {
        var box = Box()
        box.weight = barcode.toFloatOrNull() ?: 0f
        doc.addBox(box)
        doc.save()
        refreshViewModel()
    }

    fun dellBarcode(index: Int) {
        doc.dellBox(index)
        doc.save()
        refreshViewModel()
    }

    fun saveProduct(
        barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float
    ) {

        doc.stringProduct.barcode = barcode
        doc.stringProduct.weightStartProduct = weightStartProduct
        doc.stringProduct.weightEndProduct = weightEndProduct
        doc.stringProduct.weightCoffProduct = weightCoffProduct
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




