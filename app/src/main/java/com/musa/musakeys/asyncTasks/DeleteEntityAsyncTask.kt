package com.musa.musakeys.asyncTasks

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import com.musa.musakeys.db.PreviousMessagesDao


class DeleteEntityAsyncTask(
    private val context: Context,
    previousMessagesDao2: PreviousMessagesDao
) :
    AsyncTask<Void?, Void?, AsyncResult?>() {
    private val previousMessagesDao: PreviousMessagesDao
    private var progressDialog: ProgressDialog? = null

    init {
        previousMessagesDao = previousMessagesDao2
    }

    /* access modifiers changed from: protected */
    public override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage("Clearing Messages")
    }

    /* access modifiers changed from: protected */
    override fun doInBackground(vararg voidArr: Void?): AsyncResult? {
        previousMessagesDao.deleteAllMessages()
        return null
    }

    /* access modifiers changed from: protected */
    public override fun onPostExecute(asyncResult: AsyncResult?) {
        super.onPostExecute(asyncResult)
        val progressDialog2 = progressDialog
        if (progressDialog2 != null && progressDialog2.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (_: Exception) {
            }
        }
    }
}
