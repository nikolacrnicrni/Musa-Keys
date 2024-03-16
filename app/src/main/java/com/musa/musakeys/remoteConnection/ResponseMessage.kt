package com.musa.musakeys.remoteConnection

import com.musa.musakeys.constants.MusaConstants
import kotlin.math.absoluteValue

class ResponseMessage {
    var connectionError: String? = null
    var connectionResponse: String? = null
    val isVersionMismatch: Boolean
        get() = !(serverVersion == 0 || serverVersion == MusaConstants.SERVER_VERSION.absoluteValue)
    var messageError: String? = null
    var serverVersion = 0

    override fun toString(): String {
        return """
               ConnectionResponse: ${connectionResponse}
               ConnectionError: ${connectionError}
               MessageError: ${messageError}
               """.trimIndent()
    }
}
