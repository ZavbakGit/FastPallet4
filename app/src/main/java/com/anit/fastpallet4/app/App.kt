package com.anit.fastpallet4.app

import android.app.Application
import android.support.v7.app.AppCompatActivity
import com.anit.fastpallet4.app.di.activity.ActivityComponent
import com.anit.fastpallet4.app.di.activity.ActivityModule
import com.anit.fastpallet4.app.di.aplication.AppComponent
import com.anit.fastpallet4.app.di.aplication.AppModule
import com.anit.fastpallet4.app.di.aplication.DaggerAppComponent
import io.realm.Realm


open class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent

        fun getActivityComponent(activity: AppCompatActivity): ActivityComponent =
            appComponent.createActivityComponent(ActivityModule(activity))
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        initializeDagger()

    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(app = this@App))
            .build()
    }

}

