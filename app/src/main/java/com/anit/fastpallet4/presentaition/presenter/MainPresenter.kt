package com.anit.fastpallet4.presentaition.presenter

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router

@InjectViewState
class MainPresenter(router: Router) : BasePresenter<BaseView>(router) {

    init {
        App.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(screens.getListDocScreen(null))
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }



}