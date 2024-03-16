package com.musa.musakeys.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.musa.musakeys.R
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.databinding.ActivityMainBinding
import com.musa.musakeys.musa_keyboard_system.KeyboardListener
import com.musa.musakeys.utility.FontSelector
import com.musa.musakeys.utility.MusaServiceUtility

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentSelectedItem: Int? = null
    private lateinit var navController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            MusaServiceUtility.stopMusaPhoneBackgroundService(context = this@NavigationActivity)
            setSupportActionBar(appBarMain.toolbar)
            setMusaTitle(appBarMain.musaTitle)
            setupDrawerLayout()
            handleIntentExtras()
            appBarMain.hamburger.setOnClickListener { toggleDrawer() }
            appBarMain.musaTitle.setOnClickListener { finish() }
            appBarMain.drawerFlag.setOnClickListener { finish() }
        }
    }

    private fun ActivityMainBinding.setupDrawerLayout() {
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_passkey_fragment,
            R.id.nav_musa_remote_landscape_keypad,
            R.id.nav_musa_settings,
            R.id.nav_instructions,
            R.id.nav_fonts,
            R.id.nav_close
        ).setOpenableLayout(drawerLayout).build()

        navController = Navigation.findNavController(this@NavigationActivity,
            R.id.nav_host_fragment
        )
        NavigationUI.setupWithNavController(navView, navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            val shouldCloseDrawer = if (currentSelectedItem != menuItem.itemId) {
                navigateTo(menuItem.itemId)
                true
            } else false

            if (shouldCloseDrawer) drawerLayout.closeDrawer(GravityCompat.END, false)
            shouldCloseDrawer
        }
    }

    private fun toggleDrawer() {
        with(binding.drawerLayout) {
            if (isDrawerOpen(GravityCompat.END)) closeDrawer(GravityCompat.END, false)
            else openDrawer(GravityCompat.END)
        }
    }

    private fun navigateTo(navId: Int) {
        navController.popBackStack()
        navController.navigate(navId)
        currentSelectedItem = navId
    }

    private fun handleIntentExtras() {
        intent.extras?.getString(MusaConstants.WELCOME_SCREEN_ACTION)?.let { action ->
            val destination = when (action) {
                MusaConstants.COPIED_TEXT -> R.id.nav_musa_phone_portrait_mode.also {
                    intent.getStringExtra(MusaConstants.COPIED_TEXT)?.let { text ->
                        navController.navigate(it, Bundle().apply { putString(MusaConstants.COPIED_TEXT, text) })
                    }
                }
                MusaConstants.USE_REMOTELY_ACTION -> R.id.nav_passkey_fragment
                MusaConstants.OPEN_HELP_ACTION -> R.id.nav_instructions
                MusaConstants.OPEN_SETTING_ACTION -> R.id.nav_musa_settings
                MusaConstants.LAST_COPIED_TEXT -> R.id.nav_fonts
                else -> null
            }
            destination?.let { navigateTo(it) }
        }
    }

    fun setMusaTitle(textView: TextView) {
        textView.apply {
            typeface = FontSelector.getAppropriateFont(context = this@NavigationActivity)
            text = MusaConstants.musaTitle
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardListener().deleteAll(this)
        KeyboardListener.getInstance()?.clearKeyboard()
    }
}
