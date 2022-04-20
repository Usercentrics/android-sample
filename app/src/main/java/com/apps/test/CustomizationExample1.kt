package com.apps.test

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.graphics.TypefaceCompat
import com.usercentrics.sdk.*

private val logoImage = UsercentricsImage.ImageDrawableId(R.drawable.logo)
private val firstLayerImage = UsercentricsImage.ImageDrawableId(R.drawable.header_extended)
private const val buttonTextSizeInSp = 16f
private const val buttonCornerRadius = 20

private fun getCustomFont(context: Context): Typeface {
    val titleFontPath = "fonts/AndadaPro.ttf"
    val base = Typeface.createFromAsset(context.assets, titleFontPath)
    return TypefaceCompat.create(context, base, Typeface.BOLD)
}

fun customizationExample1(context: Context): BannerSettings {
    return BannerSettings(
        font = BannerFont(
            context = context,
            fontFamily = getCustomFont(context),
            sizeInSp = 14f,
        ),
        logo = logoImage,
        links = LegalLinksSettings.BOTH,
        firstLayerSettings = FirstLayerStyleSettings(
            headerImage = HeaderImageSettings.ExtendedLogoSettings(firstLayerImage),
            title = TitleSettings(
                alignment = SectionAlignment.START,
                font = getCustomFont(context),
                textColor = Color.BLACK,
                textSizeInSp = 20f,
            ),
            message = MessageSettings(
                alignment = SectionAlignment.START,
                textSizeInSp = 16f,
                textColor = Color.DKGRAY,
                linkTextColor = Color.BLACK,
                underlineLink = true,
            ),
            cornerRadius = 30,
            buttonLayout = ButtonLayout.Row(
                listOf(
                    ButtonSettings(
                        type = ButtonType.MORE,
                        font = getCustomFont(context),
                        backgroundColor = Color.TRANSPARENT,
                        textColor = Color.BLACK,
                        textSizeInSp = buttonTextSizeInSp,
                        cornerRadius = buttonCornerRadius,
                    ),
                    ButtonSettings(
                        type = ButtonType.ACCEPT_ALL,
                        font = getCustomFont(context),
                        textSizeInSp = buttonTextSizeInSp,
                        cornerRadius = buttonCornerRadius,
                    ),
                )
            )
        ),
        secondLayerSettings = SecondLayerStyleSettings(
            buttonLayout = ButtonLayout.Row(
                listOf(
                    ButtonSettings(
                        type = ButtonType.SAVE,
                        cornerRadius = buttonCornerRadius,
                    ),
                    ButtonSettings(
                        type = ButtonType.ACCEPT_ALL,
                        cornerRadius = buttonCornerRadius,
                    ),
                )
            )
        )
    )
}
