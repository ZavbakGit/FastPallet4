package com.anit.fastpallet4.presentaition.presenter.infopallet


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.common.formatDate
import com.anit.fastpallet4.domain.intity.extra.InfoPallet
import com.anit.fastpallet4.domain.intity.metaobj.Pallet
import com.anit.fastpallet4.domain.usecase.UseCaseGetInfoPallet
import com.anit.fastpallet4.domain.utils.getNumberDocByBarCode
import com.anit.fastpallet4.domain.utils.getWeightByBarcode
import com.anit.fastpallet4.domain.utils.isPallet
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.infopallet.InfoPalletsFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryView
import com.arellomobile.mvp.InjectViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class InfoPalletPresenter(
    router: Router,
    private val inputParamObj: InfoPalletsFrScreen.InputParamObj?

) : BasePresenter<BaseView>(router) {

    private val model = Model()

    var isShowDialog = false

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

        if (!isPallet(barcode)) {
            viewState.showSnackbarViewError("Не паллета!")
            return
        }
        model.createPaletByBarcode(barcode)
            .subscribe({
                //viewState.showSnackbarViewMess("Ок")
            }, {
                viewState.showSnackbarViewError(it.message.toString())
            })

    }


    fun loadInfoPallet() {


    }

    fun onClickLoad(index: Int) {
        loadInfoPallet()
    }
}

class Model {

    @Inject
    lateinit var interactorGetInfoPallet: UseCaseGetInfoPallet

    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()
    var viewModel: ViewModel? = null
    var pallets: MutableList<Pallet> = mutableListOf()


    init {
        App.appComponent.inject(this)
    }

    fun refreshViewModel() {

        var list = pallets.map {
            ItemList(
                info = "${it.number} \n${it.nameProduct} \n${it.sclad} \n${it.state} ",
                left = "${formatDate(it.dataChanged)}",
                right = "${it.count} / ${it.countBox} / 1 ",
                data = it.dataChanged,
                guid = it.guid,
                type = 1
            )
        }

        viewModel = ViewModel(
            list = list.sortedByDescending { it.data }
        )

        behaviorSubjectViewModel.onNext(viewModel!!)
    }


    fun getInfoPalletFromServer(pallet: Pallet): Completable {

        var listNumber = listOf(pallet.number ?: "")

        return interactorGetInfoPallet
            .load(listNumber)
            .toFlowable()
            .flatMap {
                Flowable.fromIterable(it)
            }.doOnNext { infoPall ->
                var pall = pallets
                    .find {
                        infoPall.code.equals(it.number, true)
                    }

                pall.let {
                    it!!.count = infoPall.count
                    it.countBox = infoPall.countBox
                    it.guidProduct = infoPall.guid
                    it.nameProduct = infoPall.nameProduct
                    it.sclad = infoPall.sclad
                    it.state = infoPall.state
                }
            }
            .doFinally {
                refreshViewModel()
            }
            .ignoreElements()
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
            .doOnNext {pall->

                var pallet = pallets.find { it.number == pall.number }
                if (pallet == null){
                    pallets!!.add(pall)
                }else{
                    pallet.dataChanged = Date()
                }

            }
            .flatMap {
                interactorGetInfoPallet
                    .load(listOf(it.number ?: ""))
                    .toFlowable()
            }
            .flatMap {
                Flowable.fromIterable(it)
            }
            .doOnNext { infoPall ->
                var pall = pallets
                    .find {
                        infoPall.code.equals(it.number, true)
                    }

                pall.let {
                    it!!.count = infoPall.count
                    it.countBox = infoPall.countBox
                    it.guidProduct = infoPall.guid
                    it.nameProduct = infoPall.nameProduct
                    it.sclad = infoPall.sclad
                    it.state = infoPall.state
                }
            }
            .doFinally {
                refreshViewModel()
            }
            .ignoreElements()
    }

}

class ViewModel(
    var info: String? = null,
    var left: String? = null,
    var right: String? = null,
    var list: List<ItemList>
)




