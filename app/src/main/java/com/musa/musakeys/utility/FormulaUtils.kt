package com.musa.musakeys.utility

import android.graphics.Typeface
import com.google.common.collect.Lists
import com.musa.musakeys.R
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.MusaConstants.accents
import com.musa.musakeys.constants.MusaConstants.keyNumber
import com.musa.musakeys.constants.MusaConstants.vowels
import com.musa.musakeys.model.MusaTextMetadata

object FormulaUtils {
    val NUMERIC_HIGH_MODE_KEY = getFormattedHexValue(11026)
    val NUMERIC_LOW_MODE_KEY = getFormattedHexValue(11027)
    val PARENT_KEY_ORIGINAL_HIGH_MODE = getFormattedHexValue(57348)
    val PARENT_KEY_ORIGINAL_LOW_MODE = getFormattedHexValue(57354)

    val highDigitsMusa = listOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")

    private val numericHighDigits = listOf(57440, 57409, 57410, 57411, 57408, 57488, 57568, 57544, 57520, 57592, 57472, 57576, 57552, 57528, 57512, 57432, 57584, 57560, 57536, 57600, 57504, 57409, 57410, 57411, 57408, 57488, 57488, 57472, 57496, 57592, 57472, 57472, 57464, 57488, 57448, 57456, 57480, 57456, 57480, 57424)
    private val numericKeysParent = listOf(57363, 57348, 57351, 11027, 57345, 57377, 57401, 57396, 57387, 57406, 57370, 57402, 57397, 57392, 57385, 57360, 57403, 57398, 57393, 57357, 57384, 9099, 8677, 8629, 9141, 57377, 57377, 57370, 57382, 57406, 57370, 57370, 57368, 57377, 57364, 57366, 57375, 57366, 57375, 57357)
    private val numericLowDigits = listOf(57444, 57409, 57410, 57411, 57408, 57492, 57572, 57548, 57524, 57596, 57476, 57580, 57556, 57532, 57516, 57436, 57588, 57564, 57540, 57600, 57508, 57409, 57410, 57411, 57408, 57492, 57492, 57476, 57500, 57596, 57476, 57476, 57468, 57492, 57452, 57460, 57484, 57460, 57484, 57428)
    private val numericModeColorCodesBackground = listOf(
        "#400080", "#808080", "#808080", "#808080", "#808080", "#804000", "#8080FF", "#40FF40", "#FF8080",
        "#808080", "#008000", "#A040FF", "#40FFFF", "#FFA040", "#FFFFFF", "#800080", "#FF40FF", "#40A0FF",
        "#FFFF40", "#404040", "#000000", "#808080", "#808080", "#808080", "#808080", "#804000", "#804000",
        "#008000", "#800000", "#808080", "#008000", "#008000", "#008080", "#804000", "#0000C0", "#004080",
        "#808000", "#004080", "#808000", "#404040"
    )

    private val numericModeColorCodesForeGround = listOf(
        "#ffffff", "#000080", "#000080", "#000080", "#000080", "#ffffff", "#000080", "#000080", "#000080",
        "#000080", "#ffffff", "#000080", "#000080", "#000080", "#000080", "#ffffff", "#000080", "#000080",
        "#000080", "#ffffff", "#ffffff", "#000080", "#000080", "#000080", "#000080", "#ffffff", "#ffffff",
        "#ffffff", "#ffffff", "#000080", "#ffffff", "#ffffff", "#ffffff", "#ffffff", "#ffffff", "#ffffff",
        "#ffffff", "#ffffff", "#ffffff", "#ffffff"
    )

    val shiftCharacters = listOf("ZWNJ", "ZWJ", "#", "", "", "", "")

    fun getNumericKeysParent(typeface: Typeface): List<MusaTextMetadata?> {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i in 0..26) {
            val builder = MusaTextMetadata.builder()
            builder.withDisplaySize(14)
            builder.withTextFont(typeface)
            builder.withDisplaySize(24)
            builder.withActualText(getFormattedHexValue(numericKeysParent[i]))
            builder.withDisplayText(getFormattedHexValue(numericKeysParent[i]))
            builder.withBackgroundColor(numericModeColorCodesBackground[i])
            builder.withForegroundColor(numericModeColorCodesForeGround[i])
            newArrayList.add(builder.build())
        }
        return newArrayList
    }

    fun getNumericLowDigits(): List<MusaTextMetadata?> {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i in 0..53) {
            val builder = MusaTextMetadata.builder()
            builder.withActualText(getFormattedHexValue(numericLowDigits[i]))
            builder.withDisplaySize(24)
            builder.withDisplayText(getFormattedHexValue(numericLowDigits[i]))
            newArrayList.add(builder.build())
        }
        return newArrayList
    }

    fun getNumericHighDigits(): List<MusaTextMetadata?> {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i in 0..53) {
            val builder = MusaTextMetadata.builder()
            builder.withActualText(getFormattedHexValue(numericHighDigits[i]))
            builder.withDisplaySize(24)
            builder.withDisplayText(getFormattedHexValue(numericHighDigits[i]))
            newArrayList.add(builder.build())
        }
        return newArrayList
    }

    fun getShortcut(topkey: Int, typeface: Typeface): List<MusaTextMetadata?> {
        val newArrayList: ArrayList<MusaTextMetadata> = arrayListOf()
        for (i2 in 0..26) {
            if (isShortcut(topkey, i2)) {
                when (getCodePoint(topkey, i2).build().actualText) {
                    "\uE13B" -> newArrayList.add(createMetadata("\u000A", typeface)) // line feed
                    "\uE13C" -> newArrayList.add(createMetadata("\u0020", typeface)) // space
                    "\uE13D" -> newArrayList.add(createMetadata("\u001B", typeface)) // escape
                    "\uE141" -> newArrayList.add(createMetadata("\u0009", typeface)) // tab
                    "\uE130" -> newArrayList.add(createMetadata("\u200C", typeface)) // zwnj
                    "\uE131" -> newArrayList.add(createMetadata("\u200D", typeface)) // zwj
                    "\uE12D" -> {
                        // Handling for numeric, which is a mode change rather than a character insert
                        // This may require handling outside this method since it's a state change
                    }

                    "\uE12E" -> newArrayList.add(
                        createMetadata(
                            "\u0020\u0020\u0020\u0020\u0020\u0020\uE12E\uE040",
                            typeface
                        )
                    ) // paragraph
                    else -> if (getCodePoint(
                            topkey,
                            i2
                        ).build().actualText == ""
                    ) return newArrayList // invalid bottom
                    // do nothing for default case
                }
            }
        }
        return newArrayList
    }

    private fun createMetadata(text: String, typeface: Typeface): MusaTextMetadata {
        return MusaTextMetadata.builder()
            .withActualText(text)
            .withDisplayText(text)
            .withTextFont(typeface)
            .build()
    }

    fun getNumericCharacters(typeface: Typeface): List<MusaTextMetadata?> {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i in 0..26) {
            if (i != 26) {
                val builder = MusaTextMetadata.builder()
                builder.withTextFont(typeface)
                builder.withDisplaySize(24)
                builder.withActualText(getFormattedHexValue(MusaConstants.keyshape[i]))
                builder.withDisplayText(getFormattedHexValue(MusaConstants.keyshape[i]))
                newArrayList.add(builder.build())
            } else {
                val builder2 = MusaTextMetadata.builder()
                builder2.withTextFont(typeface)
                builder2.withDisplaySize(24)
                builder2.withActualText("⬓")
                builder2.withDisplayText("⬓")
                newArrayList.add(builder2.build())
            }
        }
        return newArrayList
    }

    fun getParentKeyboard(typeface: Typeface): List<MusaTextMetadata?> {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        val builder = MusaTextMetadata.builder()
        for (i in 0..26) {
            builder.withTextFont(typeface)
            builder.withDisplaySize(24)
            builder.withActualText(getFormattedHexValue(MusaConstants.keyshape[i]))
            builder.withDisplayText(getFormattedHexValue(MusaConstants.keyshape[i]))
            newArrayList.add(builder.build())
        }
        return newArrayList
    }

    fun getChildKeysForTopParent(i: Int, typeface: Typeface, isHentrax: Boolean): List<MusaTextMetadata?> {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i2 in 0..26) {
            val codePoint = getCodePoint(i, i2, isHentrax)
            codePoint.withTextFont(typeface)
            codePoint.withDisplaySize(24)
            newArrayList.add(codePoint.build())
        }
        return newArrayList
    }

    fun getOutput(i: Int, i2: Int, isHentrax: Boolean): MusaTextMetadata {
        return getCodePoint(i, i2, isHentrax).build()
    }

    private fun isShortcut(t: Int, b: Int): Boolean {
        if (keyNumber[t] < 0 || keyNumber[b] < 0) {
            return false
        }
        val shortcuts = intArrayOf(0xE12D, 0xE12E, 0xE130, 0xE131, 0xE13B, 0xE13C, 0xE13D, 0xE141)
        val c = 0xE100 + (22 * keyNumber[t]) + keyNumber[b]
        return shortcuts.contains(c)
    }

    private fun getCodePoint(t: Int, b: Int, isHentrax: Boolean = false): MusaTextMetadata.Builder {
        val builder = MusaTextMetadata.builder().withDisplaySize(24)
        var codePoint: Int

        if (accents[t] < 0) {
            if (accents[b] < 0) {
                codePoint = 0xE100 + (22 * vowels[t]) + vowels[b]
                if (MusaConstants.invalidLetter.contains(codePoint) && !isHentrax) {
                    codePoint = 0
                }
            } else {
                codePoint = 0xE050 + (0x8 * vowels[t]) + accents[b]
            }
        } else {
            codePoint = if (accents[b] < 0) {
                0xE054 + (0x8 * vowels[b]) + accents[t]
            } else {
                0xE040 + (0x4 * accents[t]) + accents[b]
            }
        }

        val str = getFormattedHexValue(codePoint, isHentrax)
        return builder.withDisplayText(str).withActualText(str)
    }

    private fun getFormattedHexValue(num: Int?, isHentrax: Boolean = false): String {
        if (num == null || (num in MusaConstants.invalidLetter && !isHentrax)) return ""

        val char = num.toChar().toString()
        return if (char == "\u0000") "" else char
    }

    fun getSingleKeyboardButtonIds(): List<Int> = listOf(
        R.id.parent_1, R.id.parent_2, R.id.parent_3, R.id.parent_4,
        R.id.parent_5, R.id.parent_6, R.id.parent_7, R.id.parent_8,
        R.id.parent_9, R.id.parent_10, R.id.parent_11, R.id.parent_12,
        R.id.parent_13, R.id.parent_14, R.id.parent_15, R.id.parent_16,
        R.id.parent_17, R.id.parent_18, R.id.parent_19, R.id.parent_20,
        R.id.parent_21, R.id.parent_22, R.id.parent_23, R.id.parent_24,
        R.id.parent_25, R.id.parent_26, R.id.parent_27
    )
}