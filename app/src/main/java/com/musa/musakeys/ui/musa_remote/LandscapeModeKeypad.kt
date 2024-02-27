package com.musa.musakeys.ui.musa_remote

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
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
import com.musa.musakeys.R
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.databinding.FragmentLandscapeModeBinding
import com.musa.musakeys.musa_keyboard_system.KeyboardListener
import com.musa.musakeys.remoteConnection.NetworkCommunicationAsync
import com.musa.musakeys.utility.FontSelector.getAppropriateFont
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.getSharedPreferenceBoolean

class LandscapeModeKeypad : Fragment(), View.OnClickListener,
    OnLongClickListener {
    private var backSpaceOrResetImageView: ImageView? = null
    private var backSpaceResetCover: LinearLayout? = null
    private var childKeysFont: Typeface? = null
    private var dushanFont: Typeface? = null
    private var enter: LinearLayout? = null
    private var isTestMode = false
    private val leftParentButtons: ArrayList<Button?> = ArrayList<Button?>(20)
    private var outputText: TextView? = null
    private var parentKeysFont: Typeface? = null
    private val rightParentButtons: ArrayList<Button?> = ArrayList<Button?>(20)
    private var shift: LinearLayout? = null
    private var textFromPortraitMode = ""

    private var _binding: FragmentLandscapeModeBinding? = null
    private val binding get() = _binding!!

    override fun onClick(view: View) {}
    override fun onLongClick(view: View): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        switchToPortraitModeIfNecessary()


        val view = inflater.inflate(R.layout.fragment_landscape_mode, container, false)
        initializeViews(view)

        val arguments = arguments
        if (arguments != null) {
            textFromPortraitMode = arguments.getString(MusaConstants.TEXT_FROM_PORTRAIT, "")
        }
        isTestMode =
            getSharedPreferenceBoolean(requireActivity(), SharedPreferenceEnum.IS_TEST_MODE, false)

        dushanFont = getAppropriateFont(requireActivity())
        parentKeysFont = getAppropriateFont(requireActivity())
        childKeysFont = getAppropriateFont(requireActivity())
        outputText!!.typeface = dushanFont
        outputText!!.movementMethod = ScrollingMovementMethod()
        outputText!!.text = textFromPortraitMode
        outputText!!.addTextChangedListener(object : TextWatcher {
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

        return view
    }

    private fun initializeViews(view: View) {
        backSpaceResetCover = view.findViewById(R.id.backspace_cover)
        backSpaceOrResetImageView = view.findViewById(R.id.backspace)
        enter = view.findViewById(R.id.enter)
        shift = view.findViewById(R.id.shift)
        outputText = view.findViewById(R.id.key_output)
    }

    override fun onResume() {
        super.onResume()
        isFragmentRunning = true
    }

    @SuppressLint("WrongConstant")
    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService("android.content.Context.INPUT_METHOD_SERVICE") as InputMethodManager
        val currentFocus = requireActivity().currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 2)
        }
    }

    private fun setOnClickListenerOnAllButtons() {
        for (next in leftParentButtons) {
            next!!.setOnClickListener(this)
            next.setOnLongClickListener(this)
        }
        for (next2 in rightParentButtons) {
            next2!!.setOnClickListener(this)
            next2.setOnLongClickListener(this)
        }
    }

    override fun onDestroyView() {
        sendData("stop")
        super.onDestroyView()
        isFragmentRunning = false
    }

    private fun resetAlpha(list: List<Button>) {
        for (alpha in list) {
            alpha.alpha = 1.0f
        }
    }

    private fun setAlphaForOtherKeys(view: View, list: List<Button>) {
        for (next in list) {
            if (next.id != view.id) {
                next.alpha = 0.1f
            } else {
                next.alpha = 1.0f
            }
        }
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)
        switchToPortraitModeIfNecessary()
    }

    private fun switchToPortraitModeIfNecessary() {
        if (resources.configuration.orientation == 1) {
            val findNavController = findNavController(requireActivity(), R.id.nav_host_fragment)
            findNavController.popBackStack()
            val bundle = Bundle()
            val textView = outputText
            bundle.putString(MusaConstants.TEXT_FROM_LANDSCAPE, textView?.text?.toString() ?: "")
            findNavController.navigate(R.id.nav_musa_remote_portrait_keypad as Int, bundle)
        }
    }

    /* access modifiers changed from: private */
    fun sendData(str: String?) {
        if (!isTestMode) {
            NetworkCommunicationAsync(
                requireActivity(),
                false,
                (null as String?)!!,
                str!!
            ).execute(*arrayOfNulls<Void>(0))
        }
    }

    companion object {
        var isFragmentRunning = false
    }
}
