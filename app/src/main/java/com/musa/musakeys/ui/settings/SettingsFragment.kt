package com.musa.musakeys.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.musa.musakeys.R
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.ui.welcome.WelcomeMusaActivity
import com.musa.musakeys.utility.SharedPreferenceHelperUtil

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val fontSizeRadioGroup = view.findViewById<RadioGroup>(R.id.font_size_group)
        val fontSizeLarge = view.findViewById<RadioButton>(R.id.font_size_large)
        val fontSizeMedium = view.findViewById<RadioButton>(R.id.font_size_medium)
        val fontSizeSmall = view.findViewById<RadioButton>(R.id.font_size_small)

        val languageRadioGroup = view.findViewById<RadioGroup>(R.id.language_group)
        val languageAuto = view.findViewById<RadioButton>(R.id.language_auto)
        val languageEnglish = view.findViewById<RadioButton>(R.id.language_english)
        val languageSpanish = view.findViewById<RadioButton>(R.id.language_spanish)
        val languageFrench = view.findViewById<RadioButton>(R.id.language_french)
        val languageRussian = view.findViewById<RadioButton>(R.id.language_russian)
        val languageHindi = view.findViewById<RadioButton>(R.id.language_hindi)
        val languageChinese = view.findViewById<RadioButton>(R.id.language_chinese)
        val languagePortuguese = view.findViewById<RadioButton>(R.id.language_portuguese)
        val languageArabic = view.findViewById<RadioButton>(R.id.language_arabic)
        val languageJapanese = view.findViewById<RadioButton>(R.id.language_japanese)
        val languageVietnamese = view.findViewById<RadioButton>(R.id.language_vietnamese)

        activity?.let {
            SharedPreferenceHelperUtil.getSharedPreferenceString(
                it,
                SharedPreferenceEnum.FONT_SAVED, "font_small"
            )
                ?.let { setInitialFontSize(it, fontSizeSmall, fontSizeMedium, fontSizeLarge) }
        }
        activity?.let {
            SharedPreferenceHelperUtil.getSharedPreferenceString(
                it,
                SharedPreferenceEnum.LANGUAGE_SAVED, "auto"
            )?.let {
                setInitialLanguage(
                    it,
                    languageAuto,
                    languageEnglish,
                    languageSpanish,
                    languageFrench,
                    languageRussian,
                    languageHindi,
                    languageChinese,
                    languagePortuguese,
                    languageArabic,
                    languageJapanese,
                    languageVietnamese
                )
            }
        }

        fontSizeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.font_size_large -> activity?.let {
                    SharedPreferenceHelperUtil.updateSharedPreferenceString(
                        it,
                        SharedPreferenceEnum.FONT_SAVED, "font_large"
                    )
                }

                R.id.font_size_medium -> activity?.let {
                    SharedPreferenceHelperUtil.updateSharedPreferenceString(
                        it,
                        SharedPreferenceEnum.FONT_SAVED, "font_medium"
                    )
                }

                R.id.font_size_small -> activity?.let {
                    SharedPreferenceHelperUtil.updateSharedPreferenceString(
                        it,
                        SharedPreferenceEnum.FONT_SAVED, "font_small"
                    )
                }
            }
        }
        languageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val language = when (checkedId) {
                R.id.language_auto -> "auto"
                R.id.language_english -> "en"
                R.id.language_spanish -> "es"
                R.id.language_french -> "fr"
                R.id.language_russian -> "ru"
                R.id.language_hindi -> "hi"
                R.id.language_chinese -> "zh"
                R.id.language_portuguese -> "pt"
                R.id.language_arabic -> "ar"
                R.id.language_japanese -> "ja"
                R.id.language_vietnamese -> "vi"
                else -> "auto"
            }
            activity?.let {
                SharedPreferenceHelperUtil.updateSharedPreferenceString(
                    it,
                    SharedPreferenceEnum.LANGUAGE_SAVED, language
                )
            }
            restartApp()
        }
        return view
    }

    private fun setInitialFontSize(
        fontSizePref: String,
        fontSizeSmall: RadioButton,
        fontSizeMedium: RadioButton,
        fontSizeLarge: RadioButton
    ) {
        when (fontSizePref) {
            "font_small" -> fontSizeSmall.isChecked = true
            "font_medium" -> fontSizeMedium.isChecked = true
            "font_large" -> fontSizeLarge.isChecked = true
        }
    }

    private fun setInitialLanguage(
        languagePref: String,
        languageAuto: RadioButton,
        languageEnglish: RadioButton,
        languageSpanish: RadioButton,
        languageFrench: RadioButton,
        languageRussian: RadioButton,
        languageHindi: RadioButton,
        languageChinese: RadioButton,
        languagePortuguese: RadioButton,
        languageArabic: RadioButton,
        languageJapanese: RadioButton,
        languageVietnamese: RadioButton
    ) {
        when (languagePref) {
            "auto" -> languageAuto.isChecked = true
            "en" -> languageEnglish.isChecked = true
            "es" -> languageSpanish.isChecked = true
            "fr" -> languageFrench.isChecked = true
            "ru" -> languageRussian.isChecked = true
            "hi" -> languageHindi.isChecked = true
            "zh" -> languageChinese.isChecked = true
            "pt" -> languagePortuguese.isChecked = true
            "ar" -> languageArabic.isChecked = true
            "ja" -> languageJapanese.isChecked = true
            "vi" -> languageVietnamese.isChecked = true
        }
    }

    private fun restartApp() {
        val intent = Intent(context, WelcomeMusaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        (context as? Activity)?.finish()
    }
}
