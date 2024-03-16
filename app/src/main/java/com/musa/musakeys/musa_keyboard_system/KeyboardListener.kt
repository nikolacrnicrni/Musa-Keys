package com.musa.musakeys.musa_keyboard_system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.inputmethodservice.InputMethodService
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.musa.musakeys.R
import com.musa.musakeys.asyncTasks.AsyncResult
import com.musa.musakeys.asyncTasks.DeleteEntityAsyncTask
import com.musa.musakeys.asyncTasks.EntityPersistenceListener
import com.musa.musakeys.asyncTasks.InsertEntityAsyncTask
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.databinding.KeyboardSystemViewBinding
import com.musa.musakeys.db.InstantiateDatabase
import com.musa.musakeys.db.PersistablePreviousMessage
import com.musa.musakeys.db.PreviousMessagesAdapter
import com.musa.musakeys.model.MusaTextMetadata
import com.musa.musakeys.remoteConnection.NetworkCommunicationAsync
import com.musa.musakeys.ui.musa_remote.LandscapeModeKeypad
import com.musa.musakeys.ui.musa_remote.PortraitModeKeypad
import com.musa.musakeys.utility.AsciiChars
import com.musa.musakeys.utility.FontSelector
import com.musa.musakeys.utility.FontSizeManager
import com.musa.musakeys.utility.FormulaUtils
import com.musa.musakeys.utility.FormulaUtils.getParentKeyboard
import com.musa.musakeys.utility.FormulaUtils.getShiftedParentKeys
import com.musa.musakeys.utility.MySharedPreference
import com.musa.musakeys.utility.SharedPreferenceHelperUtil

class KeyboardListener : InputMethodService(), View.OnClickListener, View.OnLongClickListener,
    TextWatcher, KeyboardActions {

    companion object {
        var IsTabHit = false
        var backspaceHit = false
        var ic: InputConnection? = null
        var isEnterHit = false
        var isEscapeHit = false


        @Volatile
        private var instance: KeyboardListener? = null

        fun getInstance(): KeyboardListener? = instance

        fun setInstance(instance: KeyboardListener?) {
            this.instance = instance
        }
    }

    private lateinit var binding: KeyboardSystemViewBinding

    private var smsGone = false
    private var addZWNJ = false
    private lateinit var buttonList: List<Button>
    private lateinit var childKeysFont: Typeface
    private var computerIconPos = false
    private var isFirstClick = true
    private var isNumericLowDigitMode = true
    private var isNumericMode = false
    private var isShiftMode = false
    private var isTestMode = false
    private lateinit var mAdapter: PreviousMessagesAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var numericWithin22 = false
    private lateinit var parentKeysFont: Typeface
    private var shiftMode22 = false
    private var t = 0
    private var yauLongPressed = false
    private var isHentrax = false
    private val mySharedPreference = MySharedPreference(this)

    override fun onCreate() {
        super.onCreate()
        setInstance(this)
        checkComputerIconPos()
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        checkComputerIconPos()
    }

    override fun onCreateInputView(): View {
        binding = KeyboardSystemViewBinding.inflate(layoutInflater)

        isTestMode = SharedPreferenceHelperUtil.getSharedPreferenceBoolean(
            applicationContext,
            SharedPreferenceEnum.IS_TEST_MODE,
            false
        )

        initView()

        binding.keyOutput.addTextChangedListener(this)
        updateViews(getParentKeyboard(parentKeysFont))
        saveCopiedTextClipboard()

        return binding.root
    }

    private fun saveCopiedTextClipboard() {
        (applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).primaryClip?.let { clip ->
            if (clip.description.hasMimeType("text/html") || clip.description.hasMimeType("text/plain")) {
                saveCopiedText(clip.getItemAt(0).text.toString())
            }
        }
    }

    override fun onClick(view: View) {
        ic = currentInputConnection
        when (view.id) {
            binding.backspaceCover.id -> handleBackspace()
            binding.enter.id -> handleEnter()
            binding.romanKeyboard.id, R.id.roman_keyboard_on -> (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
//            binding.shift.id -> handleShift()
            binding.smsLabel.id -> toggleSmsLabel()
            else -> otherKeyboards(view)
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (isFirstClick) processFirstClick(view, true)
        else processSecondClick(view, true)
        return true
    }

    private fun handleBackspace() {
        binding.keyOutput.setText(binding.keyOutput.text.dropLast(1))

        // Reset states only if it was not the first click
        if (!isFirstClick) {
            shiftMode22 = false
            isFirstClick = true
            updateKeyboardUI()
        }

        if (binding.keyOutput.text.isEmpty()) {
            handleInputConnectionDeletion()
        }
    }

    private fun updateKeyboardUI() {
        updateViews(getParentKeyboard(parentKeysFont))
        binding.backspace.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.backspace
            )
        )
    }

    private fun handleInputConnectionDeletion() {
        val textBeforeCursor = currentInputConnection?.getTextBeforeCursor(100, 0) ?: ""
        if (textBeforeCursor.isNotEmpty()) {
            val hasSelectedText = !TextUtils.isEmpty(currentInputConnection?.getSelectedText(0))
            if (hasSelectedText) {
                currentInputConnection?.commitText("", 1)
            } else {
                currentInputConnection?.deleteSurroundingText(1, 0)
            }
        }
    }

    private fun handleEnter() {
        binding.keyOutput.append("⏎")
    }

//    private fun handleShift() {
//        isShiftMode = !isShiftMode
//        updateViews(
//            if (isShiftMode) getShiftedParentKeys(i4, parentKeysFont) else getParentKeyboard(
//                parentKeysFont
//            )
//        )
//    }

    private fun toggleSmsLabel() {
        smsGone = !smsGone
        if (smsGone) {
            binding.romanKeyboardOn.visibility = View.GONE
            binding.container.visibility = View.GONE
            binding.recyclerContainer.visibility = View.VISIBLE
            binding.keyOutput.isEnabled = false
            binding.backspaceCover.visibility = View.GONE
            binding.keyOutput.visibility = View.GONE
            binding.romanKeyboard.visibility = View.GONE
            binding.smsLabel.setImageResource(R.drawable.ic_musa_keyboard)
        } else {
            binding.recyclerContainer.visibility = View.GONE
            binding.container.visibility = View.VISIBLE
            binding.backspaceCover.visibility = View.VISIBLE
            binding.keyOutput.visibility = View.VISIBLE
            binding.romanKeyboardOn.visibility = View.VISIBLE
            binding.keyOutput.isEnabled = true
            binding.romanKeyboard.visibility = View.GONE
            binding.smsLabel.setImageResource(R.drawable.ic_sms)
        }
        checkComputerIconPos()
    }

    private fun initView() {
        parentKeysFont = FontSelector.getAppropriateFont(applicationContext)!!
        childKeysFont = FontSelector.getAppropriateFont(applicationContext)!!
        buttonList = ArrayList()

        val buttons = listOf(
            binding.parent1, binding.parent2, binding.parent3, binding.parent4,
            binding.parent5, binding.parent6, binding.parent7, binding.parent8,
            binding.parent9, binding.parent10, binding.parent11, binding.parent12,
            binding.parent13, binding.parent14, binding.parent15, binding.parent16,
            binding.parent17, binding.parent18, binding.parent19, binding.parent20,
            binding.parent21, binding.parent22, binding.parent23, binding.parent24,
            binding.parent25, binding.parent26, binding.parent27
        ).also { buttonList = it }

        // Set LayoutManager and Adapter for RecyclerView
        mLayoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = mLayoutManager
        mAdapter = PreviousMessagesAdapter(applicationContext)
        binding.recyclerView.adapter = mAdapter

        // Setting typeface and other properties for keyOutput TextView
        binding.keyOutput.typeface = FontSelector.getAppropriateFont(applicationContext)
        binding.keyOutput.textSize = FontSizeManager.getFontSize(baseContext).toFloat()
        binding.keyOutput.setText("")

        // Setting click and long click listeners
        binding.enter.setOnClickListener(this)
        binding.shift.setOnClickListener(this)
        binding.smsLabel.setOnClickListener(this)
        binding.romanKeyboardOn.setOnClickListener(this)
        binding.romanKeyboard.setOnClickListener(this)
        binding.backspaceCover.setOnClickListener(this)

        buttons.forEach { button ->
            button.typeface = parentKeysFont
            button.setOnClickListener(this)
            button.setOnLongClickListener(this)
        }
    }

    private fun updateViews(list: List<MusaTextMetadata?>?) {
        list?.forEachIndexed { index, metadata ->
            buttonList.getOrNull(index)?.apply {
                typeface = metadata?.textFont ?: parentKeysFont
                textSize = metadata?.displaySize?.toFloat() ?: textSize
                text = metadata?.displayText ?: text
            }
        }
    }

    private fun processFirstClick(view: View, z: Boolean) {
        val str: String?
        if (z) {
            when (view.id) {
                binding.parent1.id -> {
                    binding.keyOutput.setText(binding.keyOutput.text.toString() + "⎋")
                    return
                }

                binding.parent2.id -> {
                    binding.keyOutput.setText(binding.keyOutput.text.toString() + "⇥")
                    return
                }

                binding.parent3.id -> {
                    binding.keyOutput.setText(binding.keyOutput.text.toString() + "↵")
                    return
                }

                binding.parent4.id -> {
                    binding.keyOutput.setText(binding.keyOutput.text.toString() + " ")
                    return
                }
            }
        }
        val z2 = z || isShiftMode
        t = FormulaUtils.getSingleKeyboardButtonIds().indexOf(Integer.valueOf(view.id))
        if (z2 && t == 9) {
            updateViews(FormulaUtils.getNumericKeysParent(parentKeysFont))
            isNumericMode = true
        } else if (isNumericMode) {
            if (isShiftMode) {
                val i = t
                if (i == 1 || i == 2 || i == 3) {
                    updateViews(FormulaUtils.getNumericKeysParent(parentKeysFont))
                    isNumericMode = true
                    isShiftMode = false
                    return
                }
            } else {
                when (t) {
                    1 -> {
                        if (FormulaUtils.NUMERIC_LOW_MODE_KEY == buttonList[3].text) {
                            buttonList[1].text = FormulaUtils.NUMERIC_HIGH_MODE_KEY
                            buttonList[3].text = FormulaUtils.PARENT_KEY_ORIGINAL_LOW_MODE
                            isNumericLowDigitMode = false
                            return
                        }
                        return
                    }

                    2 -> {
                        isNumericLowDigitMode = false
                        isNumericMode = false
                        updateViews(getParentKeyboard(parentKeysFont))
                        isNumericLowDigitMode = true
                        return
                    }

                    3 -> {
                        if (FormulaUtils.NUMERIC_HIGH_MODE_KEY == buttonList[1].text) {
                            buttonList[3].text = FormulaUtils.NUMERIC_LOW_MODE_KEY
                            buttonList[1].text = FormulaUtils.PARENT_KEY_ORIGINAL_HIGH_MODE
                            isNumericLowDigitMode = true
                            return
                        }
                        return
                    }
                }
            }
            if (z2) {
                t += 27
            }
            str = if (isNumericLowDigitMode) {
                FormulaUtils.getNumericLowDigits()[t]!!.actualText
            } else {
                FormulaUtils.getNumericHighDigits()[t]!!.actualText
            }
            binding.keyOutput.setText(binding.keyOutput.text.toString() + str)
            if (isShiftMode) {
                isShiftMode = false
                updateViews(FormulaUtils.getNumericKeysParent(parentKeysFont))
            }
        } else {
            if (isShiftMode) {
                if (FormulaUtils.getSingleKeyboardButtonIds()
                        .indexOf(Integer.valueOf(view.id)) == 19
                ) {
                    addZWNJ = true
                }
                val asciiCharForIndex = AsciiChars.getAsciiCharForIndex(t)
                if (asciiCharForIndex != null) {
                    val unicode = asciiCharForIndex.unicode
                    isFirstClick = true
                    isShiftMode = false
                    binding.keyOutput.setText(binding.keyOutput.text.toString() + unicode)
                    updateViews(getParentKeyboard(parentKeysFont))
                    binding.backspace.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.backspace
                        )
                    )
                    return
                }
            }
            var i3 = t
            if (z2) {
                i3 += 27
            }
            t = i3

            isHentrax =
                mySharedPreference.getPreferences(MusaConstants.SAVED_FONT) == "HentraxMusaElement-Regular"
            updateViews(FormulaUtils.getChildKeysForTopParent(t, childKeysFont, isHentrax))

            isFirstClick = false
            isShiftMode = false
            binding.backspace.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.restart
                )
            )
            yauLongPressed = !(!z2 || FormulaUtils.getSingleKeyboardButtonIds()
                .indexOf(Integer.valueOf(view.id)) != 19)
        }
    }

    private fun processSecondClick(view: View, z: Boolean) {
        isHentrax =
            mySharedPreference.getPreferences(MusaConstants.SAVED_FONT) == "HentraxMusaElement-Regular"

        val indexOf = FormulaUtils.getSingleKeyboardButtonIds()
            .indexOf(view.id) + if (z || isShiftMode) 27 else 0

        val actualText = FormulaUtils.getOutput(t, indexOf, isHentrax).actualText
        val resultText = StringBuilder()

        // Prepend Zero Width Non-Joiner if conditions are met
        if ((yauLongPressed && indexOf != 15) || (addZWNJ && indexOf == 17)) {
            resultText.append(Html.fromHtml("&#x200C;").toString())
        }
        resultText.append(actualText)

        if (addZWNJ && indexOf == 15) {
            resultText.append(Html.fromHtml("&#x200C;").toString())
        }

        // Reset flags
        yauLongPressed = false
        addZWNJ = false

        if (resultText.isNotEmpty()) {
            isFirstClick = true
            isShiftMode = false
            binding.keyOutput.text.append(resultText.toString())
            updateViews(getParentKeyboard(parentKeysFont))
            binding.backspace.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.backspace
                )
            )
        }
    }


    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        when {
            shouldHandleTextChange(charSequence) -> handleTextChange(charSequence)
            isEnterHit -> handleEnterKey()
            IsTabHit -> handleTab()
            isEscapeHit -> handleEscape()
            backspaceHit && charSequence.isNotEmpty() -> handleBackspaceKey()
        }
    }

    private fun shouldHandleTextChange(charSequence: CharSequence): Boolean =
        !backspaceHit && charSequence.isNotEmpty() && !isEnterHit && !isEscapeHit && !IsTabHit

    private fun handleTextChange(charSequence: CharSequence) {
        sendKey(charSequence.last().toString())
        ic?.setComposingText(charSequence.last().toString(), 1)
        ic?.finishComposingText()
    }

    private fun handleEnterKey() {
        sendKey("\n")
        ic?.setComposingText("\n", 1)
        ic?.finishComposingText()
        isEnterHit = false
    }

    private fun handleTab() {
        sendKey("\t")
        ic?.setComposingText("\t", 1)
        ic?.finishComposingText()
        IsTabHit = false
    }

    private fun handleEscape() {
        sendKey("\u001b")
        ic?.setComposingText("\u001b", 1)
        ic?.finishComposingText()
        isEscapeHit = false
    }

    private fun handleBackspaceKey() {
        val hasSelectedText = !TextUtils.isEmpty(ic?.getSelectedText(0))
        if (hasSelectedText) ic?.commitText("", 1) else ic?.deleteSurroundingText(1, 0)
        sendKey("⌫")
    }

    private fun sendKey(key: String) {
        if (isOperationAllowed()) {
            sendData(key)
        }
    }

    private fun isOperationAllowed(): Boolean =
        NetworkCommunicationAsync.IsConnected && !PortraitModeKeypad.isFragmentRunning && !LandscapeModeKeypad.isFragmentRunning

    override fun afterTextChanged(p0: Editable?) {}

    private fun saveCopiedText(str: String) {
        if (str.isNotEmpty()) {
            val persistablePreviousMessage = PersistablePreviousMessage()
            persistablePreviousMessage.message = str
            persistablePreviousMessage.isReceived = true
            InsertEntityAsyncTask(
                applicationContext,
                persistablePreviousMessage,
                object : EntityPersistenceListener {
                    override fun onEntityPersisted(
                        persistablePreviousMessage: PersistablePreviousMessage?,
                        j: Long
                    ) {
                        mAdapter.addData(persistablePreviousMessage)
                        (this@KeyboardListener.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(
                            ClipData.newPlainText("", "")
                        )
                    }

                    override fun onFailureOccured(asyncResult: AsyncResult?) {
                        if (asyncResult != null) {
                            Toast.makeText(
                                this@KeyboardListener.applicationContext,
                                asyncResult.errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                InstantiateDatabase.getDatabaseInstance(applicationContext)!!.messageDao()
            ).execute(
                arrayOfNulls(0)
            )
            Handler(Looper.getMainLooper()).postDelayed({
                binding.recyclerView.layoutManager!!.scrollToPosition(
                    mAdapter.itemCount - 1
                )
            }, 50)
        }
    }


    private fun otherKeyboards(view: View) {
        when {
            numericWithin22 -> handleNumericWithin22(view)
            shiftMode22 -> handleShiftMode22(view)
            isFirstClick -> processFirstClick(view, false)
            else -> processSecondClick(view, false)
        }
    }

    private fun handleNumericWithin22(view: View) {
        FormulaUtils.getSingleKeyboardButtonIds().indexOf(view.id).let { index ->
            when (index) {
                8 -> {
                    setKeyMode(true, "⬒", "")
                }

                17 -> {
                    numericWithin22 = false
                    isFirstClick = true
                    computerIconPos = false
                    resetKeyboardState()
                }

                26 -> {
                    setKeyMode(false, "", "⬓")
                }

                else -> {
                    val additionalText = FormulaUtils.highDigitsMusa.getOrElse(index) { "" }
                    appendText(additionalText)
                }
            }
        }

        when (view.id) {
            binding.parent9.id -> if (computerIconPos) println("nothing")
            binding.parent27.id -> if (!computerIconPos) println("nothing")
            binding.parent18.id -> {
                numericWithin22 = false
                isFirstClick = true
                resetKeyboardState()
            }
        }
    }

    private fun handleShiftMode22(view: View) {
        when (view.id) {
            binding.parent1.id -> appendAndResetKeyboard("\n")
            binding.parent18.id -> appendAndResetKeyboard(FormulaUtils.shiftCharacters[5])
            binding.parent27.id -> appendAndResetKeyboard(FormulaUtils.shiftCharacters[6])
            binding.parent3.id -> appendAndResetKeyboard("\u001b")
            binding.parent4.id, binding.parent5.id, binding.parent6.id, binding.parent8.id, binding.parent9.id -> {
                val charToAdd = when (view.id) {
                    binding.parent4.id -> "\t"
                    binding.parent5.id -> "‌"
                    binding.parent6.id -> "‍"
                    binding.parent8.id -> FormulaUtils.shiftCharacters[3]
                    binding.parent9.id -> FormulaUtils.shiftCharacters[4]
                    else -> ""
                }
                appendAndResetKeyboard(charToAdd)
            }

            binding.parent7.id -> {
                updateViews(FormulaUtils.getNumericCharacters(parentKeysFont))
                numericWithin22 = true
                isFirstClick = true
                binding.backspace.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.backspace
                    )
                )
            }

            else -> appendText(" ")
        }
    }

    private fun resetKeyboardState() {
        updateViews(getParentKeyboard(parentKeysFont))
        binding.backspace.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.backspace
            )
        )
        shiftMode22 = false
    }

    private fun setKeyMode(iconPos: Boolean, key8Text: String, key26Text: String) {
        buttonList[8].text = key8Text
        buttonList[26].text = key26Text
        computerIconPos = iconPos
    }

    private fun appendText(text: String) {
        binding.keyOutput.setText(StringBuilder(binding.keyOutput.text).append(text))
    }

    private fun appendAndResetKeyboard(text: String) {
        appendText(text)
        isFirstClick = true
        resetKeyboardState()
    }

    private fun checkComputerIconPos() {
        if (!numericWithin22) {
            return
        }
        if (computerIconPos) {
            buttonList[8].text = "⬒"
            buttonList[26].text = ""
            return
        }
        buttonList[8].text = ""
        buttonList[26].text = "⬓"
    }

    private fun sendData(text: String) {
        if (!isTestMode) {
            NetworkCommunicationAsync(applicationContext, false, "", text).execute()
        }
    }

    fun deleteAll(context: Context?) {
        context?.let {
            DeleteEntityAsyncTask(
                it,
                InstantiateDatabase.getDatabaseInstance(it)!!.messageDao()
            ).execute()
        }
    }

    override fun clearKeyboard() {
        binding.keyOutput.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        setInstance(null)
    }
}