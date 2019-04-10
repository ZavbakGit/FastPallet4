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

class Model(guid: String) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: CreatePallet
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()

    init {
        App.appComponent.inject(this)
        doc = interactorGetDoc.get(guid) as CreatePallet
        refreshViewModel()
    }

    fun refreshViewModel() {
        behaviorSubjectViewModel.onNext(
            ViewModel(
                info = "${doc?.description} ${doc?.guid ?: ""}",
                list = doc.stringProducts.map {
                    ItemList(
                        info = it.nameProduct
                    )
                }
            )
        )
    }

    fun createTestPallet() {
//        var strProd =  StringProduct()
//        strProd.nameProduct = "Product ${Random().nextInt(50).toString()}"
//        doc.addStringProduct(strProd)
//        doc.save()
//        refreshViewModel()
    }


}

class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




