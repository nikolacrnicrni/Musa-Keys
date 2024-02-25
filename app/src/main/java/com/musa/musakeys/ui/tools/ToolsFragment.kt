package com.musa.musakeys.ui.tools

import com.musa.musakeys.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders


class ToolsFragment : Fragment() {
    private var toolsViewModel: ToolsViewModel? = null

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        toolsViewModel = ViewModelProviders.of(this as Fragment)
            .get(ToolsViewModel::class.java) as ToolsViewModel
        val inflate: View = layoutInflater.inflate(R.layout.fragment_tools, viewGroup, false)
        val textView = inflate.findViewById<View>(R.id.text_tools) as TextView
        toolsViewModel!!.text.observe(
            viewLifecycleOwner
        ) { str -> textView.text = str }
        return inflate
    }
}
