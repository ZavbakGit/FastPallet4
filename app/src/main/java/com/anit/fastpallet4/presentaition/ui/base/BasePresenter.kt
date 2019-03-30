package com.anit.fastpallet4.presentaition.ui.base


import com.anit.fastpallet4.presentaition.navigation.BackButtonListener
import com.anit.fastpallet4.presentaition.navigation.Screens
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import ru.terrakok.cicerone.Router
import javax.inject.Inject

abstract class BasePresenter<V: MvpView>(val router: Router):MvpPresenter<V>()
    , BackButtonListener {

    @Inject
    lateinit var screens: Screens

}