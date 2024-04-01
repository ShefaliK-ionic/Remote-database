package com.staticapp.ui.home.view.tab_fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.staticapp.MyPhoneStateListener
import com.staticapp.databinding.FragmentConnectionBinding
import com.staticapp.ui.home.view.MainActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConnectionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var ctx: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx=context
    }

    lateinit var connectionFragBinding: FragmentConnectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        connectionFragBinding=FragmentConnectionBinding.inflate(layoutInflater)
        clickEvents()
        setTimer()
        return connectionFragBinding.root
    }


    lateinit var mTelManager: TelephonyManager
    val mPhoneStateListener = MyPhoneStateListener()
    private fun setTimer() {
        mTelManager = ctx. getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        mTelManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }




    private fun clickEvents() {
        connectionFragBinding.imgSignOff.setOnClickListener {
            endCall()
        }

        connectionFragBinding.relativeRecentMsg.setOnClickListener {
            (activity as MainActivity?)?.changeTabSelection(1)
        }

//        connectionFragBinding.voiseasudio.setOnClickListener {
//            val intent_upload = Intent()
//            intent_upload.setType("audio/*")
//            intent_upload.setAction(Intent.ACTION_GET_CONTENT)
//            startActivityForResult(intent_upload, 1)
//        }

//         connectionFragBinding.voiseasudio.setAudio("")





    }






    fun endCall(): Boolean {
        return try {

            val telecomManager =ctx. getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
            if (ActivityCompat.checkSelfPermission(
                    ctx
                    ,
                    Manifest.permission.ANSWER_PHONE_CALLS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager?.endCall()
            }
                 sendSms()


            Log.d("222","~~~~telephonyService~~Exception~"+ MainActivity.userMobileNo)

            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

                  false
        }
    }
    private fun sendSms() {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            Log.d("222","~sendSms~userMobileNo~"+ MainActivity.userMobileNo)

            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendMultimediaMessage(ctx,MainActivity.file.toUri() ,null,null,null)
                smsManager.sendTextMessage(
                    MainActivity.userMobileNo,
                    null,
                    "Hi, There is a lot of noise from background, We will connect later.",
                    null,
                    null
                )
            }catch (e:Exception){
                Log.d("222","~~Exception~"+e)
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions( arrayOf(Manifest.permission.SEND_SMS), 10);
            }
        }

    }
    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConnectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}