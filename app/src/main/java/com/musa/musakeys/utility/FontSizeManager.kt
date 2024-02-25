package com.musa.musakeys.utility

import android.content.Context
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.getSharedPreferenceString


object FontSizeManager {
    const val LARGE = 24
    const val MEDIUM = 22
    const val SMALL = 18
    fun getFontSize(context: Context?): Int {
        val fontSizePreference = getSharedPreferenceString(
            context!!,
            SharedPreferenceEnum.FONT_SAVED,
            "font_small"
        )
        return when (fontSizePreference) {
            "font_large" -> LARGE
            "font_medium" -> MEDIUM
            "font_small" -> SMALL
            else -> SMALL
        }
    }
}
