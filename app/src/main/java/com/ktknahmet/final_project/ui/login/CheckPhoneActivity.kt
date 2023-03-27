package com.ktknahmet.final_project.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.telephony.SmsManager
import com.cafermertceyhan.postmanlib.Postman
import com.crazylegend.kotlinextensions.views.clear
import com.crazylegend.kotlinextensions.views.onClick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ktknahmet.final_project.MainActivity
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.databinding.ActivityCheckPhoneBinding
import com.ktknahmet.final_project.ui.base.BaseActivity
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.appCtx
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import com.ktknahmet.final_project.utils.showConfirmDialog

import java.util.*
import java.util.concurrent.TimeUnit



class CheckPhoneActivity : BaseActivity(), MultiplePermissionsListener {
    private lateinit var pref: MainSharedPreferences
    private lateinit var binding: ActivityCheckPhoneBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var smsManager: SmsManager
    private lateinit var timer: CountDownTimer
    private var randomPin=0
    private var email =""
    private var sifre = ""
    private var phone = ""
    private var fullname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckPhoneBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        checkPermission()
        email = intent.getStringExtra("mail").toString()
        sifre = intent.getStringExtra("pass").toString()
        phone = intent.getStringExtra("phone").toString()
        fullname = intent.getStringExtra("fullName").toString()

        binding.tvResendVerificationCode.onClick {
            timer.cancel()
            binding.pinView.clear()
            startCountDownTimer()
            generateOTP()
        }



        binding.btnSMSValidate.setOnClickListener {
            val userCode = binding.pinView.text.toString()

           if(randomPin.toString() == userCode){
               addUserData()
           }else{
               toastSuccess(str(R.string.kimlik_dogrulanmadi))
               binding.pinView.clear()
           }
        }

    }
   private fun generateOTP() {
        randomPin = (Math.random() * 9000).toInt() + 1000
       val message ="Lütfen Hesabınızı Doğrulamak için $randomPin kodunu giriniz"
       val uid =str(R.string.randomPhone)

       println("uid :$uid")
       smsManager.sendTextMessage(uid, null, message, null, null)

    }
    private fun smsRetriever() {
        @Suppress("UNUSED_VARIABLE") val postman = Postman(this) // Activity or Fragment
            .getJustVerificationCode(true)
            .verificationCodeSize(4)
            .message()
            .subscribe { verificationCode ->
                val chars = verificationCode?.toCharArray()
                if (chars?.size == 4) {
                    binding.pinView.setText(verificationCode)
                } else {
                    toastWarning(str(R.string.warning_pin_code))
                }
            }
    }
    private fun startCountDownTimer() {
        //objecy oluşturarak CountDownTimer fonksiyonunu alırız ve burada süreyi ve her saniye geçen süreyi parametre olarak veririz
        timer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvSMSDuration.text = hmsTimeFormatter(millisUntilFinished)
            }

            override fun onFinish() {
            }
        }
        timer.start()
    }
    private fun hmsTimeFormatter(milliSeconds: Long): String {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(milliSeconds),
            TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    milliSeconds
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milliSeconds
                )
            )
        )
    }
    private fun addUser(email:String,password:String){
        auth = Firebase.auth
        pref= MainSharedPreferences(this,MyPref.bilgiler)
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            toastSuccess(str(R.string.kullanıcı_olustu))

            pref.storeString(MyPref.fullName, fullname)
            pref.storeString(MyPref.email, email)
            pref.storeString(MyPref.phone, phone)
            pref.storeString(MyPref.photoUrl,null)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            it.localizedMessage?.let { it1 -> toastError(it1) }
        }
    }

    private fun checkPermission() {
        Dexter.withContext(this)
                //kullanıcıya sms göndermek için gereken izini isteriz
            .withPermissions(
                android.Manifest.permission.SEND_SMS
            )
            .withListener(this)
            .check()
    }

    override fun onPermissionRationaleShouldBeShown(request: MutableList<PermissionRequest>?, token: PermissionToken?) {
        token?.continuePermissionRequest()
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        when {
            //eğer izin verilmediyse kullanıcıya dialog mesajı gösteririz ve intent sayesinde uygulamanın ayarlarına giden kodu yazarız
            report!!.isAnyPermissionPermanentlyDenied -> {
                showConfirmDialog(
                    str(R.string.lutfen_izinleri_veriniz),
                    confirm = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", appCtx.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    },
                    buttonPositiveText = str(R.string.ayarlari_ac)
                )
            }
            report.areAllPermissionsGranted() -> {
                smsManager= SmsManager.getDefault()
                generateOTP()
                startCountDownTimer()
                smsRetriever()
            }
        }
    }

    private fun addUserData(){
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val add = HashMap<String,Any>()
        add["FULLNAME"] = fullname
        add["EMAIL"] = email
        add["PHONE"] = phone
        add["SIFRE"] = sifre
        add["PHOTOURL"] =""

        db.collection("userData").add(add).addOnSuccessListener {
            addUser(email,sifre)
        }.addOnFailureListener {
            toastError(it.toString())
        }

    }

}