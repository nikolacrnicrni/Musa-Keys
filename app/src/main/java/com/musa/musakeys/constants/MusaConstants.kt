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
}
