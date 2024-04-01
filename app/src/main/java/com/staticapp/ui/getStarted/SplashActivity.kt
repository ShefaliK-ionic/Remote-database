package com.staticapp.ui.getStarted

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.staticapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
//
//        getWindow().setStatusBarColor(ContextCompat.getColor(this@SplashActivity,R.color.white));//
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
//        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white))


        redirectScreen()


    }

    private fun redirectScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            switchRedirect()
        }, 1000)
    }

    private fun switchRedirect() {
        val intent = Intent(this, GetStartedActivity::class.java)
        startActivity(intent)
        finish()
    }
}