package com.musa.musakeys.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface PreviousMessagesDao {
    @Query("SELECT * FROM PersistablePreviousMessage")
    fun getAllMessages(): List<PersistablePreviousMessage>

    @Query("SELECT * FROM PersistablePreviousMessage WHERE message = :message")
    fun getMessageByContent(message: String): List<PersistablePreviousMessage>

    @Insert
    fun insertEntity(persistablePreviousMessage: PersistablePreviousMessage): Long

    @Update
    fun updateEntity(persistablePreviousMessage: PersistablePreviousMessage)

    @Delete
    fun deleteEntity(persistablePreviousMessage: PersistablePreviousMessage)

    @Query("DELETE FROM PersistablePreviousMessage")
    fun deleteAllMessages()
}
