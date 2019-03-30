package com.anit.fastpallet4.app.di.fragment

import dagger.Subcomponent

@Subcomponent(
    modules = arrayOf(
        FragmentModule::class
    )
)
@FragmentScope
interface FragmentComponent {

}


