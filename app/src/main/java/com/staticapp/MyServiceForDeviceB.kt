package com.example.staticapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log


class MyServiceForDeviceB: Service() {
    private val binder: IBinder = MyBinder()


    override fun onBind(p0: Intent?): IBinder? {
         return binder
    }

    fun myFunction() {
        // Your function logic here
        Log.d("222", "myFunction() called on Device B")
    }


    class MyBinder : Binder() {
//        fun getService(): MyServiceForDeviceB? {
//            return this@MyServiceForDeviceB
//        }

    }


}