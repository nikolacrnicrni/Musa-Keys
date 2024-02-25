package com.musa.musakeys.asyncTasks

import android.app.ProgressDialog
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.musa.musakeys.db.PersistablePreviousMessage
import com.musa.musakeys.db.PreviousMessagesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InsertEntityAsyncTask(
    private val context: Context,
    private val entity: PersistablePreviousMessage,
    private val entityPersistenceListener: EntityPersistenceListener,
    private val previousMessagesDao: PreviousMessagesDao
) {
    private var progressDialog: ProgressDialog? = null

    fun execute(arrayOfNulls: Array<Void?>) {
        (context as? LifecycleOwner)?.lifecycleScope?.launch {
            showProgressDialog()
            val result = doInBackground()
            onPostExecute(result)
            hideProgressDialog()
        }
    }

    private suspend fun showProgressDialog() = withContext(Dispatchers.Main) {
        progressDialog = ProgressDialog(context).apply {
            setMessage("Saving data")
            show()
        }
    }

    private suspend fun doInBackground(): AsyncResult? {
        return withContext(Dispatchers.IO) {
            try {
                // Assuming that getMessageByContent and insertEntity are long-running database operations
                entity.message?.let { previousMessagesDao.getMessageByContent(it) }
                val id = previousMessagesDao.insertEntity(entity) ?: Long.MIN_VALUE
                AsyncResult(id, false, "Data saved successfully")
            } catch (e: SQLiteConstraintException) {
                AsyncResult(Long.MIN_VALUE, true, e.message ?: "Database error")
            }
        }
    }


    private suspend fun onPostExecute(result: AsyncResult?) = withContext(Dispatchers.Main) {
        if (result == null || result.isErrorOccured) {
            entityPersistenceListener.onFailureOccured(result)
        } else {
            entityPersistenceListener.onEntityPersisted(entity, result.id)
        }
    }

    private suspend fun hideProgressDialog() = withContext(Dispatchers.Main) {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}

