package com.musa.musakeys.asyncTasks

import android.content.Context
import android.os.AsyncTask
import com.musa.musakeys.db.InstantiateDatabase
import com.musa.musakeys.db.PersistablePreviousMessage

class GetMessagesAsyncTask(
    private val context: Context,
    private val dataAvailabilityListener: DataAvailabilityListener
) : AsyncTask<Void?, Void?, List<PersistablePreviousMessage?>?>() {

    override fun doInBackground(vararg voidArr: Void?): List<PersistablePreviousMessage?>? {
        return InstantiateDatabase.getDatabaseInstance(context.applicationContext)?.messageDao()?.getAllMessages()
    }

    public override fun onPostExecute(list: List<PersistablePreviousMessage?>?) {
        super.onPostExecute(list)
        if (list != null) {
            dataAvailabilityListener.onDataSetAvailable(list)
        }
    }
}
