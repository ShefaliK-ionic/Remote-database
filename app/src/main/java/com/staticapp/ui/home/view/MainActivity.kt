package com.staticapp.ui.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.role.RoleManager
import android.content.*
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Telephony
import android.provider.Telephony.Carriers
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.InCallService
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.mms.transaction.MmsMessageSender
import com.google.android.material.tabs.TabLayoutMediator
import com.klinker.android.send_message.*
import com.staticapp.MyApp
import com.staticapp.MyPhoneStateListener
import com.staticapp.R
import com.staticapp.ServiceForDeviceB
import com.staticapp.databinding.ActivityMainBinding
import com.staticapp.ui.home.view.tab_fragment.tab_adapter.ConnectionTabAdapter
import com.staticapp.utils.MyUtils.readBytes
import com.staticapp.utils.SMSSettings
import com.staticapp.utils.getFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toLongOrDefault
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Constructor
import java.lang.reflect.Method


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private var myService: ServiceForDeviceB? = null
    private var isBound = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setupTab()

//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,  IntentFilter("custom-action-local-broadcast"));

//        startDeviceAService()
        askPermission()
//        detectCall()
//        telephonyInsideRec()
//        playOnAudioFocus()
        clickEvent()
//       playAudio()

//        requestRole()

//        val intent_upload = Intent()
//        intent_upload.setType("audio/*")
//        intent_upload.setAction(Intent.ACTION_GET_CONTENT)
//        someActivityResultLauncher.launch(intent_upload)
        handleBackPressCondition()
        initSettings()
//        sendSmsWithNew()

        initActions()
        BroadcastUtils.sendExplicitBroadcast(this, Intent(), "test action")
    }

  //##########################
  open fun sendMmsOnly(phoneNumber: String?, mediaUri: Uri?, messageText: String?) {
      try {
          val contentResolver = contentResolver
          val projection = arrayOf("_id")
          val values = ContentValues()
          values.put("address", "6232412318")
          values.put("text", messageText)
          val mmsUri = Uri.parse("content://mms")
          val mmsPartUri = Uri.parse("content://mms/part")
          val mmsUriReturned = contentResolver.insert(mmsUri, values)
          val mmsId = mmsUriReturned!!.lastPathSegment.toString()

          // Add multimedia attachment
          addMmsPart(contentResolver, mmsPartUri, mmsId, mediaUri!!)

          // Send MMS
          sendMms(contentResolver, mmsUri, mmsId)
      } catch (e: Exception) {
          e.printStackTrace()
      }
  }

    private fun addMmsPart(
        contentResolver: ContentResolver,
        mmsPartUri: Uri,
        mmsId: String,
        mediaUri: Uri
    ) {
        val inputStream = contentResolver.openInputStream(mediaUri)
        val mediaData: ByteArray = readBytes(inputStream!!)!!
        inputStream!!.close()
        val values = ContentValues()
        values.put("mid", mmsId)
        values.put("blob_col",mediaData)
//        values.put("ct", "image/jpeg")
        values.put("ct", "audio/wav")
        val partUri = contentResolver.insert(mmsPartUri, values)
//        contentResolver.update(partUri, mediaData, null, null);

        contentResolver.update(partUri!!, values, null, null)
    }

    private fun sendMms(contentResolver: ContentResolver, mmsUri: Uri, mmsId: String) {
        val values = ContentValues()
        values.put("msg_box", 2)
        contentResolver.update(mmsUri, values, "_id=$mmsId", null)
    }

//    private fun readBytes(inputStream: InputStream): ByteArray? {
//        val byteBuffer = ByteArrayOutputStream()
//        val bufferSize = 1024
//        val buffer = ByteArray(bufferSize)
//        var len: Int
//        while (inputStream.read(buffer).also { len = it } != -1) {
//            byteBuffer.write(buffer, 0, len)
//        }
//        return byteBuffer.toByteArray()



    //
    private fun initSettings() {
        settings = SMSSettings.get(this)
        if (TextUtils.isEmpty(settings!!.mmsc) &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
        ) {
            Log.d("222","~~~" +
                    "settings!!.mmsc~~~")
            initApns()
        }
    }


    private fun initApns() {
        ApnUtils.initDefaultApns(
            this
        ) { settings = SMSSettings.get(this@MainActivity, true) }
    }
//
 fun initActions() {
    if (Utils.isDefaultSmsApp(this)) {
        activityMainBinding.btnInvoke.setVisibility(View.GONE)
    } else {
        activityMainBinding.btnInvoke.setOnClickListener(View.OnClickListener { setDefaultSmsApp() })
    }
    activityMainBinding.btnEndCall.setOnClickListener(View.OnClickListener {
//        initApns()
//    sendSmsWithNew()
        sendSms()
    })
//    fromField.setText(Utils.getMyPhoneNumber(this))
//    toField.setText(Utils.getMyPhoneNumber(this))
//    imageToSend.setOnClickListener(View.OnClickListener { toggleSendImage() })
//    sendButton.setOnClickListener(View.OnClickListener { sendMessage() })
//    log.setHasFixedSize(false)
//    log.setLayoutManager(LinearLayoutManager(this))
//    logAdapter = LogAdapter(ArrayList<String>())
//    log.setAdapter(logAdapter)
}

    var someActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult?> { result ->
//            if (result. === RESULT_OK) {
                // Here, no request code
            result?.data?.let {
                val data: Intent=it
//                var file= File(data.data.toString())

//

                getFile(this@MainActivity, Uri.parse(it.data.toString()))?.let {
                    file =it
                    Log.d("222","~~~uri"+file?.absolutePath+"~~~"+file?.path)

                    sendVoice(file!!)
                }
                //
//                activityMainBinding.voiseasudio.setAudio(file.absolutePath)
               }


//                doSomeOperations()
//            }
        })

    private fun sendVoice(file: File) {
        //1srt
//        val f = File("full audio path")
//        val uri = Uri.parse(file.absolutePath)
//        val share = Intent(Intent.ACTION_SEND)
//        share.putExtra(Intent.EXTRA_STREAM, uri)
//        share.setType("audio/*")
//        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivity(Intent.createChooser(share, "Share audio File"))

        //2nd sent MAIN
        if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            Log.d("222","~sendSms~userMobileNo~"+ MainActivity.userMobileNo)
            Log.d("222","~sendSms~userMobileNo~"+ file.path)


            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(
                    MainActivity.userMobileNo,
                    null,
                    "Hi, There is a lot of noise from background, We will connect later."+file.path.toString(),
                    null,
                    null
                )
                smsManager.sendMultimediaMessage(this@MainActivity,Uri.parse(file.path.toString()) ,null,null,null)
//               Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
//smsManager.sendDataMessage()
            }catch (e:Exception){
                Log.d("222","~~Exception~"+e)
            }
        }
        else
        {   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions( arrayOf(Manifest.permission.SEND_SMS), 10);
            }
        }

        //3rd
//        val sendIntent = Intent(Intent.ACTION_SEND)
//        sendIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity")
//        sendIntent.putExtra("address", "6232412318")
//        sendIntent.putExtra("sms_body", "if you are sending text")
////        val file1: File = File(mFileName)
//        if (file.exists()) {
//            Log.d("222","file is exist")
////        }
////        val uri = Uri.fromFile(file)
//        val uri =FileProvider.getUriForFile(this@MainActivity,
//            BuildConfig.APPLICATION_ID + ".fileprovider", file);
//            Log.d("222","file is exist"+file.path)
//            sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
//        sendIntent.setType("audio/*")
//        startActivity(sendIntent)


        //4th

    }

    protected fun onActivityResult(p0: Int?, p1: Int?, p2: Intent?) {
        Log.d("222","~p0~~"+p0+"~~"+p1)
        if (p0 == 1) {
            if (p1 == RESULT_OK) {

                //the selected audio.
                val uri = p2?.data
                Log.d("222","~~~uri"+uri)
//                connectionFragBinding.voiseasudio.setAudio(uri.toString())
            }
        }
        super.onActivityResult(p0!!, p1!!, p2)
    }

    private fun handleBackPressCondition() {
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("222","Back button pressed")
                if(activityMainBinding.viewPager.currentItem == 1){
                    activityMainBinding.viewPager.setCurrentItem(0)
                } else {
                    finish()
                }
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed() // with this line
    }


    public fun changeTabSelection(pos:Int) {

        var viewPagerItemCount =
            activityMainBinding.viewPager.adapter?.itemCount
        Log.d("222","~viewPagerItemCount~~~"+viewPagerItemCount)
        if(viewPagerItemCount!=null && viewPagerItemCount > -1 && viewPagerItemCount > pos) {
            activityMainBinding.viewPager.setCurrentItem(pos)
         }
    }


    private fun setupTab() {

        var tabTitles= arrayOf(resources.getString(R.string.connection),resources.getString(R.string.custom_message ))
        var connectionTabAdapter= ConnectionTabAdapter(supportFragmentManager,lifecycle)
        connectionTabAdapter.also { activityMainBinding.viewPager.adapter = it }
        TabLayoutMediator(activityMainBinding.  tabLayout, activityMainBinding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }


    private fun clickEvent() {
//        setDefaultSmsApp()
//        activityMainBinding.btnEndCall.setOnClickListener {
//
//
////            try {
////                sendSmsWithNew()
//                var isCallDisconnect=endCall()//main
//
//               Log.d("222","~~isCallDisconnect~~")
////            } catch (ignored: Exception) {
////            }
//        }
    }
    private var settings: SMSSettings? = null
    private fun setDefaultSmsApp() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            val roleManager = getSystemService(RoleManager::class.java)
            val roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
            startActivityForResult(roleRequestIntent, 12)
        } else {
            val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
            startActivity(intent)
        }
//        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
//        intent.putExtra(
//            Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
//            packageName
//        )
//        startActivity(intent)
    }

    private val APN_PROJECTION = arrayOf(
        Carriers.TYPE,  // 0
        Carriers.MMSC,  // 1
        Carriers.MMSPROXY,  // 2
        Carriers.MMSPORT // 3
    )
    @SuppressLint("Range")
    fun sendSmsWithNew() {

        Thread {
        try {
            //
//            var cursor: Cursor? = null
//            cursor =
//                SqliteWrapper.query(
//                    this@MainActivity,
//                    getContentResolver(),
//                    Uri.withAppendedPath(Carriers.CONTENT_URI, "current"),
//                    APN_PROJECTION,
//                    null,
//                    null,
//                    null
//                )


//            cursor?.moveToLast()
//            cursor?.let {
//
//
//                val type = cursor?.getString(cursor.getColumnIndex(Carriers.TYPE))
//                val mmsc = cursor.getString(cursor.getColumnIndex(Carriers.MMSC))
//                val proxy = cursor.getString(cursor.getColumnIndex(Carriers.MMSPROXY))
//                val port = cursor.getString(cursor.getColumnIndex(Carriers.MMSPORT))
//                Log.d("222","~~~mmsc~~~"+mmsc+"~proxy~"+proxy+"~~port~"+port)

                val sendSettings = Settings()
                sendSettings.mmsc = settings!!.mmsc
//                sendSettings.mmsc = mmsc
                sendSettings.proxy = settings!!.mmsProxy
//                sendSettings.proxy = proxy
                sendSettings.port = settings!!.mmsPort
//                sendSettings.port = port
                sendSettings.useSystemSending = true
                val transaction = Transaction(this@MainActivity, sendSettings)
                val message = Message("this text7", "6232412318")
//                message.setImage(BitmapFactory.decodeResource(resources, com.staticapp.R.drawable.img_sign_off))
//                message.setAudio(BitmapFactory.decodeResource(resources, com.staticapp.R.drawable.img_sign_off))
//                val inStream: InputStream = getResources().openRawResource(com.staticapp.R.raw.audio_n)
//                val music = ByteArray(inStream.available())
//                message.addAudio(music)


//            val baos = ByteArrayOutputStream()
//            val buff = ByteArray(10240)
//            var i = Int.MAX_VALUE
//            while (true) {
//                try {
//                    if (inStream.read(buff, 0, buff.size).also { i = it } <= 0) break
//
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                baos.write(buff, 0, i)
//            }


//            val baos = ByteArrayOutputStream()
//            val buff = ByteArray(10240)
//            var i = Int.MAX_VALUE
//            while ((i = inStream.read(buff, 0, buff.length)) > 0) {
//                baos.write(buff, 0, i)
//            }
//            message.setAddress(music,"audio_my.wav")
//            message.addAudio(music)

                transaction.sendNewMessage(message, Transaction.NO_THREAD_ID)
                Log.d("222", "~~sendNewMessage~~56~~" )
//            }


            //
//            val sendSettings = Settings()
//            sendSettings.mmsc = settings!!.mmsc
//            sendSettings.proxy = settings!!.mmsProxy
//            sendSettings.port = settings!!.mmsPort
//            sendSettings.useSystemSending = true
//            val transaction = Transaction(this@MainActivity, sendSettings)
//            val message = Message("this text7", "6232412318")
////                message.setImage(BitmapFactory.decodeResource(resources, com.staticapp.R.drawable.img_sign_off))
////                message.setAudio(BitmapFactory.decodeResource(resources, com.staticapp.R.drawable.img_sign_off))
//            val inStream: InputStream = getResources().openRawResource(com.staticapp.R.raw.audio_n)
//            val music = ByteArray(inStream.available())
//            message.addAudio(music)
////            message.setAddress(music,"audio_my.wav")
////            message.addAudio(music)
//
//            transaction.sendNewMessage(message, Transaction.NO_THREAD_ID)
//            Log.d("222", "~~sendNewMessage~~~~" + music)
        }catch (e:Exception){
            Log.d("222", "~~sendNewMessage~~~Exception~" + e)

        }
        }.start()
    }
//    private fun sendSmsWithNew() {
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    com.klinker.android.send_message.Settings sendSettings = new com.klinker.android.send_message.Settings();
//                    sendSettings.setMmsc(settings.getMmsc());
//                    sendSettings.setProxy(settings.getMmsProxy());
//                    sendSettings.setPort(settings.getMmsPort());
//                    sendSettings.setUseSystemSending(true);
//
//                    Transaction transaction = new Transaction(MainActivity.this, sendSettings);
//
//                    Message message = new Message(messageField.getText().toString(), toField.getText().toString());
//
//                    if (imageToSend.isEnabled()) {
//                        message.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.android));
//                    }
//
//                    transaction.sendNewMessage(message, Transaction.NO_THREAD_ID);
//                }
//            }).start();
//
//
//
//
//        val sendSettings = com.klinker.android.send_message.Settings()
//        sendSettings.setMmsc(settings?.mmsc);
//        sendSettings.setProxy(settings?.mmsProxy);
//        sendSettings.setPort(settings?.mmsPort);
//        sendSettings.setUseSystemSending(true);
//    }

    private fun endCalNew() {

        try {
            //String serviceManagerName = "android.os.IServiceManager";
            val serviceManagerName = "android.os.ServiceManager"
            val serviceManagerNativeName = "android.os.ServiceManagerNative"
            val telephonyName = "com.android.internal.telephony.ITelephony"
            val telephonyClass: Class<*>
            val telephonyStubClass: Class<*>
            val serviceManagerClass: Class<*>
            var serviceManagerStubClass: Class<*>
            val serviceManagerNativeClass: Class<*>
            var serviceManagerNativeStubClass: Class<*>
            var telephonyCall: Method
            val telephonyEndCall: Method
            var telephonyAnswerCall: Method
            var getDefault: Method
            var temps: Array<Method?>
            var serviceManagerConstructor: Array<Constructor<*>?>

            // Method getService;
            val telephonyObject: Any
            val serviceManagerObject: Any
            telephonyClass = Class.forName(telephonyName)
            telephonyStubClass = telephonyClass.classes[0]
            serviceManagerClass = Class.forName(serviceManagerName)
            serviceManagerNativeClass = Class.forName(serviceManagerNativeName)
            val getService =  // getDefaults[29];
                serviceManagerClass.getMethod("getService", String::class.java)
            val tempInterfaceMethod = serviceManagerNativeClass.getMethod(
                "asInterface", IBinder::class.java
            )
            val tmpBinder = Binder()
            tmpBinder.attachInterface(null, "fake")
            serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder)
            val retbinder = getService.invoke(serviceManagerObject, "phone") as IBinder
            val serviceMethod = telephonyStubClass.getMethod(
                "asInterface",
                IBinder::class.java
            )
            telephonyObject = serviceMethod.invoke(null, retbinder)
            //telephonyCall = telephonyClass.getMethod("call", String.class);
            telephonyEndCall = telephonyClass.getMethod("endCall")
            //telephonyAnswerCall = telephonyClass.getMethod("answerRingingCall");
            telephonyEndCall.invoke(telephonyObject)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

            Log.d("222", "Exception object: "+e)
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    fun endCall(/*context: Context*/): Boolean {
        return try {
            //for higher
            val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//            for other
            val method = tm.javaClass.getDeclaredMethod("getITelephony")
            method.isAccessible = true
            val telephonyService = method.invoke(tm)
            val endCallMethod = telephonyService.javaClass.getDeclaredMethod("endCall")
            Log.d("222","~~~~telephonyService~~~"+telephonyService.javaClass.declaredMethods.toString())
            endCallMethod.invoke(telephonyService)
//            for( i in 0 until tm.javaClass.declaredMethods.size) {
//                Log.d("222", "~~~~telephonyService~~~" + tm.javaClass.declaredMethods[i].name)
//            }

            Toast.makeText(this@MainActivity,"End call ",Toast.LENGTH_LONG).show()

            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ANSWER_PHONE_CALLS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager?.endCall()
            }


            Log.d("222","~~~~telephonyService~~Exception~"+ userMobileNo)
            sendSms()

//            val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//            //for oplus 10
//            val method = tm.javaClass.getDeclaredMethod("endCall")
//            method.setAccessible(true);
//            method.invoke(tm)
//            Toast.makeText(this@MainActivity,"End call userMobileNo"+userMobileNo,Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun sendSms() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
//            val uri = Uri.parse("smsto:xxxxxxx")
//            val intent = Intent(Intent.ACTION_SENDTO, uri)
//            intent.putExtra("sms_body", "Hi, There is a lot of noise from background, We will connect later.")
//            startActivity(intent)
              try {

//                  val pendingIntent: PendingIntent
//                  val intent=Intent(this,MainActivity::class.java)
//                  pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                      PendingIntent.getActivity(
//                          this,
//                          0,
//                          intent,
//                          PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                      )
//                  } else {
//                      PendingIntent.getActivity(
//                          this,
//                          0, intent, PendingIntent.FLAG_UPDATE_CURRENT
//                      )
//                  }

                  val inStream: InputStream = getResources().openRawResource(com.staticapp.R.raw.audio_n)
                val music = ByteArray(inStream.available())
//                message.addAudio(music)
//            message.setAddress(music,"audio_my.wav")

                 var  buf =music;
//                    var str = String(buf, "UTF-8");
//                  var uri = Uri.parse(str);

                  val smsManager = SmsManager.getDefault()
                  smsManager.sendDataMessage(userMobileNo+"","9424059388",
                      623,music,null,null)
//                  smsManager.sendMultimediaMessage(this,uri, userMobileNo,null,null)
//                  smsManager.sendMultimediaMessage()
//                  smsManager.sendTextMessage(
//                      userMobileNo,
//                      null,
//                      "Hi, There is a lot of noise from background, We will connect later.",
//                      null,
//                      null
//                  )
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


//    private val roleManager by lazy { getSystemService(RoleManager::class.java) }
//
//    private fun requestRole() {
//
//        val intent = roleManager.createRequestRoleIntent(ROLE_CALL_SCREENING)
//        @Suppress("DEPRECATION")
//        startActivityForResult(intent,
//            200
//        )
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun playOnAudioFocus() {
        playbackAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        // set the playback attributes for the focus requester

        // set the playback attributes for the focus requester
        val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(playbackAttributes!!)
            .setAcceptsDelayedFocusGain(true).setFocusGain(1)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()

//        val am = MyApp.appContext.getSystemService(AUDIO_SERVICE) as AudioManager
//        am.mode = AudioManager.MODE_IN_CALL
//        am.isSpeakerphoneOn = true
//                val mp: MediaPlayer = MediaPlayer.create(MyApp.appContext, R.raw.audio_2)
////                mp.prepare()
//                mp.start()

        var audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
//                        audioManager.playAudio("https://example.com/audio.mp3")
        audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)
        audioManager.mode = AudioManager.MODE_IN_CALL//MODE_IN_CALL
        audioManager.isSpeakerphoneOn = true
        var mediaPlayer = MediaPlayer()
        val audioFocusRequest: Int = audioManager.requestAudioFocus( audioFocusChangeListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN)

        activityMainBinding.btnInvoke.setOnClickListener{
            if (audioFocusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.d("222","~~btnInvoke~~~")

             mediaPlayer.setAudioStreamType(AudioManager.MODE_IN_CALL)//STREAM_VOICE_CALL
            val audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"

            mediaPlayer.setDataSource(MyApp.appContext, Uri.parse(audioUrl))
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare()
            mediaPlayer.start()
                Toast.makeText(this@MainActivity,"Audio playing",Toast.LENGTH_SHORT).show()

                //
                val thread: Thread = object : Thread() {
                    override fun run() {
                        try {
                            while (true) {
                                sleep(1000)
                                audioManager.mode = AudioManager.MODE_IN_CALL
                                audioManager.setStreamVolume(AudioManager.MODE_IN_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
                                audioManager.setRouting(AudioManager.MODE_CURRENT, AudioManager.ROUTE_SPEAKER, AudioManager.ROUTE_ALL);

                                if (!audioManager.isSpeakerphoneOn) audioManager.isSpeakerphoneOn =
                                    true
                            }
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }
                thread.start()
                  //  connection.setAudioRoute(CallAudioState.ROUTE_SPEAKER)
//                } else {
//                    connection.setAudioRoute(CallAudioState.ROUTE_EARPIECE)
//                }


                //



        }else{
                Log.d("222","~~btnInvoke~11~~")

            }

        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("222","~~resultCode~~~~"+resultCode)
        if(resultCode== 200){
//bindMyService()

        }
    }


    private fun bindMyService(){
        Log.d("222", "binding my service")
        val mCallServiceIntent = Intent("android.telecom.CallScreeningService")
        mCallServiceIntent.setPackage(applicationContext.packageName)
        val mServiceConnection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                // iBinder is an instance of CallScreeningService.CallScreenBinder
                // CallScreenBinder is an inner class present inside CallScreenService
              Log.d("222","~~onServiceConnected~~~")
            }
            override fun onServiceDisconnected(componentName: ComponentName) {
                Log.d("222","~~onServiceDisconnected~~~")
            }
            override fun onBindingDied(name: ComponentName) {
                Log.d("222","~~onBindingDied~~~")
            }
        }
        bindService(mCallServiceIntent, mServiceConnection, BIND_AUTO_CREATE)
    }

    fun onPermissionsGranted(requestCode: Int, grantedPermissions: List<String>) {
        if (Manifest.permission.READ_PHONE_STATE in grantedPermissions) {
//            requestRole()
        }}

    private fun startDeviceAService() {
//        val intent = Intent(this, ServiceForDeviceB::class.java)
//        bindService(intent, connection, BIND_AUTO_CREATE)


        val intent_ = Intent(this, MyInCallService::class.java)
        startService(intent_)
    }


    fun invokeFunctionOnDeviceB(view: View?) {
       Log.d("222","~~invokeFunctionOnDeviceB~~~")

//        if (isBound && myService != null) {
//            myService!!.myFunction() // This will invoke myFunction on Device B
//        }
    }


    lateinit var mTelManager: TelephonyManager
    val mPhoneStateListener = MyPhoneStateListener()

    private fun telephonyInsideRec() {

        mTelManager =  getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        mTelManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


//    private fun askPermission() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_PHONE_STATE
//            ) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.MODIFY_AUDIO_SETTINGS
//            ) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ANSWER_PHONE_CALLS
//            ) != PackageManager.PERMISSION_GRANTED
//            || ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission. SEND_SMS
//            ) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.MODIFY_PHONE_STATE
//            ) != PackageManager.PERMISSION_GRANTED||
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CALL_PHONE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Request permissions
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf<String>(
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.ANSWER_PHONE_CALLS,Manifest.permission.MODIFY_PHONE_STATE,Manifest.permission.PROCESS_OUTGOING_CALLS,
//                    Manifest.permission.SEND_SMS
//                ),
//                12
//            )
//        }
//    }
private fun askPermission() {
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED ||

        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ANSWER_PHONE_CALLS
        ) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission. SEND_SMS
        ) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission. PROCESS_OUTGOING_CALLS
        ) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission. READ_SMS
        ) != PackageManager.PERMISSION_GRANTED
        ||  ContextCompat.checkSelfPermission(
            this,
            Manifest.permission. WRITE_VOICEMAIL
        ) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission. CHANGE_NETWORK_STATE
        ) != PackageManager.PERMISSION_GRANTED


    ) {
        // Request permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(
                Manifest.permission.READ_PHONE_STATE,
               Manifest.permission.ANSWER_PHONE_CALLS,  Manifest.permission.READ_SMS, Manifest.permission.WRITE_VOICEMAIL,
                Manifest.permission.SEND_SMS,Manifest.permission.PROCESS_OUTGOING_CALLS,
                Manifest.permission.CHANGE_NETWORK_STATE
            ),
            12
        )
    }
}
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun detectCall() {
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val phoneStateListener: PhoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                Log.d("222","~~~incomingNumber~~~~"+incomingNumber)
                when (state) {
                    TelephonyManager.CALL_STATE_IDLE -> {
                        Log.d("222","~~~CALL_STATE_IDLE~~~~")
//                        playAudio()
                    }
                    TelephonyManager.CALL_STATE_RINGING -> {
                        Log.d("222","~~~CALL_STATE_RINGING~~~~")

                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        Log.d("222","~~~CALL_STATE_OFFHOOK~~~~")
//                        playAudio()
                        var audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
//                        audioManager.playAudio("https://example.com/audio.mp3")
                        audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)

                    }
                }
            }
        }

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }


    class MyInCallService : InCallService() {
        override fun onCallAdded(call: Call) {
            // Handle incoming call.
            // To disconnect the call, you can use the endCall method.
            Log.d("222","~~onCallAdded~~~")
//            call.disconnect()
        }

        override fun onCallRemoved(call: Call?) {
            super.onCallRemoved(call)
            Log.d("222","~~onCallRemoved~~~")
        }

        override fun onCreate() {
            super.onCreate()
            Log.d("222","~~MyInCallService~~~")

        }





    }

companion object{
    lateinit var mCallDetails:Call.Details
    final var userMobileNo="6232412318"
    final lateinit var file:File

    fun playAudio() {
        val audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        var mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {


            // initiate the audio playback attributes

            // initiate the audio playback attributes


//                mediaPlayer.setDataSource(MyApp.appContext, Uri.parse(audioUrl))
//                // below line is use to prepare
//                // and start our media player.
//                mediaPlayer.prepare()
//                mediaPlayer.start()

            //
            Log.d("222","~~~audi plai~")

            val am = MyApp.appContext.getSystemService(AUDIO_SERVICE) as AudioManager
            am.mode = AudioManager.MODE_IN_COMMUNICATION
            am.isSpeakerphoneOn = true


//                val mp: MediaPlayer = MediaPlayer.create(MyApp.appContext, R.raw.audio_2)
////                mp.prepare()
//                mp.start()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)

            mediaPlayer.setDataSource(MyApp.appContext, Uri.parse(audioUrl))
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare()
            mediaPlayer.start()


        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("222","~~~IOException~~"+e)
        }

    }}

    class CallAppService : CallScreeningService() {
        override fun onScreenCall(callDetails: Call.Details) {
            Log.d("222", "Call screening service triggered")
            mCallDetails = callDetails
            //on call disconnect
            respondToCall(
                callDetails,
                CallResponse.Builder().setDisallowCall(true).setRejectCall(true).build()
            )
        }
    }






    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder: ServiceForDeviceB.MyBinder = service as ServiceForDeviceB.MyBinder
//            myService = ((ServiceForDeviceB.I)service).getService();
//            myService = (service as ServiceForDeviceB.MyBinder).getService()

            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        // we will receive data updates in onReceive method.
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getStringExtra("message")
            // on below line we are updating the data in our text view.
            activityMainBinding.btnInvoke.setText(message)
        }
    }

//    val audioFocusRequest: Int = audioManager.requestAudioFocus(focusRequest)

    var playbackAttributes: AudioAttributes? = null
    var audioFocusChangeListener =
        OnAudioFocusChangeListener { focusChange ->
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                Log.d("222","~~~~focusChange~~AUDIOFOCUS_GAIN~resume after lost~")
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                Log.d("222","~~~~focusChange~~AUDIOFOCUS_LOSS_TRANSIENT~~")

//                mediaPlayer.pause()
//                mediaPlayer.seekTo(0)
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
//                mediaPlayer.release()
                Log.d("222","~~~~focusChange~~AUDIOFOCUS_LOSS~~")


            }
        }}




