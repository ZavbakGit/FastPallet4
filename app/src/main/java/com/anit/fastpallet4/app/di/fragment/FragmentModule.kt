package com.anit.fastpallet4.app.di.fragment


import android.support.v4.app.Fragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {
    @Provides
    @FragmentScope
    fun provideFragmentContext() = fragment
}


