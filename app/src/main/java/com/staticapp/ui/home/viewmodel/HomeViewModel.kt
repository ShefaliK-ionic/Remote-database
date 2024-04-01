package com.staticapp.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.staticapp.Repository
import com.staticapp.base.DbResponseCallback
import com.staticapp.roomDb.MessageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: Repository):ViewModel()  {

    private val statusGetMessageFlow = MutableStateFlow<List<MessageData>?>(null)

    val _statusGetMessageFlow: StateFlow<List<MessageData>?> = statusGetMessageFlow

//    private val statusInsertMessageFlow = MutableStateFlow<Long?>(null)
//
//    val _statusInsertMessageFlow: StateFlow<Long?> = statusInsertMessageFlow


    fun getAllMessage(){

        viewModelScope.launch {

            repository.getAllMsg().catch {
                Log.d("222", "~~callOnboarding~getAllMsg~~~" + it.message)
            }.collect{
                  statusGetMessageFlow.value=it
                Log.d("222", "~~callOnboarding~getAllMsg~SUC~~" +it.size)
            }

        }



    }


    fun insertMessage(messageData: MessageData){

        viewModelScope.launch {

            var longId=repository.insertMsg(messageData).catch {
                Log.d("222", "~~callOnboarding~insertMessage~fail~~" + it.message)
            }.collect{
                Log.d("222", "~~callOnboarding~insertMessage~~suc~" + it)
//                statusInsertMessageFlow.value=it
            }

            Log.d("222", "~~callOnboarding~insertMessage~~longId~" + longId)


        }



    }


}