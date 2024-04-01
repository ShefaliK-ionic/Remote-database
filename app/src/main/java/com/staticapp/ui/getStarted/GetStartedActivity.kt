package com.staticapp.ui.getStarted

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import com.staticapp.R
import com.staticapp.databinding.ActivityGetStartedBinding

class GetStartedActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_get_started)
    }

    fun getStart(view: View) {
        startActivity(Intent(this@GetStartedActivity,InstructionActivity::class.java))

    }


}