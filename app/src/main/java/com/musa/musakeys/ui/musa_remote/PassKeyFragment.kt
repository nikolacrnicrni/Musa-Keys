package com.musa.musakeys.ui.musa_remote


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.musa.musakeys.R
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.remoteConnection.NetworkCommunicationAsync
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.getSharedPreferenceString
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.updateSharedPreferenceBoolean
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.updateSharedPreferenceString
import java.util.Locale


class PassKeyFragment : Fragment() {
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val inflate: View = layoutInflater.inflate(R.layout.fragment_passkey, viewGroup, false)
        val sharedPreferenceString = getSharedPreferenceString(
            requireActivity(), SharedPreferenceEnum.PASS_KEY_SAVED, null as String?
        )
        if (sharedPreferenceString != null) {
            updateSharedPreferenceBoolean(
                requireActivity(),
                SharedPreferenceEnum.IS_TEST_MODE,
                false
            )
            NetworkCommunicationAsync(
                requireActivity(), true, sharedPreferenceString,
                (null as Any?)
            ).execute(*arrayOfNulls<Void>(0))
        }
        val editText = inflate.findViewById<View>(R.id.pass_key) as EditText
        val filters = editText.filters
        val inputFilterArr = arrayOfNulls<InputFilter>(filters.size + 1)
        System.arraycopy(filters, 0, inputFilterArr, 0, filters.size)
        inputFilterArr[filters.size] = AllCaps()
        editText.filters = inputFilterArr
        val button = inflate.findViewById<View>(R.id.connect) as Button
        if (sharedPreferenceString != null) {
            editText.setText(sharedPreferenceString)
        }
        button.setOnClickListener {
            if (editText.text.toString().isEmpty()) {
                Toast.makeText(
                    this@PassKeyFragment.activity, this@PassKeyFragment.requireActivity()
                        .getString(R.string.provide_password), Toast.LENGTH_LONG
                ).show()
                editText.requestFocus()
            } else if (editText.text.toString() == "TESTMODE") {
                updateSharedPreferenceBoolean(
                    this@PassKeyFragment.requireActivity(),
                    SharedPreferenceEnum.IS_TEST_MODE,
                    true
                )
                updateSharedPreferenceString(
                    this@PassKeyFragment.requireActivity(),
                    SharedPreferenceEnum.PASS_KEY_SAVED,
                    null as String?
                )
                val findNavController =
                    findNavController(
                        this@PassKeyFragment.requireActivity(),
                        R.id.nav_host_fragment
                    )
                findNavController.popBackStack()
                findNavController.navigate(R.id.nav_musa_remote_portrait_keypad as Int)
            } else {
                updateSharedPreferenceBoolean(
                    this@PassKeyFragment.requireActivity(),
                    SharedPreferenceEnum.IS_TEST_MODE,
                    false
                )
                NetworkCommunicationAsync(
                    this@PassKeyFragment.requireActivity(),
                    true,
                    editText.text.toString().lowercase(Locale.getDefault())
                        .trim { it <= ' ' },
                    null as Any?
                ).execute(*arrayOfNulls<Void>(0))
            }
        }
        return inflate
    }
}
