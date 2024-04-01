package com.staticapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ServiceForDeviceB extends Service {
    private final IBinder binder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("222", "onBind() called on Device B");

        return binder;
    }

    public void myFunction() {
        // Your function logic here
        Intent intent = new Intent("custom-action-local-broadcast");
        // on below line we are passing data to our broad cast receiver with key and value pair.
        intent.putExtra("message", "Welcome \n to \n Geeks For Geeks");
        // on below line we are sending our broad cast with intent using broad cast manager.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        Log.d("222", "myFunction() called on Device B");
    }

    public class MyBinder extends Binder {
        ServiceForDeviceB getService() {
            return ServiceForDeviceB.this;
        }
    }




}