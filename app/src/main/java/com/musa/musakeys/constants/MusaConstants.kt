package com.musa.musakeys.constants


object MusaConstants {
    const val BROADCAST_RECEIVER_INTENT = "broadcase_receiver_action"
    const val COPIED_TEXT = "copiedText"
    const val FONTS_DOWNLOADED = "false"
    const val FONT_INDEX = "fontIndex"
    const val LANGUAGE_AUTO = "auto"
    const val LAST_COPIED_TEXT = "lastCopied"
    const val OPEN_HELP_ACTION = "open_help"
    const val OPEN_SETTING_ACTION = "open_setting"
    const val SAVED_FONT = "savedFonts"
    const val SERVER_CONNECTION_RESPONSE_SUCCESS = "Connection has been established"
    const val SERVER_VERSION = 7
    const val START_FOREGROUND_ACTION = "StartForegroundAction"
    const val STOP_FOREGROUND_ACTION = "StopForegroundAction"
    const val TEXT_FROM_LANDSCAPE = "textFromLandscape"
    const val TEXT_FROM_PORTRAIT = "textFromPotrait"
    const val USE_REMOTELY_ACTION = "use_remotely"
    const val WELCOME_SCREEN_ACTION = "welcome_screen_Action"

    var fontLabsMap: LinkedHashMap<String?, String?> = object : LinkedHashMap<String?, String?>() {
        init {
            put("Dushan Musa Alphabet", "\uE184\uE0A8\uE294\uE0EC\uE116\uE040\uE124\uE0A8\uE299\uE0EC\uE040\uE0F0\uE148\uE298\uE084\uE192\uE08C\uE19A")
            put("Kraljevo Musa Alphabet", "\uE2BE\uE2DB\uE0E8\uE050\uE152\uE08C\uE10C\uE094\uE040\uE124\uE0A8\uE299\uE0EC\uE040\uE0F0\uE148\uE298\uE084\uE192\uE08C\uE19A")
            put("Taunus Musa Ligature", "\uE208\uE0E8\uE10B\uE116\uE0A4\uE299\uE040\uE124\uE0A8\uE299\uE0EC\uE040\uE142\uE070\uE18A\uE084\uE2B8\uE111\uE06C\uE115")
            put("Yousuf Musa Ligature", "\uE111\uE0A8\uE10B\uE299\uE0A4\uE298\uE040\uE124\uE0A8\uE299\uE0EC\uE040\uE142\uE070\uE18A\uE084\uE2B8\uE111\uE06C\uE115")
            put("Zhouhei Musa Fangzi", "\uE2CA\uE0BA\uE10B\uE292\uE0C2\uE111\uE040\uE124\uE0AB\uE100\uE299\uE0EB\uE100\uE040\uE298\uE0E2\uE11C\uE2C5\uE063\uE100")
            put("Kunming Musa Fangzi", "\uE20E\uE082\uE116\uE124\uE0D9\uE11C\uE040\uE124\uE0AB\uE100\uE299\uE0EB\uE100\uE040\uE298\uE0E2\uE11C\uE2C5\uE063\uE100")
            put("Wangkai Musa Fangzi", "\uE10B\uE0E1\uE11C\uE20E\uE0F6\uE111\uE040\uE124\uE0AB\uE100\uE299\uE0EB\uE100\uE040\uE298\uE0E2\uE11C\uE2C5\uE063\uE100")
            put("Yasuhiro Musa Kana", "\uE111\uE0EC\uE299\uE0AC\uE29D\uE0D8\uE2CE\uE0BC\uE040\uE124\uE0A8\uE299\uE0EC\uE040\uE2BE\uE0E8\uE116\uE0EC")
            put("Tomokana Musa Ruby", "\uE2B8\uE0BC\uE124\uE0BC\uE2BE\uE0EC\uE116\uE0EC\uE040\uE124\uE0A8\uE299\uE0EC\uE040\uE2D1\uE0A8\uE192\uE0DC")
            put("Hentrax Musa Element", "\uE29F\uE088\uE116\uE2B8\uE115\uE0F4\uE2BE\uE299\uE040\uE124\uE0A8\uE299\uE0EC\uE040\uE088\uE142\uE084\uE124\uE054\uE116\uE2B8")
        }
    }
    var mapOfFontUris: LinkedHashMap<String?, String?> =
        object : LinkedHashMap<String?, String?>() {
            init {
                put(
                    "DushanMusaAlphabet-Regular",
                    "https://www.musa.bet/fonts/DushanMusaAlphabet-Regular.otf"
                )
                put(
                    "KraljevoMusaAlphabet-Regular",
                    "https://www.musa.bet/fonts/KraljevoMusaAlphabet-Regular.otf"
                )
                put(
                    "TaunusMusaLigature-Regular",
                    "https://www.musa.bet/fonts/TaunusMusaLigature-Regular.otf"
                )
                put(
                    "YousufMusaLigature-Regular",
                    "https://www.musa.bet/fonts/YousufMusaLigature-Regular.otf"
                )
                put(
                    "ZhouheiMusaFangzi-Regular",
                    "https://www.musa.bet/fonts/ZhouheiMusaFangzi-Regular.otf"
                )
                put(
                    "KunmingMusaFangzi-Regular",
                    "https://www.musa.bet/fonts/KunmingMusaFangzi-Regular.otf"
                )
                put(
                    "WangkaiMusaFangzi-Regular",
                    "https://www.musa.bet/fonts/WangkaiMusaFangzi-Regular.otf"
                )
                put(
                    "YasuhiroMusaKana-Regular",
                    "https://www.musa.bet/fonts/YasuhiroMusaKana-Regular.otf"
                )
                put(
                    "TomokanaMusaRuby-Regular",
                    "https://www.musa.bet/fonts/TomokanaMusaRuby-Regular.otf"
                )
                put(
                    "HentraxMusaElement-Regular",
                    "https://www.musa.bet/fonts/HentraxMusaElement-Regular.otf"
                )
            }
        }

    val musaTitle: String
        get() = "\uE124\uE0A8\uE299\uE0EC\uE040\uE20E\uE0D8\uE111\uE1BD"


    val keyNumber = listOf(
        12, 13, 14, 15, 16, 17, 11, -1, -1,
        9, 8, 7, 6, 5, 4, 10, -1, -1,
        21, 18, 20, 19, 3, 1, 2, 0, -1
    )

    val keyshape = listOf(0xE011, 0xE012, 0xE013, 0xE014, 0xE015, 0xE016, 0xE010, 0xE001, 0xE002,
        0xE00E, 0xE00D, 0xE00C, 0xE00B, 0xE00A, 0xE009, 0xE00F, 0xE001, 0xE003,
        0xE01A, 0xE017, 0xE019, 0xE018, 0xE008, 0xE006, 0xE007, 0xE005, 0xE004)

    val vowels = listOf( 12, 13, 14, 15, 16, 17, 11, -1, -1,
        9,  8,  7,  6,  5,  4, 10, -1, -1,
        21, 18, 20, 19,  3,  1,  2,  0, -1)

    val accents = listOf(
        -1, -1, -1, -1, -1, -1, -1,  0,  1,
        -1, -1, -1, -1, -1, -1, -1,  0,  2,
        -1, -1, -1, -1, -1, -1, -1, -1,  3)

    val invalidLetter by lazy {
        listOf(
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
    }

}
