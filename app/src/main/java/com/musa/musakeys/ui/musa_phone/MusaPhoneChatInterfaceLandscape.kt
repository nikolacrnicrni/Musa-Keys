package com.musa.musakeys.ui.musa_phone


import com.musa.musakeys.R
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musa.musakeys.asyncTasks.AsyncResult
import com.musa.musakeys.asyncTasks.EntityPersistenceListener
import com.musa.musakeys.asyncTasks.InsertEntityAsyncTask
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.db.InstantiateDatabase.getDatabaseInstance
import com.musa.musakeys.db.PersistablePreviousMessage
import com.musa.musakeys.db.PreviousMessagesAdapter
import com.musa.musakeys.musa_keyboard_system.KeyboardListener
import com.musa.musakeys.utility.FontSelector.getAppropriateFont
import com.musa.musakeys.utility.FontSizeManager.getFontSize
import com.musa.musakeys.utility.MusaServiceUtility.startBackgroundServiceAsRequired


class MusaPhoneChatInterfaceLandscape : Fragment(), View.OnClickListener {
    private val buttonList: List<Button>? = null
    private var doneKey: ImageView? = null
    private var dushanFont: Typeface? = null
    private val isFirstClick = true
    private val isNumericLowDigitMode = true

    /* access modifiers changed from: private */
    var mAdapter: PreviousMessagesAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    /* access modifiers changed from: private */
    var mRecyclerView: RecyclerView? = null

    /* access modifiers changed from: private */
    var shwaReply: EditText? = null
    var t = 0
    private var textFromPortraitMode: String? = null

    @SuppressLint("UseRequireInsteadOfGet", "WrongConstant")
    override fun onResume() {
        super.onResume()
        shwaReply!!.post { shwaReply!!.requestFocus() }
        Handler().postDelayed(Runnable {
            val clipboardManager = this@MusaPhoneChatInterfaceLandscape.context!!
                .getSystemService("android.content.Context.CLIPBOARD_SERVICE") as ClipboardManager
            if (!clipboardManager.hasPrimaryClip()) {
                return@Runnable
            }
            if (clipboardManager.primaryClipDescription!!.hasMimeType("text/html") || clipboardManager.primaryClipDescription!!
                    .hasMimeType("text/plain")
            ) {
                saveCopiedText(
                    clipboardManager.primaryClip!!.getItemAt(0).text.toString()
                )
                mRecyclerView!!.layoutManager!!.scrollToPosition(
                    mAdapter!!.itemCount
                )
            }
        }, 0)
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        Log.e("FocusChanged", "Focus changed Mohit")
        if (resources.configuration.orientation == 1) {
            switchToPortraitMode()
        }
        val inflate: View =
            layoutInflater.inflate(R.layout.musa_phone_chat_interface_landscape, viewGroup, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        doneKey = inflate.findViewById<View>(R.id.done) as ImageView
        doneKey!!.setOnClickListener(this)
        dushanFont = getAppropriateFont(requireActivity())
        var str: String? = ""
        if (arguments != null) {
            str = requireArguments().getString(MusaConstants.TEXT_FROM_PORTRAIT, str)
        }
        textFromPortraitMode = str
        initGenericView(inflate)
        initReplyEditText(inflate, textFromPortraitMode)
        return inflate
    }

    /* access modifiers changed from: private */
    fun saveCopiedText(str: String?) {
        if (str != null) {
            if (str.isNotEmpty()) {
                val persistablePreviousMessage = PersistablePreviousMessage()
                persistablePreviousMessage.message = str
                persistablePreviousMessage.isReceived = true
                InsertEntityAsyncTask(
                    requireActivity(),
                    persistablePreviousMessage,
                    object : EntityPersistenceListener {
                        override fun onEntityPersisted(
                            persistablePreviousMessage: PersistablePreviousMessage?,
                            j: Long
                        ) {
                            mAdapter!!.addData(persistablePreviousMessage)
                        }

                        override fun onFailureOccured(asyncResult: AsyncResult?) {
                            if (asyncResult != null) {
                                Toast.makeText(
                                    this@MusaPhoneChatInterfaceLandscape.activity,
                                    asyncResult.errorMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    getDatabaseInstance(activity)!!.messageDao()!!
                ).execute(arrayOfNulls(0))
                Handler(Looper.getMainLooper()).postDelayed({
                    mRecyclerView!!.layoutManager!!.scrollToPosition(
                        mAdapter!!.itemCount - 1
                    )
                }, 50)
            }
        }
    }

    private fun initReplyEditText(view: View, str: String?) {
        shwaReply = view.findViewById<View>(R.id.shwa_reply) as EditText
        shwaReply!!.setTypeface(dushanFont)
        shwaReply!!.isCursorVisible = true
        shwaReply!!.textSize = getFontSize(activity).toFloat()
        if (textFromPortraitMode?.isNotEmpty() == true) {
            shwaReply!!.setText(textFromPortraitMode)
            shwaReply!!.setSelection(textFromPortraitMode!!.length)
        }
    }

    private fun initGenericView(view: View) {
        mRecyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(activity)
        mLayoutManager!!.stackFromEnd = true
        mRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = PreviousMessagesAdapter(requireActivity())
        mRecyclerView!!.adapter = mAdapter
    }

    @SuppressLint("WrongConstant")
    override fun onClick(view: View) {
        if (view.id == R.id.done) {
            val obj = shwaReply!!.text.toString()
            if (obj.isNotEmpty()) {
                (requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(
                    ClipData.newPlainText("", obj)
                )
                val persistablePreviousMessage = PersistablePreviousMessage()
                persistablePreviousMessage.message = obj
                persistablePreviousMessage.isReceived = false
                InsertEntityAsyncTask(
                    requireActivity(),
                    persistablePreviousMessage,
                    object : EntityPersistenceListener {
                        override fun onEntityPersisted(
                            persistablePreviousMessage: PersistablePreviousMessage?,
                            j: Long
                        ) {
                            startBackgroundServiceAsRequired(this@MusaPhoneChatInterfaceLandscape.requireActivity())
                            shwaReply!!.setText("")
                            KeyboardListener.clearKeyboard()
                            mAdapter!!.addData(persistablePreviousMessage)
                        }

                        override fun onFailureOccured(asyncResult: AsyncResult?) {
                            if (asyncResult != null) {
                                Toast.makeText(
                                    this@MusaPhoneChatInterfaceLandscape.activity,
                                    asyncResult.errorMessage,
                                    1
                                ).show()
                            }
                        }
                    },
                    getDatabaseInstance(activity)!!.messageDao()!!
                ).execute(arrayOfNulls(0))
            } else {
                startBackgroundServiceAsRequired(requireActivity())
                mAdapter!!.refreshData()
                if (requireActivity().currentFocus != null) {
                    (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).inputMethodList.clear()
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                mRecyclerView!!.layoutManager!!.scrollToPosition(
                    mAdapter!!.itemCount - 1
                )
            }, 50)
        }
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)
        if (resources.configuration.orientation == 1) {
            switchToPortraitMode()
        }
    }

    private fun switchToPortraitMode() {
        val findNavController = findNavController(requireActivity(), R.id.nav_host_fragment)
        findNavController.popBackStack()
        val bundle = Bundle()
        val editText = shwaReply
        bundle.putString(MusaConstants.TEXT_FROM_LANDSCAPE, editText?.text?.toString() ?: "")
        findNavController.navigate(R.id.nav_musa_phone_portrait_mode as Int, bundle)
    }

    fun onCreateInputConnection(editorInfo: EditorInfo) {
        editorInfo.imeOptions = 268435456
    }
}
