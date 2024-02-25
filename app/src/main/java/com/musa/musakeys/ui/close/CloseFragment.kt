package com.musa.musakeys.ui.close

import com.musa.musakeys.R
import android.content.Intent
import android.os.Bundle
import android.text.method.TransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.services.OrientationListenerRegistrationService
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.updateSharedPreferenceBoolean


class CloseFragment : Fragment() {
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val inflate: View = layoutInflater.inflate(
            R.layout.activity_unregister_orientation_listener,
            viewGroup,
            false
        )
        val button = inflate.findViewById<View>(R.id.unregister_broadcast_listener) as Button
        button.transformationMethod = null as TransformationMethod?
        button.setOnClickListener {
            this@CloseFragment.activity?.let { it1 ->
                updateSharedPreferenceBoolean(
                    it1,
                    SharedPreferenceEnum.ENABLE_BACKGROUND_SERVICE,
                    false
                )
            }
            val intent = Intent(
                this@CloseFragment.activity,
                OrientationListenerRegistrationService::class.java
            )
            intent.action = MusaConstants.STOP_FOREGROUND_ACTION
            this@CloseFragment.activity?.startService(intent)
            Toast.makeText(
                this@CloseFragment.activity,
                this@CloseFragment.activity?.getString(R.string.background_service_stopped),
                Toast.LENGTH_SHORT
            ).show()
        }
        return inflate
    }
}
