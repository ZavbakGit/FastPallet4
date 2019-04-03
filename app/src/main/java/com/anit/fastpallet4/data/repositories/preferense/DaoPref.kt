package com.anit.fastpallet4.data.repositories.preferense

import com.anit.fastpallet4.app.App
import com.gladkikh.preference.PreferenceHelper
import javax.inject.Inject

class DaoPref {
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    init {
        App.appComponent.inject(this)
    }

    fun getCodeTsd() = preferenceHelper.getCode()
    fun getHost() = preferenceHelper.getHost()
    fun getLogin() = preferenceHelper.getLogin()
    fun getPass() = preferenceHelper.getPass()

}