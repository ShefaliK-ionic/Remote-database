package com.staticapp

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log


class MyPhoneStateListener : PhoneStateListener() {
    override fun onCallStateChanged(state: Int, incomingNumber: String) {
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                Log.d("222","~~~CALL_STATE_RINGING~~~~")
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {

                Log.d("222","~~~CALL_STATE_OFFHOOK~90~~~"+incomingNumber)
//               MainActivity.playAudio()
            }
            TelephonyManager.CALL_STATE_IDLE -> {
                Log.d("222","~~~CALL_STATE_IDLE~~909~~")
            }
        }
    }



}