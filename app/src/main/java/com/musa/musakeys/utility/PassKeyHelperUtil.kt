package com.musa.musakeys.utility

import com.musa.musakeys.model.InvalidPassKeyException


object PassKeyHelperUtil {
    @Throws(InvalidPassKeyException::class)
    fun getIpFromPassKey(str: String): String {
        val sb = StringBuilder("")
        var i = 0
        while (i < str.length) {
            try {
                sb.append(java.lang.Long.toHexString((str[i].code - 'k'.code).toLong()))
                i++
            } catch (unused: Exception) {
                throw InvalidPassKeyException("Invalid pass_key $str")
            }
        }
        return getIpFromHexKey(sb.toString(), str)
    }

    @Throws(InvalidPassKeyException::class)
    private fun getIpFromHexKey(str: String, str2: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < str.length) {
            i = try {
                val i2 = i + 2
                sb.append(Integer.valueOf(str.substring(i, i2), 16))
                sb.append(".")
                i2
            } catch (unused: Exception) {
                throw InvalidPassKeyException("Invalid pass_key $str2")
            }
        }
        return if (sb.isNotEmpty()) sb.substring(0, sb.length - 1) else sb.toString()
    }
}
