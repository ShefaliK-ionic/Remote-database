package com.staticapp.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertMessage(messageData: MessageData):Long

    @Query("Select * from MessageData")
    fun getMessage(): Flow<List<MessageData>>



}