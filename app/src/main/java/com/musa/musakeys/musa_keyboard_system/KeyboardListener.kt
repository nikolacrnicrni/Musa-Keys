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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musa.musakeys.R
import com.musa.musakeys.asyncTasks.AsyncResult
import com.musa.musakeys.asyncTasks.DeleteEntityAsyncTask
import com.musa.musakeys.asyncTasks.EntityPersistenceListener
import com.musa.musakeys.asyncTasks.InsertEntityAsyncTask
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
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
    TextWatcher {
    companion object {
        var IsTabHit = false
        var backspaceHit = false
        var ic: InputConnection? = null
        var isEnterHit = false
        var isEscapeHit = false
        private var keyOutput: TextView? = null

        fun clearKeyboard() {
            keyOutput?.text = ""
        }
    }

    private var GONE = false
    private var addZWNJ = false
    private lateinit var backspace: ImageView
    private lateinit var backspaceCover: LinearLayout
    private lateinit var buttonList: List<Button>
    private lateinit var childKeysFont: Typeface
    private var computerIconPos = false
    private lateinit var container: LinearLayout
    private lateinit var dushanFont: Typeface
    private lateinit var enterKey: LinearLayout
    private var isFirstClick = true
    private var isNumericLowDigitMode = true
    private var isNumericMode = false
    private var isShiftMode = false
    private var isTestMode = false
    private lateinit var mAdapter: PreviousMessagesAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var numericWithin22 = false
    private lateinit var parentKeysFont: Typeface
    private lateinit var recycleContainer: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var romanKeyboard: ImageView
    private lateinit var romanKeyboardOn: ImageView
    private lateinit var shiftKey: LinearLayout
    private var shiftMode22 = false
    private lateinit var smsLabel: ImageView
    private var t = 0
    private var textFromLandscapeMode = ""
    private var yauLongPressed = false
    private var isHentrax = false
    private val mySharedPreference = MySharedPreference(this)

    override fun onCreate() {
        super.onCreate()
        checkComputerIconPos()

    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        checkComputerIconPos()
    }

    override fun onCreateInputView(): View {
        val inflate = layoutInflater.inflate(R.layout.keyboard_system_view, null)
        isTestMode = SharedPreferenceHelperUtil.getSharedPreferenceBoolean(
            applicationContext,
            SharedPreferenceEnum.IS_TEST_MODE,
            false
        )
        initView(inflate)
        keyOutput?.addTextChangedListener(this)
        updateViews(getParentKeyboard(parentKeysFont))
        Handler(Looper.getMainLooper()).postDelayed({
            lambdaOnCreateInputView0()
        }, 0)
        return inflate
    }

    private fun lambdaOnCreateInputView0() {
        (applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).primaryClip?.let { clip ->
            if (clip.description.hasMimeType("text/html") || clip.description.hasMimeType("text/plain")) {
                saveCopiedText(clip.getItemAt(0).text.toString())
            }
        }
    }

    override fun onClick(view: View) {
        ic = currentInputConnection
        when (view.id) {
            R.id.backspace_cover -> handleBackspace()
            R.id.enter -> handleEnter()
            R.id.roman_keyboard, R.id.roman_keyboard_on -> (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
            R.id.shift -> handleShift()
            R.id.sms_label -> toggleSmsLabel()
            else -> otherKeyboards(view)
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (isFirstClick) processFirstClick(view, true)
        else processSecondClick(view, true)
        return true
    }

    private fun handleBackspace() {
        backspaceHit = true
        if (isFirstClick) {
            val charSequence = keyOutput!!.text.toString()
            if (charSequence.isNotEmpty()) {
                keyOutput!!.text = charSequence.substring(0, charSequence.length - 1)
            }
            backspaceHit = false
        } else {
            shiftMode22 = false
            isFirstClick = true
            backspaceHit = false
            updateViews(getParentKeyboard(parentKeysFont))
            backspace.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.backspace
                )
            )
        }
        val textBeforeCursor = currentInputConnection.getTextBeforeCursor(100, 1)
        if (keyOutput!!.text.toString() == "" && textBeforeCursor.toString() != "") {
            if (TextUtils.isEmpty(currentInputConnection.getSelectedText(0))) {
                currentInputConnection.deleteSurroundingText(1, 0)
                return
            } else {
                currentInputConnection.commitText("", 1)
                return
            }
        } else {
            return
        }
    }

    private fun handleEnter() {
        keyOutput?.append("⏎")
    }

    private fun handleShift() {
        isShiftMode = !isShiftMode
        updateViews(
            if (isShiftMode) getShiftedParentKeys(parentKeysFont) else getParentKeyboard(
                parentKeysFont
            )
        )
    }

    private fun toggleSmsLabel() {
        GONE = !GONE
        if (GONE) {
            romanKeyboardOn.visibility = View.GONE
            container.visibility = View.GONE
            recycleContainer.visibility = View.VISIBLE
            keyOutput?.isEnabled = false
            backspaceCover.visibility = View.GONE
            keyOutput?.visibility = View.GONE
            romanKeyboard.visibility = View.GONE
            smsLabel.setImageResource(R.drawable.ic_musa_keyboard)
        } else {
            recycleContainer.visibility = View.GONE
            container.visibility = View.VISIBLE
            backspaceCover.visibility = View.VISIBLE
            keyOutput?.visibility = View.VISIBLE
            romanKeyboardOn.visibility = View.VISIBLE
            keyOutput?.isEnabled = true
            romanKeyboard.visibility = View.GONE
            smsLabel.setImageResource(R.drawable.ic_sms)
        }
        checkComputerIconPos()
    }

    private fun initView(view: View) {
        dushanFont = FontSelector.getAppropriateFont(applicationContext)!!
        parentKeysFont = FontSelector.getAppropriateFont(applicationContext)!!
        childKeysFont = FontSelector.getAppropriateFont(applicationContext)!!
        buttonList = ArrayList()
        val button = view.findViewById<View>(R.id.parent_14) as Button
        val button2 = view.findViewById<View>(R.id.parent_15) as Button
        val button3 = view.findViewById<View>(R.id.parent_16) as Button
        val button4 = view.findViewById<View>(R.id.parent_17) as Button
        val button5 = view.findViewById<View>(R.id.parent_18) as Button
        val button6 = view.findViewById<View>(R.id.parent_7) as Button
        val button7 = view.findViewById<View>(R.id.parent_20) as Button
        val button8 = view.findViewById<View>(R.id.parent_21) as Button
        val button9 = view.findViewById<View>(R.id.parent_22) as Button
        val button10 = view.findViewById<View>(R.id.parent_23) as Button
        val button11 = view.findViewById<View>(R.id.parent_24) as Button
        val button12 = view.findViewById<View>(R.id.parent_25) as Button
        val button13 = view.findViewById<View>(R.id.parent_26) as Button
        val button14 = view.findViewById<View>(R.id.parent_27) as Button
        backspace =
            (view.findViewById<View>(R.id.backspace) as ImageView)
        backspaceCover =
            (view.findViewById<View>(R.id.backspace_cover) as LinearLayout)
        keyOutput = view.findViewById<View>(R.id.key_output) as TextView
        smsLabel =
            view.findViewById<View>(R.id.sms_label) as ImageView
        romanKeyboard =
            view.findViewById<View>(R.id.roman_keyboard) as ImageView
        romanKeyboardOn =
            view.findViewById<View>(R.id.roman_keyboard_on) as ImageView
        enterKey = (view.findViewById<View>(R.id.enter) as LinearLayout)
        shiftKey = (view.findViewById<View>(R.id.shift) as LinearLayout)
        container = (view.findViewById<View>(R.id.container) as LinearLayout)
        recycleContainer =
            view.findViewById<View>(R.id.recycler_container) as LinearLayout
        recyclerView = (view.findViewById<View>(R.id.recycler_view) as RecyclerView)
        mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = PreviousMessagesAdapter(applicationContext)
        recyclerView.adapter = mAdapter
        enterKey.setOnClickListener(this)
        shiftKey.setOnClickListener(this)
        smsLabel.setOnClickListener(this)
        romanKeyboardOn.setOnClickListener(this)
        romanKeyboard.setOnClickListener(this)
        keyOutput!!.typeface = dushanFont
        keyOutput!!.textSize = FontSizeManager.getFontSize(baseContext).toFloat()
        keyOutput!!.text = textFromLandscapeMode
        backspaceCover.setOnClickListener(this)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_1) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_2) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_3) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_4) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_5) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_6) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_19) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_8) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_9) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_10) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_11) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_12) as Button)
        (buttonList as ArrayList<Button>).add(view.findViewById<View>(R.id.parent_13) as Button)
        (buttonList as ArrayList<Button>).add(button)
        (buttonList as ArrayList<Button>).add(button2)
        (buttonList as ArrayList<Button>).add(button3)
        (buttonList as ArrayList<Button>).add(button4)
        (buttonList as ArrayList<Button>).add(button5)
        (buttonList as ArrayList<Button>).add(button6)
        (buttonList as ArrayList<Button>).add(button7)
        (buttonList as ArrayList<Button>).add(button8)
        (buttonList as ArrayList<Button>).add(button9)
        (buttonList as ArrayList<Button>).add(button10)
        (buttonList as ArrayList<Button>).add(button11)
        (buttonList as ArrayList<Button>).add(button12)
        (buttonList as ArrayList<Button>).add(button13)
        (buttonList as ArrayList<Button>).add(button14)
        for (next in buttonList) {
            next.typeface = parentKeysFont
            next.setOnClickListener(this)
            next.setOnLongClickListener(this)
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
                R.id.parent_1 -> {
                    keyOutput?.text = (keyOutput!!.text.toString() + "⎋")
                    return
                }

                R.id.parent_2 -> {
                    keyOutput?.text = (keyOutput!!.text.toString() + "⇥")
                    return
                }

                R.id.parent_3 -> {
                    keyOutput?.text = (keyOutput!!.text.toString() + "↵")
                    return
                }

                R.id.parent_4 -> {
                    keyOutput?.text = (keyOutput!!.text.toString() + " ")
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
            keyOutput?.text = (keyOutput!!.text.toString() + str)
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
                    keyOutput?.text = keyOutput!!.text.toString() + unicode
                    updateViews(getParentKeyboard(parentKeysFont))
                    backspace.setImageDrawable(
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
            val i4 = t
            if (i4 == 22) {
                updateViews(getShiftedParentKeys(parentKeysFont))
                shiftMode22 = true
            } else {
                updateViews(FormulaUtils.getChildKeysForTopParent(i4, childKeysFont, isHentrax))
            }
            isFirstClick = false
            isShiftMode = false
            backspace.setImageDrawable(
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
        isHentrax = mySharedPreference.getPreferences(MusaConstants.SAVED_FONT).equals("HentraxMusaElement-Regular")
        var str: String
        val str2: String
        var indexOf = FormulaUtils.getSingleKeyboardButtonIds().indexOf(Integer.valueOf(view.id))
        if (z || isShiftMode) {
            indexOf += 27
        }
        val actualText = FormulaUtils.getOutput(t, indexOf, isHentrax).actualText
        if (yauLongPressed && FormulaUtils.getSingleKeyboardButtonIds()
                .indexOf(Integer.valueOf(view.id)) != 15 || addZWNJ && FormulaUtils.getSingleKeyboardButtonIds()
                .indexOf(Integer.valueOf(view.id)) == 17
        ) {
            str2 = Html.fromHtml("&#x200C;").toString()
            yauLongPressed = false
            addZWNJ = false
            str = ""
        } else if (!yauLongPressed || FormulaUtils.getSingleKeyboardButtonIds()
                .indexOf(Integer.valueOf(view.id)) != 15
        ) {
            str2 = ""
            str = str2
        } else {
            val obj = Html.fromHtml("&#x200C;").toString()
            addZWNJ = false
            yauLongPressed = false
            str = obj
            str2 = ""
        }
        if (addZWNJ && FormulaUtils.getSingleKeyboardButtonIds()
                .indexOf(Integer.valueOf(view.id)) == 15
        ) {
            str = Html.fromHtml("&#x200C;").toString()
            addZWNJ = false
        }
        val str3 = str2 + actualText + str
        if ("" != str3) {
            isFirstClick = true
            isShiftMode = false
            keyOutput?.text = keyOutput!!.text.toString() + str3
            updateViews(getParentKeyboard(parentKeysFont))
            backspace.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.backspace
                )
            )
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
        if (!backspaceHit && charSequence.toString() != "" && !isEnterHit && !isEscapeHit && !IsTabHit) {
            if (NetworkCommunicationAsync.IsConnected && !PortraitModeKeypad.isFragmentRunning && !LandscapeModeKeypad.isFragmentRunning) {
                sendData(charSequence[charSequence.length - 1].toString())
            }
            ic!!.setComposingText(charSequence[charSequence.length - 1].toString(), 1)
            ic!!.finishComposingText()
        } else if (isEnterHit) {
            ic!!.setComposingText("\n", 1)
            ic!!.finishComposingText()
            if (NetworkCommunicationAsync.IsConnected && !PortraitModeKeypad.isFragmentRunning && !LandscapeModeKeypad.isFragmentRunning) {
                sendData("⏎")
            }
            isEnterHit = false
        } else if (IsTabHit) {
            if (NetworkCommunicationAsync.IsConnected && !PortraitModeKeypad.isFragmentRunning && !LandscapeModeKeypad.isFragmentRunning) {
                sendData("⇥")
            }
            ic!!.setComposingText("\t", 1)
            ic!!.finishComposingText()
            IsTabHit = false
        } else if (isEscapeHit) {
            ic!!.setComposingText("\u001b", 1)
            ic!!.finishComposingText()
            if (NetworkCommunicationAsync.IsConnected && !PortraitModeKeypad.isFragmentRunning && !LandscapeModeKeypad.isFragmentRunning) {
                sendData("⎋")
            }
            isEscapeHit = false
        } else if (backspaceHit && charSequence != "") {
            if (TextUtils.isEmpty(ic!!.getSelectedText(0))) {
                ic!!.deleteSurroundingText(1, 0)
            } else {
                ic!!.commitText("", 1)
            }
            if (NetworkCommunicationAsync.IsConnected && !PortraitModeKeypad.isFragmentRunning && !LandscapeModeKeypad.isFragmentRunning) {
                sendData("⌫")
            }
        }
    }

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
                recyclerView.layoutManager!!.scrollToPosition(
                    mAdapter.itemCount - 1
                )
            }, 50)
        }
    }


    private fun otherKeyboards(view: View) {
        if (numericWithin22) {
            if (computerIconPos) {
                var i = 0
                while (i < FormulaUtils.getSingleKeyboardButtonIds().size) {
                    if (FormulaUtils.getSingleKeyboardButtonIds()[i] != view.id) {
                        i++
                    } else if (i == 8) {
                        buttonList[8].text = "⬒"
                        buttonList[26].text = ""
                        computerIconPos = true
                        return
                    } else if (i == 17) {
                        numericWithin22 = false
                        isFirstClick = true
                        computerIconPos = false
                        updateViews(getParentKeyboard(parentKeysFont))
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        shiftMode22 = false
                        return
                    } else if (i == 26) {
                        buttonList[8].text = ""
                        buttonList[26].text = "⬓"
                        computerIconPos = false
                        return
                    } else {
                        val textView = keyOutput!!
                        textView.text = keyOutput!!.text.toString() + FormulaUtils.highDigitsMusa[i]
                        return
                    }
                }
            }
            if (computerIconPos && view.id == R.id.parent_9) {
                System.out.printf("nothing", *arrayOfNulls(0))
            } else if (!computerIconPos && view.id == R.id.parent_27) {
                System.out.printf("nothing", *arrayOfNulls(0))
            } else if (!computerIconPos && view.id == R.id.parent_9) {
                buttonList[8].text = "⬒"
                buttonList[26].text = ""
                computerIconPos = true
            } else if (computerIconPos && view.id == R.id.parent_27) {
                buttonList[8].text = ""
                buttonList[26].text = "⬓"
                computerIconPos = false
            } else if (view.id == R.id.parent_18) {
                numericWithin22 = false
                isFirstClick = true
                updateViews(getParentKeyboard(parentKeysFont))
                backspace.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.backspace
                    )
                )
                shiftMode22 = false
            } else {
                for (i2 in FormulaUtils.getSingleKeyboardButtonIds().indices) {
                    if (FormulaUtils.getSingleKeyboardButtonIds()[i2] == view.id) {
                        val childKeysForTopParent = FormulaUtils.getChildKeysForTopParent(
                            7,
                            childKeysFont,
                            isHentrax
                        )
                        val textView2 = keyOutput!!
                        textView2.text =
                            keyOutput!!.text.toString() + childKeysForTopParent!![i2]!!.actualText
                    }
                }
            }
        } else if (shiftMode22) {
            val id = view.id
            if (id == R.id.parent_1) {
                isEnterHit = true
                val textView3 = keyOutput!!
                textView3.text = keyOutput!!.text.toString() + "\n"
                isFirstClick = true
                updateViews(getParentKeyboard(parentKeysFont))
                backspace.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.backspace
                    )
                )
                shiftMode22 = false
            } else if (id == R.id.parent_18) {
                val textView4 = keyOutput!!
                textView4.text = keyOutput!!.text.toString() + FormulaUtils.shiftCharacters[5]
                isFirstClick = true
                updateViews(getParentKeyboard(parentKeysFont))
                backspace.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.backspace
                    )
                )
                shiftMode22 = false
            } else if (id != R.id.parent_2) {
                when (id) {
                    R.id.parent_27 -> {
                        val textView5 = keyOutput!!
                        textView5.text =
                            keyOutput!!.text.toString() + FormulaUtils.shiftCharacters[6]
                        isFirstClick = true
                        updateViews(getParentKeyboard(parentKeysFont))
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        shiftMode22 = false
                        return
                    }

                    R.id.parent_3 -> {
                        isEscapeHit = true
                        val textView6 = keyOutput!!
                        textView6.text = keyOutput!!.text.toString() + "\u001b"
                        isFirstClick = true
                        updateViews(getParentKeyboard(parentKeysFont))
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        shiftMode22 = false
                        return
                    }

                    R.id.parent_4 -> {
                        IsTabHit = true
                        isFirstClick = true
                        val textView7 = keyOutput!!
                        textView7.text = keyOutput!!.text.toString() + "\t"
                        updateViews(getParentKeyboard(parentKeysFont))
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        shiftMode22 = false
                        return
                    }

                    R.id.parent_5 -> {
                        val textView8 = keyOutput!!
                        textView8.text = keyOutput!!.text.toString() + "‌"
                        isFirstClick = true
                        updateViews(getParentKeyboard(parentKeysFont))
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        shiftMode22 = false
                        return
                    }

                    R.id.parent_6 -> {
                        val textView9 = keyOutput!!
                        textView9.text = keyOutput!!.text.toString() + "‍"
                        isFirstClick = true
                        updateViews(getParentKeyboard(parentKeysFont))
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        shiftMode22 = false
                        return
                    }

                    R.id.parent_7 -> {
                        updateViews(FormulaUtils.getNumericCharacters(parentKeysFont))
                        numericWithin22 = true
                        isFirstClick = true
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        return
                    }

                    R.id.parent_8 -> {
                        val textView10 = keyOutput!!
                        textView10.text =
                            keyOutput!!.text.toString() + FormulaUtils.shiftCharacters[3]
                        isFirstClick = true
                        updateViews(getParentKeyboard(parentKeysFont))
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        shiftMode22 = false
                        return
                    }

                    R.id.parent_9 -> {
                        val textView11 = keyOutput!!
                        textView11.text =
                            keyOutput!!.text.toString() + FormulaUtils.shiftCharacters[4]
                        isFirstClick = true
                        updateViews(getParentKeyboard(parentKeysFont))
                        backspace.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.backspace
                            )
                        )
                        shiftMode22 = false
                        return
                    }

                    else -> return
                }
            } else {
                val textView12 = keyOutput!!
                textView12.text = keyOutput!!.text.toString() + " "
                isFirstClick = true
                updateViews(getParentKeyboard(parentKeysFont))
                backspace.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.backspace
                    )
                )
                shiftMode22 = false
            }
        } else if (isFirstClick) {
            processFirstClick(view, false)
        } else {
            processSecondClick(view, false)
        }
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
        val textView = keyOutput
        if (textView != null) {
            textView.text = ""
        }
    }
}