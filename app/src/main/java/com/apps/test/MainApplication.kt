package com.apps.test

import android.app.Application
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsOptions
import com.usercentrics.sdk.models.common.UsercentricsLoggerLevel

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Usercentrics with your configuration
        val options = UsercentricsOptions(
            settingsId = "QsYfVcScdFgaVf",
            loggerLevel = UsercentricsLoggerLevel.DEBUG,
            consentMediation = true
        )

        Usercentrics.initialize(this, options)
    }

}
