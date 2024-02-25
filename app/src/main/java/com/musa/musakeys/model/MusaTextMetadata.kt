package com.musa.musakeys.model

import android.graphics.Typeface

data class MusaTextMetadata(
    val displayText: String?,
    val actualText: String?,
    val displaySize: Int,
    val textFont: Typeface?,
    val backgroundColor: String,
    val foregroundColor: String
) {
    class Builder {
        private var actualText: String? = null
        private var backgroundColor: String = "#808080"
        private var displaySize: Int = 0
        private var displayText: String? = null
        private var foregroundColor: String = "#000080"
        private var textFont: Typeface? = null

        fun withDisplayText(displayText: String) = apply { this.displayText = displayText }

        fun withActualText(actualText: String) = apply { this.actualText = actualText }

        fun withDisplaySize(displaySize: Int) = apply { this.displaySize = displaySize }

        fun withTextFont(textFont: Typeface) = apply { this.textFont = textFont }

        fun withBackgroundColor(backgroundColor: String) = apply { this.backgroundColor = backgroundColor }

        fun withForegroundColor(foregroundColor: String) = apply { this.foregroundColor = foregroundColor }

        fun build() = MusaTextMetadata(
            displayText,
            actualText,
            displaySize,
            textFont,
            backgroundColor,
            foregroundColor
        )
    }

    companion object {
        fun builder() = Builder()
    }
}
