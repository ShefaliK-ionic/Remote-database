package com.staticapp

import android.util.Log
import com.staticapp.base.DbResponseCallback
import com.staticapp.roomDb.MessageDao
import com.staticapp.roomDb.MessageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class Repository @Inject constructor(val msgDao: MessageDao) {

    suspend fun insertMsg(messageData: MessageData)= {

     GlobalScope.launch(Dispatchers.IO) {

         var it=msgDao.insertMessage(messageData)
          Log.d("222","~insertMessage~~"+it)
//   emit()
}
    }.asFlow().flowOn(Dispatchers.IO)

     fun getAllMsg()= flow<List<MessageData>> {


//       emit(DbResponseCallback.Loading())

//        var msg=msgDao.getMessage()
         emitAll(msgDao.getMessage())

//         DbResponseCallback.Success(msgDao.getMessage().collect{
//             emit(it)
//         })
//         Log.d("222","~get msg~~~"+msg.collect{
//             Log.d("222","~~Msglist~~"+it.size)
//         })


    }.flowOn(Dispatchers.IO).catch {
        Log.d("222","~catch msg~~~"+it.message)
     }




}