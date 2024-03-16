package com.musa.musakeys.ui.font_fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.musa.musakeys.R
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.utility.MySharedPreference

class MyFontsAdapter(
    private val context: Context,
    private val fontMap: LinkedHashMap<String?, String?>,
    private val mClickListener: ItemClickListener
) : RecyclerView.Adapter<MyFontsAdapter.ViewHolder>() {

    private val keysLabels = ArrayList(MusaConstants.fontLabsMap.keys)
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mySharedPreference: MySharedPreference? = null

    interface ItemClickListener {
        fun onItemClick(textView: TextView?, textView2: TextView?, i: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.font_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {
        if (i < keysLabels.size) {
            mySharedPreference = MySharedPreference(context)
            val textView = viewHolder.itemView.findViewById<View>(R.id.fontName) as TextView
            val textView2 = viewHolder.itemView.findViewById<View>(R.id.musaText) as TextView
            val preferences = mySharedPreference!!.getPreferences(MusaConstants.FONTS_DOWNLOADED)
            if (preferences == "true") {
                setDownloaded(true)
            }
            if (mySharedPreference!!.getPreferences(MusaConstants.FONT_INDEX) == i.toString()) {
                textView2.setTextColor(context.resources.getColor(R.color.titleColor))
                textView.setTextColor(context.resources.getColor(R.color.titleColor))
            } else {
                textView.setTextColor(ViewCompat.MEASURED_STATE_MASK)
                textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK)
            }
            val arrayList2: ArrayList<*> = ArrayList<Any?>(MusaConstants.mapOfFontUris.keys)
            val assets = context.assets
            textView2.typeface = Typeface.createFromAsset(assets, arrayList2[i] as String + ".otf")
            textView2.text = MusaConstants.fontLabsMap[keysLabels[i]]
            textView.text = keysLabels[i]
            if (getDownloaded()) {
                (viewHolder.itemView.findViewById<View>(R.id.downloaded) as ImageView).visibility =
                    View.VISIBLE
            }
            viewHolder.itemView.setOnClickListener {
                handleFontItemClick(i, textView, textView2)
            }
        }
    }

    private fun handleFontItemClick(
        position: Int,
        textView: TextView?,
        textView2: TextView?
    ) {

        val fontName = fontMap.keys.elementAt(position)
        mySharedPreference?.setPreferences(MusaConstants.FONT_INDEX, position.toString())
        if (fontName != null) {
            mySharedPreference?.setPreferences(MusaConstants.SAVED_FONT, fontName)
        }
        notifyDataSetChanged()
        mClickListener.onItemClick(textView, textView2, position)

    }

    override fun getItemCount(): Int {
        return fontMap.keys.size
    }

    inner class ViewHolder internal constructor(view: View?) : RecyclerView.ViewHolder(
        view!!
    )

    companion object {
        private var downloaded = false
        fun setDownloaded(bool: Boolean) {
            downloaded = bool
        }

        fun getDownloaded(): Boolean {
            return downloaded
        }
    }
}
