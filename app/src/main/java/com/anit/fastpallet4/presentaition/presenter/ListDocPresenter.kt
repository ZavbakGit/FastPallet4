package com.anit.fastpallet4.presentaition.presenter

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.Type
import com.anit.fastpallet4.domain.intity.Type.CREATE_PALLET
import com.anit.fastpallet4.domain.intity.Type.INVENTORY_PALLET
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Status.NEW
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetListMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorCreatorMetaObj
import com.anit.fastpallet4.presentaition.presenter.Model.MAIN_MENU.*
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.doc.CreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.listdoc.ListDocFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.listdoc.ListDocView
import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class ListDocPresenter(

    router: Router,
    val inputParamObj: ListDocFrScreen.InputParamObj?

) : BasePresenter<ListDocView>(router) {

    val model = Model()

    init {
        App.appComponent.inject(this)
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    fun getFlowableListItem() = model.getFlowableListItem()

    fun onClickMainMenu() {
        viewState.showMainMenu(model.getMainMenu())
    }

    fun onClickMainPopMenu(itemId: Int): Boolean {
        when (model.getMainMenuById(itemId)) {
            INVENTORY -> model.createNewInventory()
            LOAD -> model.createTestCreatePallet()
        }
        return true
    }

    fun onClickItem(guid: String, type: Int?) {

        var type = Type.getTypeById(type ?: 0)


        when (type) {
            INVENTORY_PALLET -> router.navigateTo(
                screens.getInventoryScreen(InventoryFrScreen.InputParamObj(guid = guid))
            )

            CREATE_PALLET -> router.navigateTo(
                screens.getCreatePalletFrScreen(CreatePalletFrScreen.InputParamObj(guid = guid))
            )

        }


    }

}


class Model {

    @Inject
    lateinit var interactorGetList: UseCaseGetListMetaObj

    init {
        App.appComponent.inject(this)
    }

    enum class MAIN_MENU(val title: String, val id: Int) {
        LOAD("Загрузить", 1),
        UNLOAD("Выгрузить базу в файл", 2),
        INVENTORY("Инвентаризация паллеты", 3),
        SETTINGS("Настройки", 4);
    }

    fun getMainMenuById(id: Int): MAIN_MENU? {
        return when (id) {
            1 -> return LOAD
            2 -> return UNLOAD
            3 -> return INVENTORY
            4 -> return SETTINGS
            else -> null
        }
    }

    fun getMainMenu(): List<Pair<Int, String>> {
        return listOf(
            Pair(LOAD.id, LOAD.title),
            Pair(INVENTORY.id, INVENTORY.title),
            Pair(UNLOAD.id, UNLOAD.title),
            Pair(SETTINGS.id, SETTINGS.title)
        )

    }

    fun createNewInventory() {
        var interactor = InteractorCreatorMetaObj(INVENTORY_PALLET)
        var doc = interactor.create()
        doc.date = Date()
        doc.status = NEW
        doc.description = "Инвентаризация"

        doc.save()
    }

    fun createTestCreatePallet() {
        var interactor = InteractorCreatorMetaObj(CREATE_PALLET)
        var doc = interactor.create() as CreatePallet
        doc.date = Date()
        doc.status = NEW
        doc.description = "Формирование паллет"

        (0..Random().nextInt(5)).forEach {
            var strProd = StringProduct()
            strProd.nameProduct = "Product $it"
            doc.stringProducts.add(strProd)
        }

        doc.save()
    }

    fun getFlowableListItem() =
        interactorGetList.get()
            .map { it ->
                it.map {
                    ItemList(
                        identifier = it!!.getGuid(),
                        info = "${it.description} ${it.getGuid() ?: ""}",
                        type = it.type.id

                    )
                }
            }
}





