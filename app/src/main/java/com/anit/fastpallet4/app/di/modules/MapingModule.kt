package com.anit.fastpallet4.app.di.modules



import com.anit.fastpallet4.maping.Maping
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapingModule{

    @Singleton
    @Provides
    fun provideMaping(): Maping =
        Maping()


}