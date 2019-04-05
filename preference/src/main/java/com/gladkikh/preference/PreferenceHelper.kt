package com.gladkikh.preference

import android.content.Intent

interface PreferenceHelper {
    fun getIntentPreferenceActivity():Intent
    fun getHost():String
    fun getLogin():String?
    fun getPass():String?
    fun getCode(): String?
    fun getTypeTsd():String?
}