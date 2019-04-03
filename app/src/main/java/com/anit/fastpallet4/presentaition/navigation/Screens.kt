package com.anit.fastpallet4.presentaition.navigation

import android.content.Context
import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.presentaition.ui.screens.inventory.InventoryFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.doc.CreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.pallet.PalletCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.creatpallet.product.ProductCreatePalletFrScreen
import com.anit.fastpallet4.presentaition.ui.screens.listdoc.ListDocFrScreen
import com.gladkikh.preference.PreferenceHelper
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject


class Screens {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    init {
        App.appComponent.inject(this)
    }

    fun getPreferencesScreen(): SupportAppScreen {
        return object : SupportAppScreen() {
            override fun getActivityIntent(context: Context?) =
                preferenceHelper.getIntentPreferenceActivity()
        }
    }

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