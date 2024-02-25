package com.musa.musakeys.asyncTasks

import com.musa.musakeys.db.PersistablePreviousMessage

interface DataAvailabilityListener {
    fun onDataSetAvailable(list: List<PersistablePreviousMessage?>?)
    fun onSingleResultAvailable(persistablePreviousMessage: PersistablePreviousMessage?)
}
