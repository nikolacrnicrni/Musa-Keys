package com.musa.musakeys.remoteConnection

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation.findNavController
import com.musa.musakeys.R
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.constants.SharedPreferenceEnum
import com.musa.musakeys.ui.welcome.WelcomeMusaActivity
import com.musa.musakeys.utility.PassKeyHelperUtil.getIpFromPassKey
import com.musa.musakeys.utility.SharedPreferenceHelperUtil.updateSharedPreferenceString
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket

class NetworkCommunicationAsync(
    private val context: Context,
    private val isConnectionRequest: Boolean,
    private val passKey: String,
    private val message: Any?
) :
    AsyncTask<Void?, Void?, ResponseMessage>() {
    private var builder: AlertDialog.Builder? = null
    private var progressDialog: ProgressDialog? = null

    public override fun onPreExecute() {
        super.onPreExecute()
        if (isConnectionRequest) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Trying to connect")
            progressDialog!!.show()
        }
    }

    override fun doInBackground(vararg voidArr: Void?): ResponseMessage {
        val responseMessage = ResponseMessage()
        if (isConnectionRequest) {
            try {
                clientSocket = Socket()
                clientSocket!!.connect(
                    InetSocketAddress(
                        getIpFromPassKey(
                            passKey
                        ), 9898
                    ), 10000
                )
                `in` = BufferedReader(
                    InputStreamReader(
                        clientSocket!!.getInputStream()
                    )
                )
                out = PrintWriter(
                    clientSocket!!.getOutputStream()
                )
                responseMessage.connectionResponse = `in`!!.readLine()
                if (responseMessage.connectionResponse != null) {
                    val connectionResponse = responseMessage.connectionResponse
                    if (connectionResponse!!.contains(MusaConstants.SERVER_CONNECTION_RESPONSE_SUCCESS)) {
                        val indexOf = connectionResponse.indexOf("v")
                        if (indexOf != -1) {
                            responseMessage.serverVersion =
                                connectionResponse.substring(indexOf + 1, indexOf + 2).toInt()
                        }
                    } else {
                        responseMessage.connectionError = connectionResponse
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                responseMessage.connectionError = e.message
                Log.e("CONNECTION_ERROR  ", e.toString())
            }
        } else {
            val printWriter = out
            if (!(printWriter == null || `in` == null)) {
                try {
                    if (message is String) {
                        printWriter.println(message)
                    }
                    out!!.flush()
                    responseMessage.connectionResponse = `in`!!.readLine()
                    Log.e(
                        "MESSAGE_RESPONSE",
                        "Message Sent: " + message + " Response: " + responseMessage.connectionResponse
                    )
                    val sb = StringBuilder()
                    sb.append(responseMessage.serverVersion)
                    sb.append("")
                    Log.e("version", sb.toString())
                } catch (e2: IOException) {
                    e2.printStackTrace()
                    responseMessage.messageError = e2.toString()
                    Log.e("MESSAGE_ERROR", e2.toString())
                }
            }
        }
        if (responseMessage.connectionResponse == null || responseMessage.connectionResponse == "stop" || responseMessage.connectionError != null || responseMessage.messageError != null || responseMessage.isVersionMismatch) {
            try {
                if (`in` != null) {
                    `in`!!.close()
                    `in` = null
                }
                if (out != null) {
                    out!!.close()
                    out = null
                }
                if (clientSocket != null) {
                    clientSocket!!.close()
                    clientSocket = null
                }
            } catch (e3: IOException) {
                e3.printStackTrace()
            }
        }
        return responseMessage
    }

    @SuppressLint("ResourceType")
    public override fun onPostExecute(responseMessage: ResponseMessage) {
        super.onPostExecute(responseMessage)
        Log.e("check", " $responseMessage")
        if (isConnectionRequest && progressDialog != null) {
            try {
                progressDialog!!.dismiss()
            } catch (_: Exception) {
            }
        }
        if (responseMessage.connectionResponse != null && responseMessage.messageError == null && responseMessage.connectionError == null) {
            if (responseMessage.isVersionMismatch) {
                builder = AlertDialog.Builder(context)
                builder!!.setTitle("Old Version")
                    .setMessage("Please download the latest version of the Android app and Server Agent")
                    .setPositiveButton(
                        17039379
                    ) { dialogInterface, _ -> dialogInterface.cancel() }
                    .setIcon(17301543)
                val context2 = context
                if (context2 is Activity) {
                    context2.finish()
                    if ((context as Activity).isFinishing) {
                        builder!!.show()
                    }
                }
            } else if (responseMessage.connectionResponse!!.contains(MusaConstants.SERVER_CONNECTION_RESPONSE_SUCCESS)) {
                val context3 = context
                if (context3 is Activity) {
                    val findNavController = findNavController(context3, R.id.nav_host_fragment)
                    findNavController.popBackStack()
                    findNavController.navigate(R.id.nav_musa_remote_portrait_keypad as Int)
                }
                updateSharedPreferenceString(
                    context, SharedPreferenceEnum.PASS_KEY_SAVED,
                    passKey
                )
                val context4 = context
                Toast.makeText(context4, context4.getString(R.string.connected), 0).show()
                IsConnected = true
                if (progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                updateSharedPreferenceString(
                    context, SharedPreferenceEnum.PASS_KEY_SAVED,
                    passKey
                )
                Toast.makeText(context, "Connected", 0).show()
                IsConnected = true
            } else if (responseMessage.connectionResponse == "stop") {
                val context5 = context
                Toast.makeText(context5, context5.getString(R.string.disconnected), 1).show()
                IsConnected = false
            }
        } else if ("stop" != message) {
            builder = AlertDialog.Builder(context, 16974374)
            builder!!.setTitle(context.getString(R.string.error))
                .setMessage(responseMessage.toString()).setPositiveButton(
                    17039379
                ) { dialogInterface, _ ->
                    networkCommunicationAsync(
                        dialogInterface
                    )
                }.setIcon(17301543)
            val context6 = context
            if (context6 is Activity && !context6.isFinishing) {
                builder!!.show()
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun networkCommunicationAsync(
        dialogInterface: DialogInterface
    ) {
        if (!isConnectionRequest) {
            val context2 = context
            if (context2 is Activity) {
                context2.finish()
            }
            val intent = Intent(context, WelcomeMusaActivity::class.java)
            intent.addFlags(335544320)
            context.startActivity(intent)
        }
        dialogInterface.cancel()
    }

    companion object {
        var IsConnected = false
        private var clientSocket: Socket? = null
        private var `in`: BufferedReader? = null
        private var out: PrintWriter? = null
    }
}
