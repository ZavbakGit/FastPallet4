package com.gladkikh.preference

import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast


class SettingsFragment: PreferenceFragmentCompat(),Preference.OnPreferenceChangeListener {
    override fun onPreferenceChange(p0: Preference?, p1: Any?): Boolean {
        Toast.makeText(
            activity,
            "Будь внимателен!",
            Toast.LENGTH_SHORT
        ).show()
        return true
    }


    override fun onCreatePreferences(saveInstantState: Bundle?, rootkey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootkey)
        findPreference("preference_host").setOnPreferenceChangeListener (this)
    }

}