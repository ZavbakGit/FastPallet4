package com.anit.fastpallet4.app.di.modules


import android.content.Context
import com.anit.fastpallet4.data.repositories.dbrealm.DaoDb
import com.anit.fastpallet4.data.repositories.net.DaoNet
import com.anit.fastpallet4.data.repositories.net.ManagerNet
import com.anit.fastpallet4.data.repositories.preferense.DaoPref
import com.anit.netreqest.NetHelper
import com.anit.netreqest.NetSettings
import com.gladkikh.preference.IPreferenceHelper
import com.gladkikh.preference.PreferenceHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideDao(): DaoDb = DaoDb()


    @Singleton
    @Provides
    fun provideDaoNet(): DaoNet = DaoNet()


    @Singleton
    @Provides
    fun provideDaoPref(): DaoPref = DaoPref()

    @Provides
    @Singleton
    fun providePreferenceHelper(context: Context): PreferenceHelper = IPreferenceHelper(context)


    @Provides
    @Singleton
    fun provideManagerNet(): ManagerNet = ManagerNet()


    @Singleton
    @Provides
    fun provideNetHelper(netSettings: NetSettings, gson: Gson) = NetHelper(netSettings, gson)


    @Provides
    fun provideNetSettings(preferenceHelper: PreferenceHelper): NetSettings = object : NetSettings {

        override fun getHost() = preferenceHelper.getHost()
        override fun getLogin() = preferenceHelper.getLogin()
        override fun getPass() = preferenceHelper.getPass()
        override fun getCode() = preferenceHelper.getCode()
    }


}