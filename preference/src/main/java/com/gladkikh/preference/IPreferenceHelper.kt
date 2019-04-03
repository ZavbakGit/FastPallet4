package com.gladkikh.preference

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager


class IPreferenceHelper (val context: Context):PreferenceHelper {

    val sharedPref:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        val host  = "preference_host"
        val login = "preference_login"
        val pass  = "preference_pass"
        val code  = "preference_code_1c"
    }

    override public fun getIntentPreferenceActivity() = Intent(context, ProjectPreferenceActivity::class.java)

    override public fun getHost()  = sharedPref.getString(host, null)
    override public fun getLogin() = sharedPref.getString(login, null)
    override public fun getPass()  = sharedPref.getString(pass, null)
    override public fun getCode()  = sharedPref.getString(code, null)


}