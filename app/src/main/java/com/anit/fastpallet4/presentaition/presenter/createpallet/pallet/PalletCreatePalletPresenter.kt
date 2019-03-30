package com.anit.fastpallet4.presentaition.presenter.createpallet.pallet


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.metaobj.Box
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Pallet
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
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

) : BasePresenter<BaseView>(router) {

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

    fun readBarcode(barcode: String) {
        model.createTestBox()
    }

    fun onClickItem(index: Int?) {
        model.dellBox(index!!)
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

    fun createTestBox() {
        var box = Box()
        box.weight = Random().nextInt(50).toFloat()
        pallet.boxes.add(box)

        doc.save()
        refreshViewModel()
    }

    fun dellBox(index: Int) {
        pallet.dellBox(index)
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




