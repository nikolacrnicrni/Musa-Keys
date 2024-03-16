package com.musa.musakeys.utility


enum class AsciiChars(val index: Int, val unicode: String) {
    ESC(1, "⎋"), TAB(2, "⇥"), LF(3, "↵"), SPACE(4, " ");

    companion object {
        fun getAsciiCharForIndex(i: Int): AsciiChars? {
            for (asciiChars in entries) {
                if (asciiChars.index == i) {
                    return asciiChars
                }
            }
            return null
        }
    }
}
