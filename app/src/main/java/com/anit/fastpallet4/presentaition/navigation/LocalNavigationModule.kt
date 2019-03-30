package com.anit.fastpallet4.presentaition.navigation



import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class LocalNavigationModule {

    @Provides
    @Singleton
    fun provideLocalNavigationHolder(): LocalCiceroneHolder {
        return LocalCiceroneHolder()
    }
}
