package com.usercentrics.sample

import android.app.Application
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsOptions

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Usercentrics with your configuration
        val settingsId = "Yi9N3aXia"
        val options = UsercentricsOptions(settingsId)

        Usercentrics.initialize(this, options)
    }
}
