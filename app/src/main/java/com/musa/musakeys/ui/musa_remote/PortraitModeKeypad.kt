package com.musa.musakeys.ui.musa_remote


import com.musa.musakeys.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.musa_keyboard_system.KeyboardListener
import com.musa.musakeys.remoteConnection.NetworkCommunicationAsync
import com.musa.musakeys.utility.FontSelector.getAppropriateFont
import com.musa.musakeys.utility.FontSizeManager.getFontSize
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.getSharedPreferenceBoolean


class PortraitModeKeypad : Fragment(), View.OnClickListener,
    OnLongClickListener {
    private val addZWNJ = false
    private val backspace: ImageView? = null
    private val backspaceCover: LinearLayout? = null
    private val buttonList: List<Button>? = null
    private var childKeysFont: Typeface? = null
    private var dushanFont: Typeface? = null
    private val enterKey: LinearLayout? = null
    private val isFirstClick = true
    private val isNumericLowDigitMode = true
    private val isNumericMode = false
    private val isShiftMode = false
    private var isTestMode = false

    /* access modifiers changed from: private */
    var keyOutput: TextView? = null
    private var parentKeysFont: Typeface? = null
    private val shiftKey: LinearLayout? = null
    private val shwaRemoteTitle: TextView? = null
    private val t = 0
    private var textFromLandscapeMode = ""
    private val yauLongPressed = false
    override fun onClick(view: View) {}
    override fun onLongClick(view: View): Boolean {
        return true
    }

    override fun onStart() {
        super.onStart()
        isFragmentRunning = true
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        isFragmentRunning = true
        val inflate: View =
            layoutInflater.inflate(R.layout.fragment_portrait_mode, viewGroup, false)
        switchToLandscapeModeIfNecessary()
        dushanFont = getAppropriateFont(requireActivity())
        keyOutput = inflate.findViewById<View>(R.id.key_output) as TextView
        keyOutput!!.isCursorVisible = true
        keyOutput!!.setTypeface(dushanFont)
        val arguments = arguments
        if (arguments != null) {
            textFromLandscapeMode = arguments.getString(MusaConstants.TEXT_FROM_LANDSCAPE, "")
        }
        if (textFromLandscapeMode.isNotEmpty()) {
            keyOutput!!.text = textFromLandscapeMode
        }
        keyOutput!!.textSize = getFontSize(activity).toFloat()
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            1,
            0
        )
        isTestMode =
            getSharedPreferenceBoolean(requireActivity(), SharedPreferenceEnum.IS_TEST_MODE, false)
        initView(inflate)
        keyOutput!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {}
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (KeyboardListener.isEnterHit) {
                    sendData("⏎")
                    KeyboardListener.isEnterHit = false
                } else if (KeyboardListener.isEscapeHit) {
                    sendData("⎋")
                    KeyboardListener.isEscapeHit = false
                } else if (KeyboardListener.backspaceHit) {
                    sendData("⌫")
                } else if (KeyboardListener.IsTabHit) {
                    sendData("⇥")
                } else if (i2 < charSequence.toString().length) {
                    sendData(charSequence[charSequence.length - 1].toString())
                }
            }
        })
        return inflate
    }

    @SuppressLint("WrongConstant")
    override fun onResume() {
        super.onResume()
        isFragmentRunning = true
        keyOutput!!.post {
            keyOutput!!.requestFocus()
            (this@PortraitModeKeypad.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                keyOutput,
                1
            )
        }
    }

    override fun onPause() {
        super.onPause()
        isFragmentRunning = false
    }

    private fun initView(view: View) {
        parentKeysFont = getAppropriateFont(requireActivity())
        childKeysFont = getAppropriateFont(requireActivity())
    }

    override fun onDestroyView() {
        sendData("stop")
        super.onDestroyView()
        isFragmentRunning = false
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)
        switchToLandscapeModeIfNecessary()
    }

    private fun switchToLandscapeModeIfNecessary() {
        if (resources.configuration.orientation == 2) {
            val findNavController = findNavController(requireActivity(), R.id.nav_host_fragment)
            findNavController.popBackStack()
            val bundle = Bundle()
            val textView = keyOutput
            bundle.putString(MusaConstants.TEXT_FROM_PORTRAIT, textView?.text?.toString() ?: "")
            findNavController.navigate(R.id.nav_musa_remote_landscape_keypad as Int, bundle)
        }
    }

    fun sendData(str: String?) {
        if (!isTestMode) {
            NetworkCommunicationAsync(
                requireActivity(),
                false,
                "",
                str!!
            ).execute(*arrayOfNulls<Void>(0))
        }
    }

    companion object {
        var isFragmentRunning = false
    }
}
