package com.anit.fastpallet4.app.di.modules


import com.anit.fastpallet4.data.repositories.Dao
import com.anit.fastpallet4.data.repositories.RealmInitLocal
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class RepositoryModule{


    @Singleton
    @Provides
    fun provideDao(): Dao = Dao()


}