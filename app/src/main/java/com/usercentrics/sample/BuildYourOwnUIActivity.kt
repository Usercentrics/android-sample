package com.usercentrics.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.usercentrics.sdk.UserDecision
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.models.common.UsercentricsVariant
import com.usercentrics.sdk.models.settings.UsercentricsConsentType
import com.usercentrics.sdk.services.tcf.TCFDecisionUILayer
import com.usercentrics.sdk.services.tcf.interfaces.TCFUserDecisionOnPurpose
import com.usercentrics.sdk.services.tcf.interfaces.TCFUserDecisionOnSpecialFeature
import com.usercentrics.sdk.services.tcf.interfaces.TCFUserDecisionOnVendor
import com.usercentrics.sdk.services.tcf.interfaces.TCFUserDecisions

class BuildYourOwnUIActivity : AppCompatActivity() {

    private val currentLegalFrameworkTextView by lazy { findViewById<TextView>(R.id.current_legal_framework_text) }
    private val printUiElementsButton by lazy { findViewById<View>(R.id.print_ui_elements_button) }
    private val acceptAllButton by lazy { findViewById<View>(R.id.accept_all_button) }
    private val denyAllButton by lazy { findViewById<View>(R.id.deny_all_button) }
    private val saveServicesButton by lazy { findViewById<View>(R.id.save_services_button) }
    private val changeLanguageButton by lazy { findViewById<View>(R.id.change_language_button) }

    private val activeVariant by lazy { Usercentrics.instance.getCMPData().activeVariant }
    private val isTCF by lazy { activeVariant == UsercentricsVariant.TCF }
    private val isCCPA by lazy { activeVariant == UsercentricsVariant.CCPA }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_ui)

        toggleButtonsState(isEnabled = false)

        setupUsercentrics()
        setupButtons()
        showCurrentLegalFramework()
    }

    private fun setupUsercentrics() {
        Usercentrics.isReady(onSuccess = {
            toggleButtonsState(isEnabled = true)
        }, onFailure = {
            it.printStackTrace()
        })
    }

    private fun applyConsent() {
        Usercentrics.instance.getConsents()
        // https://docs.usercentrics.com/cmp_in_app_sdk/latest/apply_consent/apply-consent/#apply-consent-to-each-service
    }

    private fun printGDPR() {
        val data = Usercentrics.instance.getCMPData()

        val settings = data.settings
        println("Title: ${settings.labels.firstLayerTitle}")
        println("Description: ${settings.bannerMessage}")

        println("data.settings: $settings")
        println("data.services: ${data.services}")
        println("data.categories: ${data.categories}")

        println("Accept All button: ${settings.labels.btnAcceptAll}")
        println("Deny button: ${settings.labels.btnDeny}")
        println("Save button: ${settings.labels.btnSave}")
    }

    private fun printCCPA() {
        val data = Usercentrics.instance.getCMPData()
        val ccpa = data.settings.ccpa ?: return

        println("Title: ${ccpa.firstLayerTitle}")
        println("Description: ${ccpa.appFirstLayerDescription}")

        for (category in data.categories) {
            println("Category name: ${category.label}")
        }

        for (service in data.services) {
            println("Data processor: ${service.dataProcessor} with Template Id: ${service.templateId}")
        }

        println("Do not sell my info: ${ccpa.optOutNoticeLabel}")
        println("Save: ${ccpa.btnSave}")
    }

    private fun printTCF() {
        println("Set your CMP ID")
        Usercentrics.instance.setCMPId(0)

        val data = Usercentrics.instance.getCMPData()
        val tcf2 = data.settings.tcf2 ?: return

        println("First layer title: ${tcf2.firstLayerTitle}")
        println("First layer description: ${tcf2.firstLayerDescription}")

        println("Second layer title: ${tcf2.secondLayerTitle}")
        println("Second layer description: ${tcf2.secondLayerDescription}")

        // TCF Data
        val tcfData = Usercentrics.instance.getTCFData()
        for (purpose in tcfData.purposes) {
            println("Purpose: ${purpose.name}")
        }

        for (specialPurpose in tcfData.specialPurposes) {
            println("Special Purpose: ${specialPurpose.name}")
        }

        for (feature in tcfData.features) {
            println("Feature: ${feature.name}")
        }

        for (specialFeature in tcfData.specialFeatures) {
            println("Special Feature: ${specialFeature.name}")
        }

        for (stack in tcfData.stacks) {
            println("Stack: ${stack.name}")
        }

        for (vendor in tcfData.vendors) {
            println("Vendor: ${vendor.name}")
        }

        // Non-IAB data
        for (category in data.categories) {
            println("Category name: ${category.label}")
        }

        for (service in data.services) {
            println("Data processor: ${service.dataProcessor} with Template Id: ${service.templateId}")
        }

        println("Accept All button: ${tcf2.buttonsAcceptAllLabel}")
        println("Deny button: ${tcf2.buttonsDenyAllLabel}")
        println("Save button: ${tcf2.buttonsSaveLabel}")

        println("TCString ${Usercentrics.instance.getTCString()}")
    }

    private fun acceptAllCallback() {
        when {
            isTCF -> {
                val layer = TCFDecisionUILayer.FIRST_LAYER
                Usercentrics.instance.acceptAllForTCF(layer)
            }
            isCCPA -> {
                val doNotSellUsersInformation = false
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.saveOptOutForCCPA(doNotSellUsersInformation, consentType)
            }
            else -> {
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.acceptAllServices(consentType)
            }
        }
        applyConsent()
    }

    private fun denyAllCallback() {
        when {
            isTCF -> {
                val layer = TCFDecisionUILayer.FIRST_LAYER
                Usercentrics.instance.denyAllForTCF(layer)
            }
            isCCPA -> {
                val doNotSellUsersInformation = true
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.saveOptOutForCCPA(doNotSellUsersInformation, consentType)
            }
            else -> {
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.denyAllServices(consentType)
            }
        }

        applyConsent()
    }

    @Suppress("ControlFlowWithEmptyBody")
    private fun saveServicesCallback() {
        when {
            isTCF -> {
                val layer = TCFDecisionUILayer.FIRST_LAYER
                val decisions = TCFUserDecisions(purposes = purposesExample(), specialFeaturesExample(), vendorsExample())
                Usercentrics.instance.updateChoicesForTCF(decisions, layer)
            }
            isCCPA -> {
                println("NO ACTION FOR CCPA")
            }
            else -> {
                val layer = TCFDecisionUILayer.FIRST_LAYER
                val decisions = TCFUserDecisions(purposes = purposesExample(), specialFeaturesExample(), vendorsExample())
                Usercentrics.instance.updateChoicesForTCF(decisions, layer)
            }
        }

        applyConsent()
    }

    private fun changeLanguageCallback() {
        val languageCode = "de"
        Usercentrics.instance.changeLanguage(languageCode, onSuccess = {
            printUiElementsCallback()
            Toast.makeText(this, "Language changed successfully!", Toast.LENGTH_SHORT).show()
        }, onFailure = {
            it.printStackTrace()
        })
    }

    private fun printUiElementsCallback() {
        when {
            isCCPA -> printCCPA()
            isTCF -> printTCF()
            else -> printGDPR()
        }
    }

    private fun purposesExample(): List<TCFUserDecisionOnPurpose> {
        return listOf(TCFUserDecisionOnPurpose(id = 123, consent = false, legitimateInterestConsent = true))
    }

    private fun specialFeaturesExample(): List<TCFUserDecisionOnSpecialFeature> {
        return listOf(TCFUserDecisionOnSpecialFeature(id = 222, consent = false))
    }

    private fun vendorsExample(): List<TCFUserDecisionOnVendor> {
        return listOf(TCFUserDecisionOnVendor(id = 111, consent = false, legitimateInterestConsent = true))
    }

    private fun decisionsListExample(): List<UserDecision> {
        return listOf(UserDecision(serviceId = "1s4dc4", consent = false))
    }

    private fun showCurrentLegalFramework() {
        val legalFrameworkStringResId = when {
            isTCF -> R.string.tcf_2_0
            isCCPA -> R.string.ccpa
            else -> R.string.gdpr
        }
        currentLegalFrameworkTextView.setText(legalFrameworkStringResId)
    }

    private fun setupButtons() {
        printUiElementsButton.setOnClickListener {
            printUiElementsCallback()
        }
        acceptAllButton.setOnClickListener {
            acceptAllCallback()
        }
        denyAllButton.setOnClickListener {
            denyAllCallback()
        }
        saveServicesButton.setOnClickListener {
            saveServicesCallback()
        }
        changeLanguageButton.setOnClickListener {
            changeLanguageCallback()
        }
    }

    private fun toggleButtonsState(isEnabled: Boolean) {
        acceptAllButton.isEnabled = isEnabled
        denyAllButton.isEnabled = isEnabled
        saveServicesButton.isEnabled = isEnabled
        changeLanguageButton.isEnabled = isEnabled
    }
}
