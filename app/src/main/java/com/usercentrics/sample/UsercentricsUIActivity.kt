package com.usercentrics.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.usercentrics.sdk.*

class UsercentricsUIActivity : AppCompatActivity() {

    private val openCmpButton by lazy { findViewById<View>(R.id.open_cmp_button) }
    private val customUiButton by lazy { findViewById<View>(R.id.custom_ui_button) }
    private val usercentricsActivityLauncher = registerForActivityResult(UsercentricsActivityContract(), ::usercentricsActivityCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // isReady is called after Usercentrics has finished initializing
        // get the consent status of the user, via UsercentricsReadyStatus
        Usercentrics.isReady(onSuccess = { status ->
            if (status.shouldShowCMP) {
                showCMP(showCloseButton = false)
            } else {
                applyConsent(status.consents)
            }

            bindContent()
        }, onFailure = {
            // Handle error
            it.printStackTrace()
        })
    }

    private fun applyConsent(consents: List<UsercentricsServiceConsent>) {
        // https://docs.usercentrics.com/cmp_in_app_sdk/latest/apply_consent/apply-consent/#apply-consent-to-each-service
    }

    private fun showCMP(showCloseButton: Boolean) {
        // launch Usercentrics Activity with your custom settings
        usercentricsActivityLauncher.launch(UsercentricsUISettings(showCloseButton = showCloseButton))
    }

    private fun showCustomUiActivity() {
        startActivity(Intent(this, BuildYourOwnUIActivity::class.java))
    }

    private fun usercentricsActivityCallback(consentUserResponse: UsercentricsConsentUserResponse?) {
        if (consentUserResponse == null) {
            return
        }
        println("Consents -> ${consentUserResponse.consents}")
        println("User Interaction -> ${consentUserResponse.userInteraction}")
        println("Controller Id -> ${consentUserResponse.controllerId}")
    }

    private fun bindContent() {
        openCmpButton.setOnClickListener {
            // This is useful when you need to call our CMP from settings screen for instance, therefore the user may dismiss the view
            showCMP(showCloseButton = true)
        }
        customUiButton.setOnClickListener {
            showCustomUiActivity()
        }

        openCmpButton.isEnabled = true
        customUiButton.isEnabled = true
    }
}
