package com.anit.fastpallet4.presentaition.presenter

import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.anit.fastpallet4.presentaition.ui.screens.listdoc.ListDocFrScreen
import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router

@InjectViewState
class ListDocPresenter(router: Router, val inputParamObj: ListDocFrScreen.InputParamObj?) :
    BasePresenter<BaseView>(router) {
    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }
}



