package com.ktknahmet.final_project.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.crazylegend.kotlinextensions.views.isEmpty
import com.ktknahmet.final_project.MainActivity
import com.ktknahmet.final_project.databinding.ActivityAddUserBinding
import com.ktknahmet.final_project.ui.base.BaseActivity
import com.ktknahmet.final_project.utils.Errors

class AddUserActivity : BaseActivity() {
    private lateinit var binding: ActivityAddUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarImage.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.edtPassword.doAfterTextChanged {
            val value: String = it.toString()
            when {
                value.length < 6 -> {
                    binding.tilPass.error = Errors.minPass
                }
                else -> {
                    binding.tilPass.error = null

                }
            }
        }
        binding.imgBtnSave.setOnClickListener {

            var email = binding.addEmail.text.toString()
            email = email.lowercase()
            val check = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
              if(binding.addAdsoyad.text!!.isEmpty() || email.isEmpty() || binding.addPhone.isEmpty() || binding.edtPassword.isEmpty()){
                  binding.tilAdsoyad.error = Errors.BosGecilemez
                  binding.tilEmail.error = Errors.BosGecilemez
                  binding.tilTelefon.error = Errors.BosGecilemez
                  binding.tilPass.error = Errors.BosGecilemez
              }else if(check){
                  val intent = Intent(this,CheckPhoneActivity::class.java)
                  intent.putExtra("mail", binding.addEmail.text.toString())
                  intent.putExtra("pass",binding.edtPassword.text.toString())
                  intent.putExtra("fullName",binding.addAdsoyad.text.toString())
                  intent.putExtra("phone",binding.addPhone.text.toString())
                  startActivity(intent)
                  finish()

              }else{
                  toastError(Errors.emailWrong)

              }
        }
    }






}