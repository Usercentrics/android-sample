package com.apps.test

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.usercentrics.sdk.*

private val firstLayerLogoImage = UsercentricsImage.ImageDrawableId(R.drawable.header_logo)
private const val buttonTextSizeInSp = 14f
private const val buttonCornerRadius = 50

fun customizationExample2(context: Context): BannerSettings {
    return BannerSettings(
        font = BannerFont(
            regularFont = Typeface.DEFAULT,
            boldFont = Typeface.DEFAULT_BOLD,
            sizeInSp = 14f,
        ),
        links = LegalLinksSettings.SECOND_LAYER_ONLY,
        firstLayerSettings = FirstLayerStyleSettings(
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
        secondLayerSettings = SecondLayerStyleSettings(
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
