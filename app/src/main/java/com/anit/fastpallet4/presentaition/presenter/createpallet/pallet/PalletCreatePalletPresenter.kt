package com.anit.fastpallet4.presentaition.presenter.createpallet.pallet


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.common.formatDate
import com.anit.fastpallet4.domain.intity.metaobj.*
import com.anit.fastpallet4.domain.intity.metaobj.Status.*
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.domain.utils.getWeightByBarcode
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.inventory.CreatePalletView
import com.anit.fastpallet4.presentaition.ui.util.EventKeyClick
import com.anit.fastpallet4.presentaition.ui.util.KeyKode
import com.anit.fastpallet4.presentaition.ui.util.getTotalBoxInfoByPallet
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

) : BasePresenter<CreatePalletView>(router) {

    var isShowDialog = false

    init {
        App.appComponent.inject(this)
    }

    private val model = Model(
        inputParamObj!!.guid,
        inputParamObj.guidStringProduct,
        inputParamObj.guidPallet
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

    fun readBarcode(barcode: String?) {
        barcode.let {
            var weight = getWeightByBarcode(
                barcode = it!!,
                start = model.stringProduct!!.weightStartProduct,
                finish = model.stringProduct!!.weightEndProduct,
                coff = model.stringProduct!!.weightCoffProduct
            )

            if (weight == 0f) {
                viewState.showSnackbarViewError("Не верный вес!")
            } else if (it.length != model.stringProduct!!.barcode?.length) {
                viewState.showSnackbarViewError("Не верная длинна штрихкода!")
            } else {
                model.addBox(weight, it)
            }
        }
    }

    fun onClickItem(index: Int) {
        viewState.showDialogBox(
            title = model.stringProduct!!.nameProduct ?: "",
            weight = model.getBox(index).weight,
            date = model.getBox(index).data ?: Date(),
            barcode = model.getBox(index).barcode,
            index = index

        )
    }

    fun onClickInfo() {

        viewState.showDialogProduct(
            title = model.stringProduct!!.nameProduct ?: "",
            weightStartProduct = model.stringProduct!!.weightStartProduct,
            weightEndProduct = model.stringProduct!!.weightEndProduct,
            weightCoffProduct = model.stringProduct!!.weightCoffProduct,
            barcode = model.stringProduct!!.barcode
        )
    }

    fun saveProduct(
        barcode: String?
        , weightStartProduct: Int
        , weightEndProduct: Int
        , weightCoffProduct: Float
    ) {
        model.saveProduct(
            barcode = barcode,
            weightStartProduct = weightStartProduct,
            weightEndProduct = weightEndProduct,
            weightCoffProduct = weightCoffProduct
        )

    }

    fun saveBox(barcode: String?, weight: Float, index: Int?) {
        if (weight == 0f) {
            viewState.showSnackbarViewError("Не верный вес!")
        } else {
            model.saveBox(
                index = index,
                barcode = barcode,
                weight = weight
            )
        }

    }

    fun onClickDell(id: Int) {
        when (model.doc!!.status) {
            NEW, LOADED -> {
                viewState.showDialogConfirmDell(id, "Удалить коробку?")
            }
            else -> viewState.showSnackbarViewError("Нельзя Удалять!")
        }

    }

    fun dellBox(id: Int) {
        model.dellBox(id)
    }


}

class Model(
    var guidDoc: String,
    var guidStringProduct: String,
    var guidPallet: String
) {

    @Inject
    lateinit var interactorGetDoc: UseCaseGetMetaObj
    var doc: CreatePallet? = null
    var stringProduct: StringProduct? = null
    var pallet: Pallet? = null
    var behaviorSubjectViewModel = BehaviorSubject.create<ViewModel>()

    var viewModel: ViewModel? = null

    init {
        App.appComponent.inject(this)

    }

    fun getStringProducts(guid: String): StringProduct {
        return doc!!.stringProducts.find { it.guid == guid }!!
    }

    fun getPallet(guid: String): Pallet {
        return stringProduct!!.pallets.find { it.guid == guid }!!
    }

    fun refreshViewModel() {

        doc = interactorGetDoc.get(guidDoc) as CreatePallet
        stringProduct = getStringProducts(guidStringProduct)
        pallet = getPallet(guidPallet)


        var totalInfoPall = getTotalBoxInfoByPallet(pallet!!)

        var list = pallet!!.boxes.map {
            ItemList(
                info = "${it.weight.toString()} / 1",
                left = "${formatDate(it.data)}",
                guid = it.guid,
                data = it.data
            )
        }
            .sortedByDescending { it.data }

        viewModel = ViewModel(
            info = "${pallet?.number}",
            list = list,
            left = "${formatDate(pallet!!.dataChanged)}",
            right = "${totalInfoPall.weight} / ${totalInfoPall.countBox} / ${totalInfoPall.countPallet} "
        )


        behaviorSubjectViewModel.onNext(viewModel!!)

    }

    fun dellBox(index: Int) {
        var guid = getBox(index).guid
        pallet!!.boxes.removeAll { it.guid == guid }
        doc!!.save()
        refreshViewModel()
    }

    fun getBox(index: Int): Box {
        var guid = viewModel!!.list.get(index).guid
        return pallet!!.boxes.find { it.guid!!.equals(guid) }!!
    }

    fun saveProduct(
        barcode: String?,
        weightStartProduct: Int,
        weightEndProduct: Int,
        weightCoffProduct: Float
    ) {

        stringProduct!!.barcode = barcode
        stringProduct!!.weightStartProduct = weightStartProduct
        stringProduct!!.weightEndProduct = weightEndProduct
        stringProduct!!.weightCoffProduct = weightCoffProduct
        doc!!.save()
        refreshViewModel()
    }

    fun saveBox(
        index: Int?,
        weight: Float,
        barcode: String?
    ) {
        var box: Box?

        if (index != null) {
            box = getBox(index)
        } else {
            box = Box()
            pallet!!.boxes.add(box)
        }

        box.barcode = barcode
        box.data = Date()
        box.weight = weight

        doc!!.save()
        refreshViewModel()
    }

    fun addBox(weight: Float, barcode: String?) {
        var box = Box()

        box.barcode = barcode
        box.data = Date()
        box.weight = weight

        pallet!!.boxes.add(box)
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




