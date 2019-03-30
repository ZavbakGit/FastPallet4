package com.anit.fastpallet4.app.di.aplication


import android.content.Context
import com.anit.fastpallet4.app.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class AppModule(private val app: App) {
  @Provides
  @Singleton
  fun provideContext(): Context = app
}


