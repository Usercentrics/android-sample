package com.apps.test

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.usercentrics.sdk.*
import com.usercentrics.sdk.models.common.UsercentricsLoggerLevel

class MainActivity : AppCompatActivity() {

    private val showFirstLayerGDPRButton by lazy { findViewById<View>(R.id.show_first_layer_gdpr) }
    private val showSecondLayerGDPRButton by lazy { findViewById<View>(R.id.show_second_layer_gdpr) }
    private val showFirstLayerTCFButton by lazy { findViewById<View>(R.id.show_first_layer_tcf) }
    private val showSecondLayerTCFButton by lazy { findViewById<View>(R.id.show_second_layer_tcf) }
    private val showFirstLayerCCPAButton by lazy { findViewById<View>(R.id.show_first_layer_ccpa) }
    private val showSecondLayerCCPAButton by lazy { findViewById<View>(R.id.show_second_layer_ccpa) }
    private var banner: UsercentricsBanner? = null

    private val gdprSettingsId = "lQ_Dio7QL"
    private val tcfSettingsId = "EA4jnNPb9"
    private val ccpaSettingsId = "NfThHXzzZNc-RE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize(gdprSettingsId)
    }

    private fun initialize(settingsId: String) {
        val options = UsercentricsOptions(
            settingsId = settingsId,
            loggerLevel = UsercentricsLoggerLevel.DEBUG,
        )

        Usercentrics.initialize(this, options)

        Usercentrics.isReady({bindContent()}, {})
    }

    private fun clearAndInitAgain(settingsId: String) {
        Usercentrics.isReady({
            Usercentrics.instance.clearUserSession({}, {})

            initialize(settingsId = settingsId)
        }, {

        })
    }

    override fun onResume() {
        super.onResume()
        bindContent()
    }

    override fun onPause() {
        showFirstLayerGDPRButton.setOnClickListener(null)
        showFirstLayerGDPRButton.isEnabled = true
        showSecondLayerGDPRButton.setOnClickListener(null)
        showSecondLayerGDPRButton.isEnabled = true

        showFirstLayerTCFButton.setOnClickListener(null)
        showFirstLayerTCFButton.isEnabled = true
        showSecondLayerTCFButton.setOnClickListener(null)
        showSecondLayerTCFButton.isEnabled = true

        showFirstLayerCCPAButton.setOnClickListener(null)
        showFirstLayerCCPAButton.isEnabled = false
        showSecondLayerCCPAButton.setOnClickListener(null)
        showSecondLayerCCPAButton.isEnabled = false

        super.onPause()
    }

    override fun onDestroy() {
        banner?.dismiss()
        banner = null
        super.onDestroy()
    }

    private fun applyConsent(consents: List<UsercentricsServiceConsent>) {
        // https://docs.usercentrics.com/cmp_in_app_sdk/latest/apply_consent/apply-consent/#apply-consent-to-each-service
    }

    private fun showFirstLayer() {
        val firstLayerStyleSettings = FirstLayerStyleSettings(UsercentricsLayout.Full)

        Usercentrics.isReady({
            banner = UsercentricsBanner(
                this,
                BannerSettings(firstLayerStyleSettings = firstLayerStyleSettings)
            )
            banner?.showFirstLayer { }
        }, {

        })
    }

    private fun showSecondLayer() {
        val settings = BannerSettings(
            secondLayerStyleSettings = SecondLayerStyleSettings(
                showCloseButton = true,
            )
        )

        Usercentrics.isReady({
            banner = UsercentricsBanner(this, settings).also {
                it.showSecondLayer(
                    callback = ::handleUserResponse
                )
            }
        }, {
        })
    }

    private fun handleUserResponse(userResponse: UsercentricsConsentUserResponse?) {
        userResponse ?: return

        println("Consents -> ${userResponse.consents}")
        println("User Interaction -> ${userResponse.userInteraction}")
        println("Controller Id -> ${userResponse.controllerId}")

        applyConsent(userResponse.consents)
    }

    private fun bindContent() {
        showFirstLayerGDPRButton.setOnClickListener {
            clearAndInitAgain(gdprSettingsId)
            showFirstLayer()
        }
        showFirstLayerTCFButton.setOnClickListener {
            clearAndInitAgain(tcfSettingsId)
            showFirstLayer()
        }
        showFirstLayerCCPAButton.setOnClickListener {
            clearAndInitAgain(ccpaSettingsId)
            showFirstLayer()
        }
        showSecondLayerGDPRButton.setOnClickListener {
            clearAndInitAgain(gdprSettingsId)
            showSecondLayer()
        }
        showSecondLayerTCFButton.setOnClickListener {
            clearAndInitAgain(tcfSettingsId)
            showSecondLayer()
        }
        showSecondLayerCCPAButton.setOnClickListener {
            clearAndInitAgain(ccpaSettingsId)
            showSecondLayer()
        }

        showFirstLayerGDPRButton.isEnabled = true
        showFirstLayerTCFButton.isEnabled = true
        showFirstLayerCCPAButton.isEnabled = true
        showSecondLayerGDPRButton.isEnabled = true
        showSecondLayerTCFButton.isEnabled = true
        showSecondLayerCCPAButton.isEnabled = true
    }
}
