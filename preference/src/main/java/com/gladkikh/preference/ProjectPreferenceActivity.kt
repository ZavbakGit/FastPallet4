package com.gladkikh.preference

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ProjectPreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content,SettingsFragment())
            .commit()
    }
}
