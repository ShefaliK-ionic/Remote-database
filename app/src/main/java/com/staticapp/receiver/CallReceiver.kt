package com.staticapp.receiver.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.staticapp.ui.home.view.MainActivity.Companion.userMobileNo


class CallReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        Log.d("222", "call coming: "+state)
        val incomingNumber = intent.getStringExtra("android.intent.extra.PHONE_NUMBER")
        if(incomingNumber!=null){
            userMobileNo=incomingNumber
        }
        Log.d("222","~~NEW_OUTGOING_CALL~~~"+incomingNumber)
        if (state == "android.intent.action.NEW_OUTGOING_CALL") {

//            Toast.makeText(context, "Hey! Calling Number : $incomingNumber", Toast.LENGTH_LONG)
//                .show()
        }
    }
}