package com.anit.fastpallet4.presentaition.presenter.createpallet.product


import android.annotation.SuppressLint
import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.common.formatDate
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Pallet
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.domain.utils.getNumberDocByBarCode
import com.anit.fastpallet4.domain.utils.isPallet
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.product.ProductCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.inventory.CreatePalletProductView
import com.anit.fastpallet4.presentaition.ui.util.getTotalBoxInfoByPallet
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

@InjectViewState
class ProductCreatePalletPresenter(
    router: Router,
    private val inputParamObj: ProductCreatePalletFrScreen.InputParamObj?

) : BasePresenter<CreatePalletProductView>(router) {

    init {
        App.appComponent.inject(this)
    }

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

    fun readBarcode(barcode: String) {
        when (model.doc!!.status) {
            Status.NEW, Status.LOADED -> {
                model.createPaletByBarcode(barcode)
                    .subscribe({
                        //viewState.showSnackbarViewMess("Ок")
                    }, {
                        viewState.showSnackbarViewError(it.message.toString())
                    })
            }
            else -> viewState.showSnackbarViewError("Нельзя Изменять!")
        }
    }

    fun onClickItem(index: Int) {
        router.navigateTo(
            screens.getPalletCreatePalletFrScreen(
                PalletCreatePalletFrScreen
                    .InputParamObj(
                        guid = inputParamObj!!.guid,
                        guidStringProduct = inputParamObj.guidStringProduct,
                        guidPallet = model.getPalletByIndex(index)!!.guid!!
                    )
            )
        )

    }

    fun onClickDell(index: Int) {
        when (model.doc!!.status) {
            Status.NEW, Status.LOADED -> {

                val pallet = model.getPalletByIndex(index)
                var count = pallet!!.boxes.size
                viewState.showDialogConfirmDell(index, "Удалить паллету? \n Коробок - $count")
            }
            else -> viewState.showSnackbarViewError("Нельзя Удалять!")
        }
    }

    fun dellPallet(id: Int) {
        model.dellPallet(id)
    }
}

class Model(
    var guidDoc: String,
    var guidStringProduct: String
) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: CreatePallet? = null
    var stringProduct: StringProduct? = null
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()
    var viewModel: ViewModel? = null

    init {
        App.appComponent.inject(this)
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

    fun refreshViewModel() {
        doc = interactorGetDoc.get(guidDoc) as CreatePallet
        stringProduct = doc!!.getStringProductByGuid(guidStringProduct)


        var totalInfoStr = getTotalBoxInfoByPallet(stringProduct!!)

        viewModel = ViewModel(
            info = "${stringProduct?.nameProduct}",
            left = "${stringProduct!!.count} / ${stringProduct!!.countBox}",
            right = "${totalInfoStr.weight} / ${totalInfoStr.countBox} / ${totalInfoStr.countPallet}",
            list = stringProduct!!.pallets
                .map {

                    var totalInfoPall = getTotalBoxInfoByPallet(it)

                    ItemList(
                        info = it.number,
                        left = "${formatDate(it.dataChanged)}",
                        right = "${totalInfoPall.weight} / ${totalInfoPall.countBox} / ${totalInfoPall.countPallet} ",
                        data = it.dataChanged,
                        guid = it.guid
                    )
                }
                .sortedByDescending { it.data }
        )

        behaviorSubjectViewModel.onNext(viewModel!!)
    }



    fun getPalletByIndex(index: Int): Pallet? {
        var guid =  viewModel!!.list.get(index).guid!!
        return stringProduct!!.getPalletByGuid(guid)
    }



    fun dellPallet(index: Int) {
        var guid = getPalletByIndex(index)!!.guid
        stringProduct!!.dellPalletByGuid(guid!!)
        doc!!.save()
        refreshViewModel()
    }

}

class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




