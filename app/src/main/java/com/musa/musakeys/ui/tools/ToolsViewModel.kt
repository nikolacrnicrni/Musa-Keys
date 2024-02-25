package com.musa.musakeys.ui.tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ToolsViewModel : ViewModel() {
    private val mText = MutableLiveData<String>()

    init {
        mText.value = "This is tools fragment"
    }

    val text: LiveData<String>
        get() = mText
}
