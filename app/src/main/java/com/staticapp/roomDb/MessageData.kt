package com.staticapp.roomDb

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "MessageData")
data class MessageData( @PrimaryKey (autoGenerate = true)
                       var uid:Int, var message:String, var  timestamp:String){
    @Ignore
    var isSelected:Boolean=false
}