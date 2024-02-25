package com.musa.musakeys.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.musa.musakeys.R
import com.musa.musakeys.databinding.HelpLayoutBinding

class HelpFragment : Fragment() {

    private var _binding: HelpLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var helpPagesAdapter: HelpPagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HelpLayoutBinding.inflate(inflater, container, false)
        helpPagesAdapter = HelpPagesAdapter(this)
        binding.viewPager.adapter = helpPagesAdapter

        // Setup TabLayout with ViewPager2
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getPageTitle(position)
        }.attach()

        return binding.root
    }

    private fun getPageTitle(position: Int): String? {
        return when (position) {
            0 -> getString(R.string.agent_app)
            1 -> getString(R.string.connecting_to_agent)
            2 -> getString(R.string.use_app_with_computer)
            else -> getString(R.string.use_app_with_phone)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
