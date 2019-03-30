package com.anit.fastpallet4.presentaition.presenter.Inventory


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.metaobj.Box
import com.anit.fastpallet4.domain.intity.metaobj.InventoryPallet
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.Inventory.InventoryFrScreen
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class InventoryPresenter(
    router: Router,
    private val inputParamObj: InventoryFrScreen.InputParamObj?

) : BasePresenter<BaseView>(router) {

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

    fun dellBarcode(index:Int){
        doc.dellBox(index)
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




