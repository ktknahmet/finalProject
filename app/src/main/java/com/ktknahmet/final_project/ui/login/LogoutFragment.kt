package com.ktknahmet.final_project.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.kotlinextensions.fragments.finish
import com.crazylegend.kotlinextensions.views.onClick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ktknahmet.final_project.MainActivity
import com.ktknahmet.final_project.databinding.FragmentLogoutBinding
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.viewModel.LoginViewModel


class LogoutFragment : BaseFragment<FragmentLogoutBinding>(FragmentLogoutBinding::inflate) {
    private lateinit var auth:FirebaseAuth
    private val viewModel : LoginViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = Firebase.auth

        binding.btnOk.onClick {
           auth.signOut()
           val intent = Intent(requireContext(),MainActivity::class.java)
           startActivity(intent)
           finish()

       }

    }


}