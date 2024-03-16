package com.musa.musakeys.ui.musa_phone

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
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
import com.musa.musakeys.db.InstantiateDatabase
import com.musa.musakeys.db.PersistablePreviousMessage
import com.musa.musakeys.db.PreviousMessagesAdapter
import com.musa.musakeys.musa_keyboard_system.KeyboardListener
import com.musa.musakeys.utility.FontSelector
import com.musa.musakeys.utility.FontSizeManager
import com.musa.musakeys.utility.MusaServiceUtility
import com.musa.musakeys.utility.MusaServiceUtility.startBackgroundServiceAsRequired

class MusaPhoneChatInterfacePortrait : Fragment(), View.OnClickListener {
    private lateinit var doneKey: ImageView
    private lateinit var mAdapter: PreviousMessagesAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var shwaReply: EditText
    private var textFromLandscape: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switchToLandscapeMode()
        }

        val view = inflater.inflate(R.layout.musa_phone_chat_interface_portrait, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        val appropriateFont = FontSelector.getAppropriateFont(requireActivity())
        textFromLandscape = arguments?.getString(MusaConstants.TEXT_FROM_LANDSCAPE)

        shwaReply = view.findViewById(R.id.shwa_reply)
        doneKey = view.findViewById(R.id.done)

        with(shwaReply) {
            isCursorVisible = true
            setTypeface(appropriateFont)
            textFromLandscape?.let {
                setText(it)
                setSelection(length())
            }
            textSize = FontSizeManager.getFontSize(activity).toFloat()
        }

        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        mRecyclerView = view.findViewById(R.id.recycler_view)
        with(mRecyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity).apply { stackFromEnd = true }
            mAdapter = PreviousMessagesAdapter(requireActivity())
            adapter = mAdapter
        }

        doneKey.setOnClickListener(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        shwaReply.requestFocus()
        Handler(Looper.getMainLooper()).postDelayed({ checkClipboard() }, 0)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switchToLandscapeMode()
        }
    }

    private fun switchToLandscapeMode() {
        findNavController().popBackStack()
        val bundle = Bundle().apply {
            putString(MusaConstants.TEXT_FROM_PORTRAIT, shwaReply.text.toString())
        }
        findNavController().navigate(R.id.nav_musa_phone_landscape_mode, bundle)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.done) {
            shwaReply.text.toString().let { obj ->
                if (obj.isNotEmpty()) {
                    saveCopiedText(obj)
                    storeData(obj)
                    insertMessage(obj, false)
                } else {
                    startBackgroundServiceAsRequired(requireActivity())
                    mAdapter.refreshData()
                }
                scrollToBottom()
            }
        }
    }

    private fun saveCopiedText(text: String) {
        val clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", text))
    }

    private fun insertMessage(text: String, isReceived: Boolean) {
        PersistablePreviousMessage().apply {
            message = text
            this.isReceived = isReceived
        }.also { message ->
            InstantiateDatabase.getDatabaseInstance(activity)?.let {
                InsertEntityAsyncTask(
                    requireActivity(),
                    message,
                    object : EntityPersistenceListener {
                        override fun onEntityPersisted(
                            persistablePreviousMessage: PersistablePreviousMessage?, id: Long
                        ) {
                            startBackgroundServiceAsRequired(requireActivity())
                            if (!isReceived) shwaReply.setText("")
                            KeyboardListener.getInstance()?.clearKeyboard()
                            mAdapter.addData(persistablePreviousMessage)
                        }

                        override fun onFailureOccured(asyncResult: AsyncResult?) {
                            asyncResult?.let {
                                Toast.makeText(activity, it.errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }

                    },
                    it.messageDao()
                ).execute(arrayOfNulls(0))
            }
        }
    }

    private fun scrollToBottom() {
        Handler(Looper.getMainLooper()).postDelayed({
            mRecyclerView.layoutManager?.scrollToPosition(mAdapter.itemCount - 1)
        }, 50)
    }

    private fun storeData(text: String) {
        requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE).edit().apply {
            putString(MusaConstants.LAST_COPIED_TEXT, text)
            apply()
        }
    }

    private fun checkClipboard() {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()?.let { text ->
            if (text.isNotEmpty() && clipboardManager.primaryClipDescription?.hasMimeType(
                    ClipDescription.MIMETYPE_TEXT_PLAIN
                ) == true
            ) {
                saveCopiedText(text)
                insertMessage(text, true)
            }
        }
    }
}
