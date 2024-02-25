package com.musa.musakeys.asyncTasks

import com.musa.musakeys.db.PersistablePreviousMessage

interface EntityPersistenceListener {
    fun onEntityPersisted(persistablePreviousMessage: PersistablePreviousMessage?, j: Long)
    fun onFailureOccured(asyncResult: AsyncResult?)
}
