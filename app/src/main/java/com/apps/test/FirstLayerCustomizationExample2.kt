package com.apps.test

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.usercentrics.sdk.*

private val headerImage = UsercentricsImage.ImageDrawableId(R.drawable.header_logo)
private const val buttonTextSizeInSp = 14f
private const val buttonCornerRadius = 50

fun firstLayerCustomizationExample2(context: Context): FirstLayerStyleSettings {
    return FirstLayerStyleSettings(
        backgroundColor = ContextCompat.getColor(context, R.color.dark),
        headerImage = HeaderImageSettings.LogoSettings(headerImage),
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
    )
}
