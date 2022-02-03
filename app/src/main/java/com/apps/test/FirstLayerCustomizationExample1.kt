package com.apps.test

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.graphics.TypefaceCompat
import com.usercentrics.sdk.*

private val headerImage = UsercentricsImage.ImageDrawableId(R.drawable.header_extended)
private const val buttonTextSizeInSp = 16f
private const val buttonCornerRadius = 20

private fun getCustomFont(context: Context): Typeface {
    val titleFontPath = "fonts/AndadaPro.ttf"
    val base = Typeface.createFromAsset(context.assets, titleFontPath)
    return TypefaceCompat.create(context, base, Typeface.BOLD)
}

fun firstLayerCustomizationExample1(context: Context): FirstLayerStyleSettings {
    return FirstLayerStyleSettings(
        headerImage = HeaderImageSettings.ExtendedLogoSettings(headerImage),
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
        cornerRadius = 50,
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
    )
}
