package com.anit.fastpallet4.presentaition.presenter.createpallet.doc


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Pallet
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.doc.CreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.product.ProductCreatePalletFrScreen
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

@InjectViewState
class CreatePalletPresenter(
    router: Router,
    private val inputParamObj: CreatePalletFrScreen.InputParamObj?

) : BasePresenter<BaseView>(router) {

    init {
        App.appComponent.inject(this)
    }

    private val model = Model(inputParamObj!!.guid)

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun onStart(){
        model.refreshViewModel()
    }

    fun getViewModelFlowable() = model.behaviorSubjectViewModel
        .toFlowable(BackpressureStrategy.BUFFER)

    fun onClickItem(index: Int?) {
        index.let {
            router.navigateTo(
                screens.getProductCreatePalletFrScreen(
                    ProductCreatePalletFrScreen
                        .InputParamObj(
                            guid = inputParamObj!!.guid,
                            indexProd = index!!
                        )
                )
            )
        }
    }

}

class Model(var guid: String) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: CreatePallet? = null
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()

    init {
        App.appComponent.inject(this)

    }

    fun refreshViewModel() {
        doc = interactorGetDoc.get(guid) as CreatePallet
        behaviorSubjectViewModel.onNext(
            ViewModel(
                info = "${doc?.description} ${doc?.guid ?: ""}",
                list = doc!!.stringProducts.map {

                   var listBox =  it.pallets
                        .flatMap {
                            it.boxes
                        }
                        .map {
                            BigDecimal(it.weight.toString())
                        }


                   var weight =  listBox.fold(BigDecimal(0)){total:BigDecimal,next:BigDecimal ->total.add(next)}


                    ItemList(
                        info = it.nameProduct,
                        left = "${it.count} / ${it.countBox}",
                        right = "$weight / ${listBox.size}"
                    )
                }
            )
        )
    }


}

class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




