package com.apps.test

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.usercentrics.sdk.UserDecision
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsServiceConsent
import com.usercentrics.sdk.firstLayerDescription
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_ui)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.custom_ui)

        // isReady is called after Usercentrics has finished initializing
        Usercentrics.isReady(onSuccess = {
            bindContent()
        }, onFailure = {
            // Handle error
            it.printStackTrace()
        })
    }

    private fun applyConsent(consents: List<UsercentricsServiceConsent>) {
        // https://docs.usercentrics.com/cmp_in_app_sdk/latest/apply_consent/apply-consent/#apply-consent-to-each-service
    }

    private fun printGDPR() {
        val data = Usercentrics.instance.getCMPData()

        val settings = data.settings
        println("Title: ${settings.labels.firstLayerTitle}")
        println("Description: ${settings.firstLayerDescription}")

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
        Usercentrics.instance.setCMPId(13)

        val data = Usercentrics.instance.getCMPData()
        val tcf2 = data.settings.tcf2 ?: return

        println("First layer title: ${tcf2.firstLayerTitle}")
        println("First layer description: ${tcf2.firstLayerDescription}")

        println("Second layer title: ${tcf2.secondLayerTitle}")
        println("Second layer description: ${tcf2.secondLayerDescription}")

        // TCF Data
        Usercentrics.instance.getTCFData { tcfData ->
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

            println("TCString ${tcfData.tcString}")
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
    }

    private fun acceptAllCallback() {
        val consents = when (activeVariant) {
            UsercentricsVariant.DEFAULT -> {
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.acceptAll(consentType)
            }
            UsercentricsVariant.CCPA -> {
                val doNotSellUsersInformation = false
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.saveOptOutForCCPA(doNotSellUsersInformation, consentType)
            }
            UsercentricsVariant.TCF -> {
                val layer = TCFDecisionUILayer.FIRST_LAYER
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.acceptAllForTCF(layer, consentType)
            }
        }

        applyConsent(consents)
    }

    private fun denyAllCallback() {
        val consents = when (activeVariant) {
            UsercentricsVariant.DEFAULT -> {
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.denyAll(consentType)
            }
            UsercentricsVariant.CCPA -> {
                val doNotSellUsersInformation = true
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.saveOptOutForCCPA(doNotSellUsersInformation, consentType)
            }
            UsercentricsVariant.TCF -> {
                val layer = TCFDecisionUILayer.FIRST_LAYER
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.denyAllForTCF(layer, consentType)
            }
        }

        applyConsent(consents)
    }

    private fun saveServicesCallback() {
        val consents = when (activeVariant) {
            UsercentricsVariant.DEFAULT -> {
                val decisions = decisionsListExample()
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.saveDecisions(decisions, consentType)
            }
            UsercentricsVariant.CCPA -> {
                println("NO ACTION FOR CCPA - This legal framework has no granular choices")
                Usercentrics.instance.getConsents()
            }
            UsercentricsVariant.TCF -> {
                val layer = TCFDecisionUILayer.FIRST_LAYER
                val tcfDecisions = TCFUserDecisions(
                    purposes = purposesExample(),
                    specialFeaturesExample(),
                    vendorsExample()
                )
                val decisions = decisionsListExample()
                val consentType = UsercentricsConsentType.EXPLICIT
                Usercentrics.instance.saveDecisionsForTCF(
                    tcfDecisions,
                    layer,
                    decisions,
                    consentType
                )
            }
        }

        applyConsent(consents)
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
        when (activeVariant) {
            UsercentricsVariant.DEFAULT -> printGDPR()
            UsercentricsVariant.CCPA -> printCCPA()
            UsercentricsVariant.TCF -> printTCF()
        }
    }

    private fun purposesExample(): List<TCFUserDecisionOnPurpose> {
        return listOf(
            TCFUserDecisionOnPurpose(
                id = 123,
                consent = false,
                legitimateInterestConsent = true
            )
        )
    }

    private fun specialFeaturesExample(): List<TCFUserDecisionOnSpecialFeature> {
        return listOf(TCFUserDecisionOnSpecialFeature(id = 222, consent = false))
    }

    private fun vendorsExample(): List<TCFUserDecisionOnVendor> {
        return listOf(
            TCFUserDecisionOnVendor(
                id = 111,
                consent = false,
                legitimateInterestConsent = true
            )
        )
    }

    private fun decisionsListExample(): List<UserDecision> {
        return listOf(UserDecision(serviceId = "1s4dc4", consent = false))
    }

    private fun bindContent() {
        val legalFrameworkStringResId = when (activeVariant) {
            UsercentricsVariant.DEFAULT -> R.string.gdpr
            UsercentricsVariant.CCPA -> R.string.ccpa
            UsercentricsVariant.TCF -> R.string.tcf_2_0
        }
        currentLegalFrameworkTextView.setText(legalFrameworkStringResId)

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

        printUiElementsButton.isEnabled = true
        acceptAllButton.isEnabled = true
        denyAllButton.isEnabled = true
        saveServicesButton.isEnabled = true
        changeLanguageButton.isEnabled = true
    }
}
