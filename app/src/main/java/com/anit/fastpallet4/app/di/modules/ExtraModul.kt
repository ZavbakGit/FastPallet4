package com.anit.fastpallet4.app.di.modules

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExtraModul {

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
//            .addSerializationExclusionStrategy(object : ExclusionStrategy {
//                override fun shouldSkipField(f: FieldAttributes): Boolean {
//                    return f.name.toLowerCase().contains("useCaseMetaObj")
//                }
//
//                override fun shouldSkipClass(aClass: Class<*>): Boolean {
//                    return false
//                }
//            })
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()



}