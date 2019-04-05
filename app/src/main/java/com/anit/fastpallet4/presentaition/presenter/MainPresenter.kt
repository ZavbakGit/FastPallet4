package com.anit.fastpallet4.presentaition.presenter

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.presentaition.ui.base.BasePresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseView
import com.arellomobile.mvp.InjectViewState
import com.gladkikh.preference.PreferenceHelper
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class MainPresenter(router: Router) : BasePresenter<BaseView>(router) {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

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