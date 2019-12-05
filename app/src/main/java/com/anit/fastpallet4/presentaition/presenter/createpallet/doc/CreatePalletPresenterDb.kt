package com.anit.fastpallet4.presentaition.presenter.createpallet.doc

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.usecasenew.UseCaseGetDocument
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.doc.CreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.product.ProductCreatePalletFrScreen
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class CreatePalletPresenterDb(
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

class ModelDb(var guid: String) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetDocument
    var doc: CreatePallet? = null
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModelDb>()
    var viewModel: ViewModelDb? = null

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

            var summPalletInfoStr = it.getSummPalletInfoFromPalletBoxes()



            ItemList(
                info = it.nameProduct,
                left = "${it.count} / ${it.countBox} ",
                right = "${summPalletInfoStr.count} / ${summPalletInfoStr.countBox} / ${summPalletInfoStr.countPallet} ",
                guid = it.guid
            )
        }


        //Это нумерация
        list.forEachIndexed { index, itemList ->
            itemList.info = "${list.size - index}. ${itemList.info}"
        }


        viewModel = ViewModelDb(
            info = "${doc?.description} ${doc?.guid ?: ""}",
            list = list
        )

        behaviorSubjectViewModel.onNext(viewModel!!)
    }


}

class ViewModelDb(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




