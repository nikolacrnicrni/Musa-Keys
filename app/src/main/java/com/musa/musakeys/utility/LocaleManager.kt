package com.musa.musakeys.utility

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
import java.util.Locale


class LocaleManager(context: Context?) : ContextWrapper(context) {

    companion object {
        fun wrap(context: Context): ContextWrapper {
            var sharedPreferenceString: String =
                SharedPreferenceHelperUtil.getSharedPreferenceString(
                    context,
                    SharedPreferenceEnum.LANGUAGE_SAVED,
                    MusaConstants.LANGUAGE_AUTO
                ).toString()
            val configuration: Configuration = context.resources.configuration
            val language = Locale.getDefault().language
            if (MusaConstants.LANGUAGE_AUTO == sharedPreferenceString) {
                sharedPreferenceString = language
            }
            if (sharedPreferenceString != "") {
                val locale = Locale(sharedPreferenceString)
                Locale.setDefault(locale)
                setSystemLocale(configuration, locale)
                Log.e("Language ", "changed")
            }

            return LocaleManager(context.createConfigurationContext(configuration))
        }


        private fun setSystemLocale(configuration: Configuration, locale: Locale?) {
            configuration.setLocale(locale)
        }
    }
}
