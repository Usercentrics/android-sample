package com.apps.test

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.usercentrics.sdk.BannerFont
import com.usercentrics.sdk.BannerSettings
import com.usercentrics.sdk.ButtonLayout
import com.usercentrics.sdk.ButtonSettings
import com.usercentrics.sdk.ButtonType
import com.usercentrics.sdk.FirstLayerStyleSettings
import com.usercentrics.sdk.GeneralStyleSettings
import com.usercentrics.sdk.HeaderImageSettings
import com.usercentrics.sdk.LegalLinksSettings
import com.usercentrics.sdk.MessageSettings
import com.usercentrics.sdk.SecondLayerStyleSettings
import com.usercentrics.sdk.SectionAlignment
import com.usercentrics.sdk.TitleSettings
import com.usercentrics.sdk.UsercentricsImage

private val firstLayerLogoImage = UsercentricsImage.ImageDrawableId(R.drawable.header_logo)
private const val buttonTextSizeInSp = 14f
private const val buttonCornerRadius = 50

fun customizationExample2(context: Context): BannerSettings {
    return BannerSettings(
        generalStyleSettings = GeneralStyleSettings(
            font = BannerFont(
                regularFont = Typeface.DEFAULT,
                boldFont = Typeface.DEFAULT_BOLD,
                sizeInSp = 14f,
            ),
            links = LegalLinksSettings.SECOND_LAYER_ONLY,
            textColor = Color.LTGRAY,
            bordersColor = ContextCompat.getColor(context, R.color.grey),
            layerBackgroundColor = ContextCompat.getColor(context, R.color.dark),
            layerBackgroundSecondaryColor = ContextCompat.getColor(context, R.color.grey),
            linkColor = Color.WHITE,
            tabColor = Color.WHITE,
        ),
        firstLayerStyleSettings = FirstLayerStyleSettings(
            backgroundColor = ContextCompat.getColor(context, R.color.dark),
            headerImage = HeaderImageSettings.LogoSettings(firstLayerLogoImage),
            title = TitleSettings(
                alignment = SectionAlignment.START,
                textColor = Color.WHITE,
                textSizeInSp = 18f,
            ),
            message = MessageSettings(
                alignment = SectionAlignment.START,
                textSizeInSp = 14f,
                textColor = Color.LTGRAY,
                linkTextColor = Color.WHITE,
                underlineLink = true,
            ),
            buttonLayout = ButtonLayout.Column(
                listOf(
                    ButtonSettings(
                        type = ButtonType.MORE,
                        textColor = Color.WHITE,
                        backgroundColor = Color.TRANSPARENT,
                        textSizeInSp = buttonTextSizeInSp,
                        cornerRadius = buttonCornerRadius,
                    ),
                    ButtonSettings(
                        type = ButtonType.ACCEPT_ALL,
                        textColor = Color.BLACK,
                        backgroundColor = Color.WHITE,
                        textSizeInSp = buttonTextSizeInSp,
                        cornerRadius = buttonCornerRadius,
                    ),
                ),
            )
        ),
        secondLayerStyleSettings = SecondLayerStyleSettings(
            buttonLayout = ButtonLayout.Column(
                listOf(
                    ButtonSettings(
                        type = ButtonType.SAVE,
                        cornerRadius = buttonCornerRadius,
                    ),
                )
            )
        )
    )
}
