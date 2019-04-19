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
import com.anit.fastpallet4.presentaition.ui.util.getTotalBoxInfoByPallet
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

    fun onStart() {
        model.refreshViewModel()
    }

    fun getViewModelFlowable() = model.behaviorSubjectViewModel
        .toFlowable(BackpressureStrategy.BUFFER)

    fun onClickItem(index: Int) {
        router.navigateTo(
            screens.getProductCreatePalletFrScreen(
                ProductCreatePalletFrScreen
                    .InputParamObj(
                        guid = inputParamObj!!.guid,
                        guidStringProduct = model.getStringProductByIndex(index).guid!!
                    )
            )
        )

    }

}

class Model(var guid: String) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: CreatePallet? = null
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()
    var viewModel: ViewModel? = null


    init {
        App.appComponent.inject(this)
    }

    fun getStringProductByIndex(index: Int): StringProduct {
        var guid = viewModel!!.list.get(index).guid!!
        return doc!!.getStringProductByGuid(guid)!!
    }

    fun refreshViewModel() {
        doc = interactorGetDoc.get(guid) as CreatePallet
        var list = doc!!.stringProducts.map {

            var totalInfo = getTotalBoxInfoByPallet(it)

            ItemList(
                info = it.nameProduct,
                left = "${it.count} / ${it.countBox} ",
                right = "${totalInfo.weight} / ${totalInfo.countBox} / ${totalInfo.countPallet} ",
                guid = it.guid
            )
        }


        list.forEachIndexed { index, itemList ->
            itemList.info = "${list.size - index}. ${itemList.info}"
        }


        viewModel = ViewModel(
            info = "${doc?.description} ${doc?.guid ?: ""}",
            list = list
        )

        behaviorSubjectViewModel.onNext(viewModel!!)
    }


}

class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




