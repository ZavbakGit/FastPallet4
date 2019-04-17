package com.anit.fastpallet4.presentaition.presenter

import android.os.Handler
import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.Type
import com.anit.fastpallet4.domain.intity.Type.CREATE_PALLET
import com.anit.fastpallet4.domain.intity.Type.INVENTORY_PALLET
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Status.NEW
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.interactor.InteractorCreatorMetaObj
import com.anit.fastpallet4.presentaition.presenter.Model.MAIN_MENU.*
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.ItemList
import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.doc.CreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.listdoc.ListDocFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.listdoc.ListDocView
import com.arellomobile.mvp.InjectViewState
import com.gladkikh.preference.PreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject
import android.support.v4.os.HandlerCompat.postDelayed
import com.anit.fastpallet4.domain.intity.listmetaobj.ItemListMetaObj
import com.anit.fastpallet4.domain.usecase.*
import com.anit.fastpallet4.presentaition.presenter.createpallet.pallet.ViewModel
import com.anit.fastpallet4.presentaition.ui.util.EventKeyClick
import com.anit.fastpallet4.presentaition.ui.util.KeyKode
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject


@InjectViewState
class ListDocPresenter(

    router: Router,
    val inputParamObj: ListDocFrScreen.InputParamObj?


) : BasePresenter<ListDocView>(router) {

    val model = Model()

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

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
            SETTINGS -> router.navigateTo(screens.getPreferencesScreen())
            LOAD -> model.loadDocs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showSnackbarViewMess("Ок")
                }, {
                    viewState.showSnackbarViewError(it.message ?: "")
                })
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

    enum class LIST_CONTEXT_MENU(val title: String, val id: Int) {
        DELL("Удалить", 1), SEND("Отправить", 2);

        companion object {
            fun getEnumById(id: Int): LIST_CONTEXT_MENU? {
                return when (id) {
                    1 -> return DELL
                    2 -> return SEND
                    else -> null
                }
            }
        }

    }

    fun getItemMenu(eventKeyClick: EventKeyClick): List<Pair<Int, String>>? {
        if (KeyKode.KEY_MENU == eventKeyClick.keyCode) {
            return listOf(
                Pair(LIST_CONTEXT_MENU.SEND.id, LIST_CONTEXT_MENU.SEND.title),
                Pair(LIST_CONTEXT_MENU.DELL.id, LIST_CONTEXT_MENU.DELL.title)

            )
        } else {
            return null
        }
    }

    fun onClicItemMenu(itemIdMenu: Int, eventKey: EventKeyClick): Boolean {
        var itemMenu = LIST_CONTEXT_MENU.getEnumById(itemIdMenu)
        when (itemMenu) {
            LIST_CONTEXT_MENU.DELL -> {
                viewState.showDialogConfirmDell(eventKey.id, model.getItemListMetaObj(eventKey.id)!!.description!!)
            }
            LIST_CONTEXT_MENU.SEND -> {
                model.sendDocToServer(eventKey.id)
                    .subscribe({
                        viewState.showSnackbarViewMess("Ok")
                    }, {
                        viewState.showSnackbarViewError(it.message.toString())
                    })
            }
        }

        return true

    }

    fun dellDoc(id: Int) {
        model.dellDoc(id)
    }

}


class Model {

    @Inject
    lateinit var interactorGetList: UseCaseGetListMetaObj

    @Inject
    lateinit var interactorUseCaseGetMetaObj: UseCaseGetMetaObj

    @Inject
    lateinit var interacLoadDocsFromServer: UseCaseGetListDocFromServer


    @Inject
    lateinit var interacSendCreatePalletToServer: UseCaseSendCreatePalletToServer

    private var listDoc: MutableList<ItemListMetaObj?> = mutableListOf()

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

    fun getItemListMetaObj(id: Int) = listDoc.get(id)

    fun getFlowableListItem() =
        interactorGetList.get()
            .doOnNext {
                listDoc = it.toMutableList()
            }
            .map { it ->
                it.map {
                    ItemList(
                        identifier = it!!.getGuid(),
                        info = "${it.description} ${it.getGuid() ?: ""}",
                        type = it.type.id

                    )
                }
            }

    fun loadDocs() = interacLoadDocsFromServer.load()

    fun dellDoc(id: Int) {
        var guid: String = getItemListMetaObj(id)!!.getGuid()!!
        var metaObj = interactorUseCaseGetMetaObj.get(guid)!!
        metaObj.dell()
    }

    fun sendDocToServer(id: Int): Completable {
        var guid: String = getItemListMetaObj(id)!!.getGuid()!!
        var metaObj = interactorUseCaseGetMetaObj.get(guid)!!
        return interacSendCreatePalletToServer.send(listOf(metaObj))
    }


}








