package com.musa.musakeys.db

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.musa.musakeys.R

import com.musa.musakeys.asyncTasks.DataAvailabilityListener
import com.musa.musakeys.asyncTasks.GetMessagesAsyncTask
import com.musa.musakeys.utility.FontSelector.getAppropriateFont
import com.musa.musakeys.utility.FontSizeManager.getFontSize


class PreviousMessagesAdapter(private val context: Context) :
    RecyclerView.Adapter<PreviousMessagesAdapter.ViewHolder>() {
    private var font: Typeface? = null
    private val fontSizeSaved: Int = getFontSize(context)
    private val previousMessages: MutableList<PersistablePreviousMessage?> = ArrayList()

    init {
        refreshData()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: CardView
        var chatBackground: LinearLayout
        var previousMessage: TextView

        init {
            cardView = view.findViewById<View>(R.id.card_view) as CardView
            chatBackground = view.findViewById<View>(R.id.chat_background) as LinearLayout
            previousMessage = view.findViewById<View>(R.id.previous_chat_text) as TextView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val inflate: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.previous_chat_card_view, viewGroup, false)
        font = getAppropriateFont(context)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.previousMessage.text = previousMessages[i]!!.message
        viewHolder.previousMessage.setTextSize(2, fontSizeSaved.toFloat())
        viewHolder.previousMessage.typeface = font
        if (previousMessages[i]!!.isReceived) {
            viewHolder.chatBackground.gravity = 3
            viewHolder.previousMessage.gravity = 3
            viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFF8D"))
            return
        }
        viewHolder.chatBackground.gravity = 5
        viewHolder.previousMessage.gravity = 3
        viewHolder.cardView.setCardBackgroundColor(-1)
    }

    override fun getItemCount(): Int {
        val list: List<PersistablePreviousMessage?> = previousMessages
        return list.size
    }

    fun refreshData() {
        GetMessagesAsyncTask(context, object : DataAvailabilityListener {
            override fun onSingleResultAvailable(persistablePreviousMessage: PersistablePreviousMessage?) {}
            override fun onDataSetAvailable(list: List<PersistablePreviousMessage?>?) {
                setData(list)
            }
        }).execute(*arrayOfNulls<Void>(0))
    }

    fun setData(list: List<PersistablePreviousMessage?>?) {
        if (list != null) {
            previousMessages.clear()
            previousMessages.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun addData(persistablePreviousMessage: PersistablePreviousMessage?) {
        if (persistablePreviousMessage != null) {
            previousMessages.add(persistablePreviousMessage)
            notifyDataSetChanged()
        }
    }
}
