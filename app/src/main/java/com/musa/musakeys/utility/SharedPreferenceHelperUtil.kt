package com.musa.musakeys.utility

import android.content.Context
import com.musa.musakeys.constants.SharedPreferenceEnum


object SharedPreferenceHelperUtil {
    fun updateSharedPreferenceString(
        context: Context,
        sharedPreferenceEnum: SharedPreferenceEnum,
        str: String?
    ) {
        val edit =
            context.getSharedPreferences(SharedPreferenceEnum.MUSA_SHARED_PREFERENCE.name, 0).edit()
        edit.putString(sharedPreferenceEnum.name, str)
        edit.apply()
    }

    fun updateSharedPreferenceBoolean(
        context: Context,
        sharedPreferenceEnum: SharedPreferenceEnum,
        z: Boolean
    ) {
        val edit =
            context.getSharedPreferences(SharedPreferenceEnum.MUSA_SHARED_PREFERENCE.name, 0).edit()
        edit.putBoolean(sharedPreferenceEnum.name, z)
        edit.apply()
    }

    fun getSharedPreferenceString(
        context: Context,
        sharedPreferenceEnum: SharedPreferenceEnum,
        str: String?
    ): String? {
        return context.getSharedPreferences(SharedPreferenceEnum.MUSA_SHARED_PREFERENCE.name, 0)
            .getString(sharedPreferenceEnum.name, str)
    }

    fun getSharedPreferenceBoolean(
        context: Context,
        sharedPreferenceEnum: SharedPreferenceEnum,
        z: Boolean
    ): Boolean {
        return context.getSharedPreferences(SharedPreferenceEnum.MUSA_SHARED_PREFERENCE.name, 0)
            .getBoolean(sharedPreferenceEnum.name, z)
    }
}
