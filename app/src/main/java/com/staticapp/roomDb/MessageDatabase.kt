package com.staticapp.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MessageData::class], version = 1,exportSchema = false)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun getDao():MessageDao
}