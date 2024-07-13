package com.staticapp.roomDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageData: MessageData):Long

    @Query("Select * from MessageData")
    fun getMessage(): Flow<List<MessageData>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(msgData: MessageData)


}