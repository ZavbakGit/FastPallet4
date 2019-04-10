package com.anit.fastpallet4.presentaition.presenter.createpallet.product


import android.annotation.SuppressLint
import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Pallet
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.domain.utils.getNumberDocByBarCode
import com.anit.fastpallet4.domain.utils.isPallet
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.product.ProductCreatePalletFrScreen
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class ProductCreatePalletPresenter(
    router: Router,
    private val inputParamObj: ProductCreatePalletFrScreen.InputParamObj?

) : BasePresenter<BaseView>(router) {

    init {
        App.appComponent.inject(this)
    }

    private val model = Model(
        inputParamObj!!.guid,
        inputParamObj.indexProd
    )

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun getViewModelFlowable() = model.behaviorSubjectViewModel
        .toFlowable(BackpressureStrategy.BUFFER)

    fun readBarcode(barcode: String) {
        model.createPaletByBarcode(barcode)
            .subscribe({
                viewState.showSnackbarViewMess("Ок")
            }, {
                viewState.showSnackbarViewError(it.message.toString())
            })

    }

    fun onClickItem(index: Int?) {
        index.let {
            router.navigateTo(
                screens.getPalletCreatePalletFrScreen(
                    PalletCreatePalletFrScreen
                        .InputParamObj(
                            guid = inputParamObj!!.guid,
                            indexProd = inputParamObj.indexProd,
                            indexPallet = index!!
                        )
                )
            )
        }
    }


}

class Model(
    guid: String
    , indexProduct: Int
) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: CreatePallet
    var stringProduct: StringProduct
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()

    init {
        App.appComponent.inject(this)
        doc = interactorGetDoc.get(guid) as CreatePallet
        stringProduct = doc.stringProducts.get(indexProduct)

        refreshViewModel()
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

                return@map pallet
            }
            .flatMap { pallet ->
                var listPallet = doc.stringProducts
                    .flatMap {
                        it.pallets.map {
                            it
                        }
                    }

                if (listPallet.filter { it.number.equals(pallet.number, true) }.size > 0) {
                    return@flatMap Flowable.error<Throwable>(Throwable("Эта паллета уже есть!"))
                } else {
                    return@flatMap Flowable.just(pallet)
                }

            }
            .doOnNext {
                stringProduct.addPallet(it as Pallet)
                doc.save()
                refreshViewModel()
            }
            .ignoreElements()
    }

    fun refreshViewModel() {
        behaviorSubjectViewModel.onNext(
            ViewModel(
                info = "${stringProduct?.nameProduct}",
                list = stringProduct.pallets.map {
                    ItemList(
                        info = it.number
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




