package com.musa.musakeys.utility

import android.content.Context
import android.graphics.Typeface
import android.os.Environment
import android.util.Log
import com.musa.musakeys.constants.MusaConstants
import java.io.File

object FontSelector {
    fun getAppropriateFont(context: Context): Typeface? {
        val mySharedPreference = MySharedPreference(context)
        return if (mySharedPreference.getPreferences(MusaConstants.SAVED_FONT).equals("")) {
            Typeface.createFromAsset(context.assets, "DushanMusaAlphabet-Regular.otf")
        } else mySharedPreference.getPreferences(MusaConstants.SAVED_FONT)
            ?.let { getFontByTitle(context, it) }
    }

    private fun getFontByTitle(context: Context, str: String): Typeface {
        val externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        val file = File(externalStoragePublicDirectory, "/$str.otf")
        try {
            if (file.exists()) {
                return Typeface.createFromFile(file)
            }
        } catch (e: RuntimeException) {
            Log.e("getFontByTitle", "Failed to load Typeface from file: ${file.path}", e)
        }

        val assets = context.assets
        return Typeface.createFromAsset(assets, "$str.otf")
    }
}
