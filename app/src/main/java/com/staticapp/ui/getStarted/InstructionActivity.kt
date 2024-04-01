package com.staticapp.ui.getStarted
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.staticapp.ui.home.view.MainActivity
import com.staticapp.databinding.ActivityInstructionBinding

class InstructionActivity : AppCompatActivity() {

    lateinit var activityInstructionBinding: ActivityInstructionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityInstructionBinding=ActivityInstructionBinding.inflate(layoutInflater)
        setContentView(activityInstructionBinding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun redirectToHome(view: View) {
        startActivity(Intent(this@InstructionActivity, MainActivity::class.java))
    }


}