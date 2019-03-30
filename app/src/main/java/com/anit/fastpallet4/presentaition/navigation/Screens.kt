package com.anit.fastpallet4.presentaition.navigation

import com.anit.fastpallet4.presentaition.ui.screens.listdoc.ListDocFrScreen
import ru.terrakok.cicerone.android.support.SupportAppScreen


class Screens {
    fun getListDocScreen(inputparam: ListDocFrScreen.InputParamObj?): SupportAppScreen {
        return object : SupportAppScreen() {
            override fun getFragment() = ListDocFrScreen.newInstance(inputparam)
        }
    }

}