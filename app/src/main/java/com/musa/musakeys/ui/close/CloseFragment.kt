package com.musa.musakeys.ui.close

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.musa.musakeys.R
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.databinding.ActivityUnregisterOrientationListenerBinding
import com.musa.musakeys.services.OrientationListenerRegistrationService
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.updateSharedPreferenceBoolean

class CloseFragment : Fragment() {

    private var _binding: ActivityUnregisterOrientationListenerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityUnregisterOrientationListenerBinding.inflate(inflater, container, false)

        binding.unregisterBroadcastListener.transformationMethod = null
        binding.unregisterBroadcastListener.setOnClickListener {
            updateSharedPreferenceBoolean(
                requireContext(),
                SharedPreferenceEnum.ENABLE_BACKGROUND_SERVICE,
                false
            )

            Intent(
                requireContext(),
                OrientationListenerRegistrationService::class.java
            ).also { intent ->
                intent.action = MusaConstants.STOP_FOREGROUND_ACTION
                requireContext().startService(intent)
            }

            Toast.makeText(
                requireContext(),
                getString(R.string.background_service_stopped),
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
