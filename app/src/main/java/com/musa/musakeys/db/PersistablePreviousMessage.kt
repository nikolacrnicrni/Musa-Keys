package com.musa.musakeys.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PersistablePreviousMessage {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "is_received")
    var isReceived: Boolean = false

    @ColumnInfo(name = "message")
    var message: String? = null
}
