package com.ktknahmet.final_project

import android.content.Intent
import android.os.Bundle
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.isEmpty
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ktknahmet.final_project.databinding.ActivityMainBinding
import com.ktknahmet.final_project.ui.base.BaseActivity
import com.ktknahmet.final_project.ui.login.AddUserActivity
import com.ktknahmet.final_project.ui.page.MainHomeActivity
import com.ktknahmet.final_project.utils.Errors
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.resources.str
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import com.ktknahmet.final_project.utils.te
import com.ktknahmet.final_project.utils.ts

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var pref: MainSharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        init()
        val remember = pref.getBoolean(MyPref.remember,false)
        if(remember){
            val currentUser = auth.currentUser

            if(currentUser !=null){
                val intent = Intent(this, MainHomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        binding.tvForgotPassTitle.onClick {

            if(binding.edtUserName.isEmpty()){
                binding.tilUser.error = Errors.emailRequired
            }else{
                auth.sendPasswordResetEmail(binding.edtUserName.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        ts(str(R.string.sifre_gonderildi))
                    }else{
                        te(it.exception.toString())
                    }
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            if(binding.edtUserName.text!!.isEmpty() || binding.edtPassword.text!!.isEmpty() ){
                te(str(R.string.gerekli_alan))
            }
            if(binding.edtPassword.text!!.length<6){
                binding.tilPass.error = Errors.minPass
            }else{
                binding.pgBar.visible()
                signInWithPassword(binding.edtUserName.text.toString(),binding.edtPassword.text.toString())
            }

        }
    }

    private fun init(){
        pref= MainSharedPreferences(this,MyPref.bilgiler)
        auth = Firebase.auth
    }


    private fun signInWithPassword(email:String,password:String){
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    val user = auth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val idToken = tokenTask.result?.token
                            println("idToken :$idToken")
                            if(binding.cbRemember.isChecked){
                                pref.storeBoolean(MyPref.remember, true)
                            }else{
                                pref.storeBoolean(MyPref.remember, false)
                            }
                            pref.storeString(MyPref.email,binding.edtUserName.text.toString())
                            val intent = Intent(this, MainHomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }

        }.addOnFailureListener {
            binding.pgBar.gone()
            toastError(it.localizedMessage!!.toString())
                println("hataa :${it.localizedMessage!!}")
        }
    }

}