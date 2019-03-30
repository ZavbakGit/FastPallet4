package com.anit.fastpallet4.app.di.activity


import com.anit.fastpallet4.app.di.fragment.FragmentComponent
import com.anit.fastpallet4.app.di.fragment.FragmentModule
import dagger.Subcomponent

@Subcomponent(
    modules = arrayOf(
        ActivityModule::class
    )
)
@ActivityScope
interface ActivityComponent {
    fun createFragmentComponent(fragmentModule: FragmentModule): FragmentComponent
}


