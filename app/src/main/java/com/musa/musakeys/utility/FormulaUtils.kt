package com.musa.musakeys.utility

import com.musa.musakeys.R
import android.graphics.Typeface
import android.text.Html
import android.util.Log
import com.google.common.collect.Lists
import com.musa.musakeys.model.MusaTextMetadata

object FormulaUtils {
    val NUMERIC_HIGH_MODE_KEY = getFormattedHexValue(11026)
    val NUMERIC_LOW_MODE_KEY = getFormattedHexValue(11027)
    val PARENT_KEY_ORIGINAL_HIGH_MODE = getFormattedHexValue(57348)
    val PARENT_KEY_ORIGINAL_LOW_MODE = getFormattedHexValue(57354)

    private val accents = listOf(
        -1, -1, -1, -1, -1, -1, -1,  0,  1,
        -1, -1, -1, -1, -1, -1, -1,  0,  2,
        -1, -1, -1, -1, -1, -1, -1, -1,  3)
    val highDigitsMusa = listOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")

    private val keyshape = listOf(0xE011, 0xE012, 0xE013, 0xE014, 0xE015, 0xE016, 0xE010, 0xE001, 0xE002,
        0xE00E, 0xE00D, 0xE00C, 0xE00B, 0xE00A, 0xE009, 0xE00F, 0xE001, 0xE003,
        0xE01A, 0xE017, 0xE019, 0xE018, 0xE008, 0xE006, 0xE007, 0xE005, 0xE004)

    private val numericHighDigits = listOf(57440, 57409, 57410, 57411, 57408, 57488, 57568, 57544, 57520, 57592, 57472, 57576, 57552, 57528, 57512, 57432, 57584, 57560, 57536, 57600, 57504, 57409, 57410, 57411, 57408, 57488, 57488, 57472, 57496, 57592, 57472, 57472, 57464, 57488, 57448, 57456, 57480, 57456, 57480, 57424)
    private val numericKeysParent = listOf(57363, 57348, 57351, 11027, 57345, 57377, 57401, 57396, 57387, 57406, 57370, 57402, 57397, 57392, 57385, 57360, 57403, 57398, 57393, 57357, 57384, 9099, 8677, 8629, 9141, 57377, 57377, 57370, 57382, 57406, 57370, 57370, 57368, 57377, 57364, 57366, 57375, 57366, 57375, 57357)
    private val numericKeysParentShifted = listOf(57384, 57348, 57351, 11027, 57345, 57377, 57377, 57370, 57382, 57406, 57370, 57370, 57368, 57377, 57364, 57366, 57375, 57366, 57375, 57357)
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

    const val isHentrax = false

    val shiftCharacters = listOf("ZWNJ", "ZWJ", "#", "", "", "", "")

    private val vowels = listOf( 12, 13, 14, 15, 16, 17, 11, -1, -1,
        9,  8,  7,  6,  5,  4, 10, -1, -1,
        21, 18, 20, 19,  3,  1,  2,  0, -1)

    private val invalidLetter = listOf(
        0xE117,
        0xE119,
        0xE11A,
        0xE11B,
        0xE11D,
        0xE11E,
        0xE121,
        0xE123,
        0xE127,
        0xE129,
        0xE12A,
        0xE12B,
        0xE137,
        0xE138,
        0xE13E,
        0xE13F,
        0xE140,
        0xE143,
        0xE144,
        0xE145,
        0xE146,
        0xE147,
        0xE149,
        0xE14A,
        0xE14D,
        0xE14E,
        0xE14F,
        0xE150,
        0xE151,
        0xE153,
        0xE155,
        0xE156,
        0xE157,
        0xE159,
        0xE15A,
        0xE15B,
        0xE15C,
        0xE15D,
        0xE15F,
        0xE160,
        0xE163,
        0xE164,
        0xE165,
        0xE167,
        0xE169,
        0xE16C,
        0xE16D,
        0xE16F,
        0xE171,
        0xE172,
        0xE173,
        0xE174,
        0xE175,
        0xE17D,
        0xE17E,
        0xE180,
        0xE181,
        0xE182,
        0xE185,
        0xE188,
        0xE189,
        0xE18B,
        0xE18F,
        0xE197,
        0xE198,
        0xE19B,
        0xE19D,
        0xE19E,
        0xE19F,
        0xE1A1,
        0xE1A2,
        0xE1A5,
        0xE1A6,
        0xE1A7,
        0xE1AA,
        0xE1AC,
        0xE1AD,
        0xE1AE,
        0xE1B1,
        0xE1B2,
        0xE1B4,
        0xE1B5,
        0xE1B7,
        0xE1BB,
        0xE1C5,
        0xE1F3,
        0xE1F5,
        0xE1F6,
        0xE1F7,
        0xE1F8,
        0xE1F9,
        0xE201,
        0xE202,
        0xE204,
        0xE209,
        0xE20A,
        0xE20C,
        0xE20D,
        0xE20F,
        0xE213,
        0xE214,
        0xE21B,
        0xE21C,
        0xE21F,
        0xE220,
        0xE222,
        0xE223,
        0xE225,
        0xE229,
        0xE22C,
        0xE231,
        0xE233,
        0xE235,
        0xE238,
        0xE239,
        0xE23B,
        0xE23F,
        0xE240,
        0xE247,
        0xE248,
        0xE24B,
        0xE24C,
        0xE24D,
        0xE24E,
        0xE24F,
        0xE251,
        0xE252,
        0xE255,
        0xE256,
        0xE257,
        0xE25A,
        0xE25C,
        0xE25E,
        0xE261,
        0xE262,
        0xE263,
        0xE264,
        0xE265,
        0xE267,
        0xE268,
        0xE26B,
        0xE26C,
        0xE26D,
        0xE26E,
        0xE26F,
        0xE271,
        0xE273,
        0xE274,
        0xE277,
        0xE278,
        0xE27A,
        0xE27B,
        0xE27C,
        0xE27D,
        0xE281,
        0xE282,
        0xE284,
        0xE286,
        0xE287,
        0xE288,
        0xE289,
        0xE28D,
        0xE28E,
        0xE290,
        0xE291,
        0xE293,
        0xE297,
        0xE2A1,
        0xE2A3,
        0xE2A4,
        0xE2A6,
        0xE2A7,
        0xE2A9,
        0xE2AD,
        0xE2AE,
        0xE2B5,
        0xE2B6,
        0xE2B9,
        0xE2BC,
        0xE2BD,
        0xE2BF,
        0xE2C3,
        0xE2CF,
        0xE2D0,
        0xE2D2,
        0xE2D3,
        0xE2D4,
        0xE2D5,
        0xE2D9,
        0xE2DE,
        0xE2DF,
        0xE2E1,
        0xE2E4,
        0xE2E5,
        0xE2E6,
        0xE2E7,
        0xE2E8,
        0xE2E9,
        0xE2EA,
        0xE2EB,
        0xE2EC,
        0xE2ED,
        0xE2EF,
        0xE2EF,
        0xE2F0,
        0xE2F1,
        0xE2F2,
        0xE2F3,
        0xE2F4,
        0xE2F5,
        0xE2F6,
        0xE2F7,
        0xE2F8,
        0xE2F9,
        0xE2FA,
        0xE2FB,
        0xE2FC,
        0xE2FD,
        0xE2FE,
        0xE2FF
    )

    fun getNumericKeysParent(typeface: Typeface): List<MusaTextMetadata?>? {
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

    fun getNumericLowDigits(): List<MusaTextMetadata?>? {
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

    fun getNumericHighDigits(): List<MusaTextMetadata?>? {
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

    fun getShiftedParentKeys(typeface: Typeface): List<MusaTextMetadata?>? {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i in 0..26) {
            val builder = MusaTextMetadata.builder()
            if (i == 17) {
                builder.withTextFont(typeface).withDisplayText(shiftCharacters[5])
                    .withDisplaySize(24).withActualText("⇥")
            } else if (i != 26) {
                when (i) {
                    0 -> builder.withTextFont(typeface).withDisplayText("↵").withDisplaySize(24)
                        .withActualText("↵")

                    1 -> builder.withTextFont(typeface).withDisplayText("␣").withDisplaySize(24)
                        .withActualText(" ")

                    2 -> builder.withTextFont(typeface).withDisplayText("⎋").withDisplaySize(24)
                        .withActualText("⎋")

                    3 -> builder.withTextFont(typeface).withDisplayText("⇥").withDisplaySize(24)
                        .withActualText("⇥")

                    4 -> builder.withTextFont(typeface).withDisplayText(shiftCharacters[0])
                        .withDisplaySize(10).withActualText("‌")

                    5 -> builder.withTextFont(typeface).withDisplayText(shiftCharacters[1])
                        .withDisplaySize(12).withActualText("‍")

                    6 -> builder.withTextFont(typeface).withDisplayText(shiftCharacters[2])
                        .withDisplaySize(24).withActualText("⇥")

                    7 -> builder.withTextFont(typeface).withDisplayText(shiftCharacters[3])
                        .withDisplaySize(24).withActualText("⇥")

                    8 -> builder.withTextFont(typeface).withDisplayText(shiftCharacters[4])
                        .withDisplaySize(24).withActualText("⇥")
                }
            } else {
                builder.withTextFont(typeface).withDisplayText(shiftCharacters[6])
                    .withDisplaySize(24).withActualText("⇥")
            }
            newArrayList.add(builder.build())
        }
        return newArrayList
    }

    fun getNumericCharacters(typeface: Typeface): List<MusaTextMetadata?>? {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i in 0..26) {
            if (i != 26) {
                val builder = MusaTextMetadata.builder()
                builder.withTextFont(typeface)
                builder.withDisplaySize(24)
                builder.withActualText(getFormattedHexValue(keyshape[i]))
                builder.withDisplayText(getFormattedHexValue(keyshape[i]))
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

    fun getParentKeyboard(typeface: Typeface): List<MusaTextMetadata?>? {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i in 0..26) {
            val builder = MusaTextMetadata.builder()
            builder.withTextFont(typeface)
            builder.withDisplaySize(24)
            builder.withActualText(getFormattedHexValue(keyshape[i]))
            builder.withDisplayText(getFormattedHexValue(keyshape[i]))
            newArrayList.add(builder.build())
        }
        return newArrayList
    }

    fun getChildKeysForTopParent(i: Int, typeface: Typeface): List<MusaTextMetadata?>? {
       val newArrayList: ArrayList<MusaTextMetadata> = Lists.newArrayList<MusaTextMetadata>()
        for (i2 in 0..26) {
            val codePoint = getCodePoint(i, i2)
            codePoint.withTextFont(typeface)
            codePoint.withDisplaySize(24)
            newArrayList.add(codePoint.build())
        }
        return newArrayList
    }

    fun getOutput(i: Int, i2: Int): MusaTextMetadata? {
        return getCodePoint(i, i2).build()
    }

    private fun getCodePoint(t: Int, b: Int): MusaTextMetadata.Builder {
        val builder = MusaTextMetadata.builder().withDisplaySize(24)
        var codePoint: Int

        if (accents[t] < 0) {
            if (accents[b] < 0) {
                codePoint = 0xE100 + (22 * vowels[t]) + vowels[b]
                if (isHentrax) {
                    codePoint
                } else if (invalidLetter.contains(codePoint)) {
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

        val str = getFormattedHexValue(codePoint)
        return builder.withDisplayText(str).withActualText(str)
    }


    private fun getFormattedHexValue(num: Int?): String {
        val invalidLetter = listOf(
            0xE117,
            0xE119,
            0xE11A,
            0xE11B,
            0xE11D,
            0xE11E,
            0xE121,
            0xE123,
            0xE127,
            0xE129,
            0xE12A,
            0xE12B,
            0xE137,
            0xE138,
            0xE13E,
            0xE13F,
            0xE140,
            0xE143,
            0xE144,
            0xE145,
            0xE146,
            0xE147,
            0xE149,
            0xE14A,
            0xE14D,
            0xE14E,
            0xE14F,
            0xE150,
            0xE151,
            0xE153,
            0xE155,
            0xE156,
            0xE157,
            0xE159,
            0xE15A,
            0xE15B,
            0xE15C,
            0xE15D,
            0xE15F,
            0xE160,
            0xE163,
            0xE164,
            0xE165,
            0xE167,
            0xE169,
            0xE16C,
            0xE16D,
            0xE16F,
            0xE171,
            0xE172,
            0xE173,
            0xE174,
            0xE175,
            0xE17D,
            0xE17E,
            0xE180,
            0xE181,
            0xE182,
            0xE185,
            0xE188,
            0xE189,
            0xE18B,
            0xE18F,
            0xE197,
            0xE198,
            0xE19B,
            0xE19D,
            0xE19E,
            0xE19F,
            0xE1A1,
            0xE1A2,
            0xE1A5,
            0xE1A6,
            0xE1A7,
            0xE1AA,
            0xE1AC,
            0xE1AD,
            0xE1AE,
            0xE1B1,
            0xE1B2,
            0xE1B4,
            0xE1B5,
            0xE1B7,
            0xE1BB,
            0xE1C5,
            0xE1F3,
            0xE1F5,
            0xE1F6,
            0xE1F7,
            0xE1F8,
            0xE1F9,
            0xE201,
            0xE202,
            0xE204,
            0xE209,
            0xE20A,
            0xE20C,
            0xE20D,
            0xE20F,
            0xE213,
            0xE214,
            0xE21B,
            0xE21C,
            0xE21F,
            0xE220,
            0xE222,
            0xE223,
            0xE225,
            0xE229,
            0xE22C,
            0xE231,
            0xE233,
            0xE235,
            0xE238,
            0xE239,
            0xE23B,
            0xE23F,
            0xE240,
            0xE247,
            0xE248,
            0xE24B,
            0xE24C,
            0xE24D,
            0xE24E,
            0xE24F,
            0xE251,
            0xE252,
            0xE255,
            0xE256,
            0xE257,
            0xE25A,
            0xE25C,
            0xE25E,
            0xE261,
            0xE262,
            0xE263,
            0xE264,
            0xE265,
            0xE267,
            0xE268,
            0xE26B,
            0xE26C,
            0xE26D,
            0xE26E,
            0xE26F,
            0xE271,
            0xE273,
            0xE274,
            0xE277,
            0xE278,
            0xE27A,
            0xE27B,
            0xE27C,
            0xE27D,
            0xE281,
            0xE282,
            0xE284,
            0xE286,
            0xE287,
            0xE288,
            0xE289,
            0xE28D,
            0xE28E,
            0xE290,
            0xE291,
            0xE293,
            0xE297,
            0xE2A1,
            0xE2A3,
            0xE2A4,
            0xE2A6,
            0xE2A7,
            0xE2A9,
            0xE2AD,
            0xE2AE,
            0xE2B5,
            0xE2B6,
            0xE2B9,
            0xE2BC,
            0xE2BD,
            0xE2BF,
            0xE2C3,
            0xE2CF,
            0xE2D0,
            0xE2D2,
            0xE2D3,
            0xE2D4,
            0xE2D5,
            0xE2D9,
            0xE2DE,
            0xE2DF,
            0xE2E1,
            0xE2E4,
            0xE2E5,
            0xE2E6,
            0xE2E7,
            0xE2E8,
            0xE2E9,
            0xE2EA,
            0xE2EB,
            0xE2EC,
            0xE2ED,
            0xE2EF,
            0xE2EF,
            0xE2F0,
            0xE2F1,
            0xE2F2,
            0xE2F3,
            0xE2F4,
            0xE2F5,
            0xE2F6,
            0xE2F7,
            0xE2F8,
            0xE2F9,
            0xE2FA,
            0xE2FB,
            0xE2FC,
            0xE2FD,
            0xE2FE,
            0xE2FF
        )
        num?.let {
            return if (invalidLetter.contains(num.toInt())) {
                ""
            }
            else {
                val htmlVal = Html.fromHtml("&#x" + Integer.toHexString(num.toInt()).uppercase()).toString()
                if (htmlVal == "&#x0")
                {
                    ""
                } else {
                    htmlVal
                }
            }
        } ?: run {
            return ""
        }
    }

    fun getSingleKeyboardButtonIds(): List<Int?>? {
        val arrayList: ArrayList<Int?> = ArrayList()
        arrayList.add(Integer.valueOf(R.id.parent_1))
        arrayList.add(Integer.valueOf(R.id.parent_2))
        arrayList.add(Integer.valueOf(R.id.parent_3))
        arrayList.add(Integer.valueOf(R.id.parent_4))
        arrayList.add(Integer.valueOf(R.id.parent_5))
        arrayList.add(Integer.valueOf(R.id.parent_6))
        arrayList.add(Integer.valueOf(R.id.parent_19))
        arrayList.add(Integer.valueOf(R.id.parent_8))
        arrayList.add(Integer.valueOf(R.id.parent_9))
        arrayList.add(Integer.valueOf(R.id.parent_10))
        arrayList.add(Integer.valueOf(R.id.parent_11))
        arrayList.add(Integer.valueOf(R.id.parent_12))
        arrayList.add(Integer.valueOf(R.id.parent_13))
        arrayList.add(Integer.valueOf(R.id.parent_14))
        arrayList.add(Integer.valueOf(R.id.parent_15))
        arrayList.add(Integer.valueOf(R.id.parent_16))
        arrayList.add(Integer.valueOf(R.id.parent_17))
        arrayList.add(Integer.valueOf(R.id.parent_18))
        arrayList.add(Integer.valueOf(R.id.parent_7))
        arrayList.add(Integer.valueOf(R.id.parent_20))
        arrayList.add(Integer.valueOf(R.id.parent_21))
        arrayList.add(Integer.valueOf(R.id.parent_22))
        arrayList.add(Integer.valueOf(R.id.parent_23))
        arrayList.add(Integer.valueOf(R.id.parent_24))
        arrayList.add(Integer.valueOf(R.id.parent_25))
        arrayList.add(Integer.valueOf(R.id.parent_26))
        arrayList.add(Integer.valueOf(R.id.parent_27))
        return arrayList
    }

}