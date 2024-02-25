package com.musa.musakeys.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.musa.musakeys.R
import com.musa.musakeys.databinding.HelpPageContentBinding

class MusaHelpContent : Fragment() {

    private var binding: HelpPageContentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HelpPageContentBinding.inflate(inflater, container, false)
        val textResId = when (arguments?.getInt("index") ?: -1) {
            1 -> R.string.musa_help_tab_1
            2 -> R.string.musa_help_tab_2
            3 -> R.string.musa_help_tab_3
            4 -> R.string.musa_help_tab_4
            else -> null
        }
        textResId?.let { binding?.helpContent?.setText(it) }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance(index: Int): MusaHelpContent =
            MusaHelpContent().apply {
                arguments = Bundle().apply {
                    putInt("index", index)
                }
            }
    }
}
