package com.musa.musakeys.utility

import android.content.Context
import android.text.TextUtils
import com.musa.musakeys.constants.SharedPreferenceEnum
import java.io.Serializable


class MySharedPreference(private val context: Context) {
    fun setPreferences(str: String?, str2: Serializable) {
        val edit =
            context.getSharedPreferences(SharedPreferenceEnum.MUSA_SHARED_PREFERENCE.name, 0)
                .edit()
        edit.putString(str, str2.toString())
        edit.apply()
    }

    fun getPreferences(str: String?): String? {
        val string =
            context.getSharedPreferences(SharedPreferenceEnum.MUSA_SHARED_PREFERENCE.name, 0)
                .getString(str, "")
        return if (TextUtils.isEmpty(string)) {
            ""
        } else string
    }
}
