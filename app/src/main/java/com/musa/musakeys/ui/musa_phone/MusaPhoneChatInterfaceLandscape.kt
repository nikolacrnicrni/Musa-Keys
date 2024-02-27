package com.musa.musakeys.ui.musa_phone


import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musa.musakeys.R
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
    private var doneKey: ImageView? = null
    private var dushanFont: Typeface? = null
    private var mAdapter: PreviousMessagesAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var shwaReply: EditText? = null
    private var textFromPortraitMode: String? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onResume() {
        super.onResume()
        shwaReply?.post { shwaReply?.requestFocus() }
        Handler(Looper.getMainLooper()).postDelayed({
            (context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.let { clipboardManager ->
                if (clipboardManager.hasPrimaryClip() &&
                    (clipboardManager.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML) == true ||
                            clipboardManager.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true)
                ) {
                    clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()
                        ?.let { saveCopiedText(it) }
                    mRecyclerView?.layoutManager?.scrollToPosition(mAdapter?.itemCount ?: 0)
                }
            }
        }, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            switchToPortraitMode()
        }
        val view = inflater.inflate(R.layout.musa_phone_chat_interface_landscape, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        doneKey = view.findViewById(R.id.done)
        doneKey?.setOnClickListener(this)
        dushanFont = getAppropriateFont(requireActivity())
        textFromPortraitMode = arguments?.getString(MusaConstants.TEXT_FROM_PORTRAIT, "")

        mRecyclerView = (view.findViewById(R.id.recycler_view) as RecyclerView).apply {
            setHasFixedSize(true)
            mLayoutManager = LinearLayoutManager(activity)
            mLayoutManager!!.stackFromEnd = true
            layoutManager = LinearLayoutManager(activity).also { mLayoutManager = it }
            adapter = PreviousMessagesAdapter(requireActivity()).also { mAdapter = it }
        }

        initReplyEditText(view)
        return view
    }

    private fun saveCopiedText(text: String) {
        if (text.isNotEmpty()) {
            val persistablePreviousMessage = PersistablePreviousMessage().apply {
                message = text
                isReceived = true
            }
            InsertEntityAsyncTask(
                requireActivity(),
                persistablePreviousMessage,
                object : EntityPersistenceListener {
                    override fun onEntityPersisted(
                        persistablePreviousMessage: PersistablePreviousMessage?,
                        id: Long
                    ) {
                        mAdapter?.addData(persistablePreviousMessage)
                    }

                    override fun onFailureOccured(asyncResult: AsyncResult?) {
                        asyncResult?.errorMessage?.let {
                            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                getDatabaseInstance(activity)?.messageDao()!!
            ).execute(arrayOfNulls(0))
            Handler(Looper.getMainLooper()).postDelayed({
                mRecyclerView?.layoutManager?.scrollToPosition(mAdapter?.itemCount ?: (0 - 1))
            }, 50)
        }
    }

    private fun initReplyEditText(view: View) {
        shwaReply = view.findViewById(R.id.shwa_reply)
        shwaReply?.apply {
            typeface = dushanFont
            isCursorVisible = true
            textSize = getFontSize(activity).toFloat()
            textFromPortraitMode?.let {
                setText(it)
                setSelection(it.length)
            }
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.done) {
            shwaReply?.text.toString().let { text ->
                if (text.isNotEmpty()) {
                    persistMessageAndClear(text, false)
                } else {
                    startBackgroundServiceAsRequired(requireActivity())
                    mAdapter?.refreshData()
                    (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.inputMethodList?.clear()
                }
            }
        }
    }

    private fun persistMessageAndClear(text: String, isReceived: Boolean) {
        (requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(
            ClipData.newPlainText("", text)
        )
        val message = PersistablePreviousMessage().apply {
            this.message = text
            this.isReceived = isReceived
        }
        InsertEntityAsyncTask(
            requireActivity(),
            message,
            object : EntityPersistenceListener {
                override fun onEntityPersisted(
                    persistablePreviousMessage: PersistablePreviousMessage?,
                    id: Long
                ) {
                    startBackgroundServiceAsRequired(requireActivity())
                    shwaReply?.setText("")
                    KeyboardListener.clearKeyboard()
                    mAdapter?.addData(persistablePreviousMessage)
                }

                override fun onFailureOccured(asyncResult: AsyncResult?) {
                    asyncResult?.errorMessage?.let {
                        Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            getDatabaseInstance(activity)?.messageDao()!!
        ).execute(arrayOfNulls(0))
        Handler(Looper.getMainLooper()).postDelayed({
            mRecyclerView?.layoutManager?.scrollToPosition(mAdapter?.itemCount ?: (0 - 1))
        }, 50)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            switchToPortraitMode()
        }
    }

    private fun switchToPortraitMode() {
        findNavController().popBackStack()
        findNavController().navigate(R.id.nav_musa_phone_portrait_mode, Bundle().apply {
            putString(MusaConstants.TEXT_FROM_LANDSCAPE, shwaReply?.text?.toString() ?: "")
        })
    }
}
