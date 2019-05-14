package com.anit.fastpallet4.presentaition.presenter.action.doc


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.metaobj.ActionPallet
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.presentaition.ui.screens.action.doc.ActionPalletFrScreen
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.action.product.ProductActionPalletFrScreen

import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router

import javax.inject.Inject

@InjectViewState
class ActionPalletPresenter(
    router: Router,
    private val inputParamObj: ActionPalletFrScreen.InputParamObj?

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
            screens.getProductActionPalletFrScreen(
                ProductActionPalletFrScreen
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
    var doc: ActionPallet? = null
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
        doc = interactorGetDoc.get(guid) as ActionPallet
        var list = doc!!.stringProducts.map {

            var summPalletInfo = it.getSummPalletInfoForAction()

            ItemList(
                info = it.nameProduct,
                left = "${it.count} / ${it.countBox} ",
                right = "${summPalletInfo.count} / ${summPalletInfo.countBox} / ${summPalletInfo.countPallet} ",
                guid = it.guid
            )
        }


        list.forEachIndexed { index, itemList ->
            itemList.info = "${list.size - index}. ${itemList.info}"
        }


        viewModel = ViewModel(
            info = "${doc?.description}",
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




