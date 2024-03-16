package com.musa.musakeys.ui.welcome

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.databinding.MusaWelcomeScreenBinding
import com.musa.musakeys.ui.MusaClipboardModePopup
import com.musa.musakeys.ui.NavigationActivity
import com.musa.musakeys.utility.FontSelector
import com.musa.musakeys.utility.LocaleManager
import com.musa.musakeys.utility.SharedPreferenceHelperUtil

class WelcomeMusaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: MusaWelcomeScreenBinding

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(LocaleManager.wrap(context))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MusaWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val bri = extras?.getBoolean(MusaConstants.BROADCAST_RECEIVER_INTENT) ?: false

        if (bri) {
            val intent = Intent(this, NavigationActivity::class.java)
            if (extras != null) {
                intent.putExtra(MusaConstants.COPIED_TEXT, extras.getString(MusaConstants.COPIED_TEXT))
            }
            intent.putExtra(MusaConstants.WELCOME_SCREEN_ACTION, MusaConstants.BROADCAST_RECEIVER_INTENT)
            startActivity(intent)
            finish()
        }
        binding.copyrightText.text = getCurrentYearFormatted()
        binding.connectRemotely.setOnClickListener(this)
        binding.useLocally.setOnClickListener(this)
        binding.instructions.setOnClickListener(this)
        binding.fonts.setOnClickListener(this)

        binding.linkText.setOnClickListener {
            openWebPage("www.musa.bet")
        }
    }

    private fun getCurrentYearFormatted(): String {
        val currentYear = java.time.Year.now().value
        return "©$currentYear The Musa Academy"
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        binding.musaText.typeface = FontSelector.getAppropriateFont(this)
        binding.musaText.text = MusaConstants.musaTitle
    }

    override fun onClick(view: View) {
        val intent = Intent(this, NavigationActivity::class.java)
        when (view.id) {
            binding.connectRemotely.id -> {
                intent.putExtra(MusaConstants.WELCOME_SCREEN_ACTION, MusaConstants.USE_REMOTELY_ACTION)
                startActivity(intent)
            }
            binding.fonts.id -> {
                intent.putExtra(MusaConstants.WELCOME_SCREEN_ACTION, MusaConstants.LAST_COPIED_TEXT)
                startActivity(intent)
            }
            binding.instructions.id -> {
                intent.putExtra(MusaConstants.WELCOME_SCREEN_ACTION, MusaConstants.OPEN_HELP_ACTION)
                startActivity(intent)
            }
            binding.useLocally.id -> {
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                intent.putExtra(MusaConstants.WELCOME_SCREEN_ACTION, MusaConstants.COPIED_TEXT)
                SharedPreferenceHelperUtil.updateSharedPreferenceBoolean(this, SharedPreferenceEnum.ENABLE_BACKGROUND_SERVICE, true)
                MusaClipboardModePopup.renderPopup(this)
                startActivity(intent)
            }
        }
    }
}
