package com.anit.fastpallet4.presentaition.navigation

import com.anit.fastpallet4.presentaition.ui.screens.Inventory.InventoryFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.listdoc.ListDocFrScreen
import ru.terrakok.cicerone.android.support.SupportAppScreen


class Screens {
    fun getListDocScreen(inputparam: ListDocFrScreen.InputParamObj?): SupportAppScreen {
        return object : SupportAppScreen() {
            override fun getFragment() = ListDocFrScreen.newInstance(inputparam)
        }
    }

    fun getInventoryScreen(inputparam: InventoryFrScreen.InputParamObj?): SupportAppScreen {
        return object : SupportAppScreen() {
            override fun getFragment() = InventoryFrScreen.newInstance(inputparam)
        }
    }

}