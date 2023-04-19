package com.ktknahmet.final_project.ui.page

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.RelativeLayout
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.databinding.FragmentSettingsUserBinding
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.Constant.DISABLE
import com.ktknahmet.final_project.utils.Constant.ENABLE
import com.ktknahmet.final_project.utils.Constant.LIGHT
import com.ktknahmet.final_project.utils.Errors
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.onCheck
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import java.util.HashMap


class SettingsUserFragment : BaseFragment<FragmentSettingsUserBinding>(FragmentSettingsUserBinding::inflate){
    private lateinit var pref: MainSharedPreferences
    private var cameraUri: Uri? = null
    private var check = false
    private var myTheme: Int = LIGHT
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var  email=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        myTheme = pref.getInt(MyPref.theme, DISABLE)

        binding.swDarkTheme.isChecked = myTheme != LIGHT
        binding.swDarkTheme.onCheck {
            saveTheme(if (it) ENABLE else DISABLE)
        }
        binding.civPhoto.setOnClickListener {
            bottomSheet()
        }

        binding.btnSave.onClick {
            if(binding.edtPassword.text!!.isEmpty() || binding.edtPasswordAgain.text!!.isEmpty()){
                binding.tilPass.error = Errors.BosGecilemez
                binding.tilPassAgain.error = Errors.BosGecilemez
            }else if(binding.edtPassword.text.toString().length <6 || binding.edtPasswordAgain.text.toString().length <6){
                binding.tilPass.error = Errors.minPass
                binding.tilPassAgain.error = Errors.minPass
            }else if(binding.edtPassword.text!!.toString() == binding.edtPasswordAgain.text!!.toString()){
                updateData()

            }else{
                toastInfo(str(R.string.sifreler_aynidegil))
            }

        }
    }



    private fun init() {
        pref = MainSharedPreferences(requireContext(), MyPref.bilgiler)
        val adSoyad = pref.getString(MyPref.fullName, "").toString()
        email = pref.getString(MyPref.email, "").toString()
        val phone = pref.getString(MyPref.phone, "").toString()

        firestore = Firebase.firestore
        storage = Firebase.storage
        binding.tvFullName.text = email
        if(phone.isNotEmpty()){
            binding.addTelefon.setText(phone)
        }
        binding.addAdsoyad.setText(adSoyad)

    }

    private fun saveTheme(theme: Int) {
        pref.storeInt(MyPref.theme, theme)
        if (theme != myTheme) {
            val intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
        }
    }


    private fun bottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.fragment_photo)
        val camera = dialog.findViewById<RelativeLayout>(R.id.kemaraTikla)
        val gallery = dialog.findViewById<RelativeLayout>(R.id.galeriTikla)

        gallery!!.setOnClickListener {
            check = false
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(intent, 100)

            dialog.dismiss()
        }
        camera!!.setOnClickListener {
            check = true
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            cameraUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
            startActivityForResult(intent, 1001)

            dialog.dismiss()
        }
        dialog.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!check) {
            if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
                binding.civPhoto.setImageURI(data?.data)
                cameraUri = data?.data
                pref.storeString(MyPref.photoUrl,cameraUri.toString())
            }
        } else {
            binding.civPhoto.setImageURI(cameraUri)
            pref.storeString(MyPref.photoUrl,cameraUri.toString())
        }


    }

    private fun updateData(){
        binding.pgBar.visible()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val add = HashMap<String,Any>()
        add["FULLNAME"] = binding.addAdsoyad.text.toString()
        add["EMAIL"] = email
        add["PHONE"] = binding.addTelefon.text.toString()
        add["SIFRE"] = binding.edtPassword.text.toString()
        add["PHOTOURL"] =cameraUri.toString()

        db.collection("userData").whereEqualTo("EMAIL",email).get().addOnSuccessListener {
            for(document in it){
                document.reference.update(add)
            }
            binding.pgBar.gone()
            toastSuccess(str(R.string.bilgiler_guncellendi))
        }.addOnFailureListener {
            toastError(it.toString())
            binding.pgBar.gone()
        }

    }

}

