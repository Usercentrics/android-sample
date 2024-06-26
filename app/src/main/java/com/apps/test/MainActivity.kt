package com.apps.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.usercentrics.sdk.*

class MainActivity : AppCompatActivity() {

    private val showFirstLayerButton by lazy { findViewById<View>(R.id.show_first_layer) }
    private val showSecondLayerButton by lazy { findViewById<View>(R.id.show_second_layer) }
    private val showCustomizationExample1Button by lazy { findViewById<View>(R.id.show_customization_example_1) }
    private val showCustomizationExample2Button by lazy { findViewById<View>(R.id.show_customization_example_2) }
    private val customUIButton by lazy { findViewById<View>(R.id.custom_ui) }
    private var banner: UsercentricsBanner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        // 'isReady' is called after Usercentrics has finished initializing
        // get the consent status of the user, via UsercentricsReadyStatus
        Usercentrics.isReady(onSuccess = { status ->
            if (status.shouldCollectConsent) {
                showFirstLayer()
            } else {
                applyConsent(status.consents)
            }

            bindContent()
        }, onFailure = {
            // Handle error
            it.printStackTrace()
        })
    }

    override fun onDestroy() {
        banner?.dismiss()
        banner = null
        super.onDestroy()
    }

    private fun applyConsent(consents: List<UsercentricsServiceConsent>) {
        // https://docs.usercentrics.com/cmp_in_app_sdk/latest/apply_consent/apply-consent/#apply-consent-to-each-service
    }

    private fun showFirstLayer(
        layout: UsercentricsLayout = UsercentricsLayout.Popup(PopupPosition.BOTTOM),
        settings: BannerSettings? = null
    ) {
        // Launch Usercentrics Banner with your settings
        banner = UsercentricsBanner(this, settings).also {
            it.showFirstLayer(
                callback = ::handleUserResponse
            )
        }
    }

    private fun showSecondLayer() {
        // This is useful when you need to call our CMP from settings screen for instance, therefore the user may dismiss the view
        val settings = BannerSettings(
            secondLayerStyleSettings = SecondLayerStyleSettings(
                showCloseButton = true,
            )
        )
        banner = UsercentricsBanner(this, settings).also {
            it.showSecondLayer(
                callback = ::handleUserResponse
            )
        }
    }

    private fun handleUserResponse(userResponse: UsercentricsConsentUserResponse?) {
        userResponse ?: return

        println("Consents -> ${userResponse.consents}")
        println("User Interaction -> ${userResponse.userInteraction}")
        println("Controller Id -> ${userResponse.controllerId}")

        applyConsent(userResponse.consents)
    }

    private fun bindContent() {
        showFirstLayerButton.setOnClickListener {
            showFirstLayer()
        }
        showSecondLayerButton.setOnClickListener {
            showSecondLayer()
        }
        showCustomizationExample1Button.setOnClickListener {
            showFirstLayer(
                layout = UsercentricsLayout.Popup(PopupPosition.BOTTOM),
                settings = customizationExample1(this),
            )
        }
        showCustomizationExample2Button.setOnClickListener {
            showFirstLayer(
                layout = UsercentricsLayout.Full,
                settings = customizationExample2(this),
            )
        }
        customUIButton.setOnClickListener {
            startActivity(Intent(this, BuildYourOwnUIActivity::class.java))
        }

        showFirstLayerButton.isEnabled = true
        showSecondLayerButton.isEnabled = true
        showCustomizationExample1Button.isEnabled = true
        showCustomizationExample2Button.isEnabled = true
        customUIButton.isEnabled = true
    }
}
