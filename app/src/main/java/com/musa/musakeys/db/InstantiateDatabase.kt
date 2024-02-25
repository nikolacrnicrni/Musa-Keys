package com.musa.musakeys.db

import android.content.Context
import androidx.room.Room.databaseBuilder


object InstantiateDatabase {
    private var applicationDatabase: ApplicationDatabase? = null
    fun getDatabaseInstance(context: Context?): ApplicationDatabase? {
        if (applicationDatabase == null) {
            applicationDatabase = databaseBuilder(
                context!!,
                ApplicationDatabase::class.java, "ApplicationDatabase"
            ).build()
        }
        return applicationDatabase
    }
}
