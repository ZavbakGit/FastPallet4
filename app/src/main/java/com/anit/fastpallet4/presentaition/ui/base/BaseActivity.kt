package com.anit.fastpallet4.presentaition.ui.base

import android.os.Bundle
import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.db.RealmInitLocal
import com.anit.fastpallet4.presentaition.navigation.BackButtonListener
import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.arellomobile.mvp.MvpAppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Inject

abstract class BaseActivity: MvpAppCompatActivity(), RouterProvider {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var routerMain: Router

    @Inject
    lateinit var realmInitLocal: RealmInitLocal

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)

        realmInitLocal.openLocalInstance()

        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(getNavigator())
    }
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(getIdConteiner())
        if (fragment != null && fragment is BackButtonListener && fragment.onBackPressed()) {
            return
        } else {
            pressBackTo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realmInitLocal.closeLocalInstance()
    }



    override fun getRouter() = routerMain
    abstract fun pressBackTo():Boolean
    abstract fun getIdConteiner(): Int
    abstract fun getNavigator(): Navigator
    abstract fun getLayout(): Int

}