package com.anit.fastpallet4.presentaition.navigation

import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.doc.CreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.product.ProductCreatePalletFrScreen
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

    fun getCreatePalletFrScreen(inputparam: CreatePalletFrScreen.InputParamObj?): SupportAppScreen {
        return object : SupportAppScreen() {
            override fun getFragment() = CreatePalletFrScreen.newInstance(inputparam)
        }
    }

    fun getProductCreatePalletFrScreen(inputparam: ProductCreatePalletFrScreen.InputParamObj?): SupportAppScreen {
        return object : SupportAppScreen() {
            override fun getFragment() = ProductCreatePalletFrScreen.newInstance(inputparam)
        }
    }

    fun getPalletCreatePalletFrScreen(inputparam: PalletCreatePalletFrScreen.InputParamObj?): SupportAppScreen {
        return object : SupportAppScreen() {
            override fun getFragment() = PalletCreatePalletFrScreen.newInstance(inputparam)
        }
    }




}