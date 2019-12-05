package com.anit.fastpallet4.app.di.modules


import com.anit.fastpallet4.data.repositories.dbrealm.RealmInitLocal
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RealmModule{

    @Singleton
    @Provides
    fun provideRealmInitLocal(): RealmInitLocal =
        RealmInitLocal()
}