package com.anit.fastpallet4.app.di.modules


import com.anit.fastpallet4.data.repositories.RealmInitLocal
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class RealmModule{

    @Singleton
    @Provides
    fun provideRealmInitLocal(): RealmInitLocal = RealmInitLocal()

    //Специально не Singleton пусть запрашивает каждый раз
//    @Singleton
//    @Provides
//    fun provideRealm(realmInitLocal:RealmInitLocal): Realm = realmInitLocal.getLocalInstance()


}