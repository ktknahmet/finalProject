package com.ktknahmet.final_project.ui.spending


import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.AddPaymentAdapter
import com.ktknahmet.final_project.databinding.FragmentSpendingBinding
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.Constant.BORCTIP
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.navigate
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import com.ktknahmet.final_project.utils.showConfirmDialog
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SpendingFragment : BaseFragment<FragmentSpendingBinding>(FragmentSpendingBinding::inflate) {
    private lateinit var listFatura: ArrayList<String>
    private lateinit var allList: ArrayList<AddPayment>
    private lateinit var pref: MainSharedPreferences
    private lateinit var mAdapter: AddPaymentAdapter
    private var faturaTip = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        allList = ArrayList()
        faturalar()

        binding.imgPdf.onClick {
            showConfirmDialog(
                title = str(R.string.pdf_olusturmak_istermisiniz),
                confirm = {

                },
                buttonPositiveText = str(R.string.kaydet),
                buttonNegativeText = str(R.string.hayir),
            )
        }
    }

    private fun faturalar() {
        listFatura = ArrayList()
        listFatura.addAll(BORCTIP)

        val spinnerAdapter = activity?.let {
            ArrayAdapter(it, R.layout.view_spinner_dropdown_item, listFatura)
        }!!

        binding.spinnerFatura.setAdapter(spinnerAdapter)
        binding.spinnerFatura.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                faturaTip = parent.getItemAtPosition(position) as String
                allData(faturaTip)
            }
    }


    private fun allData(faturatip: String) {
        pref = MainSharedPreferences(requireContext(), MyPref.bilgiler)
        val email = pref.getString(MyPref.email, "").toString()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val query =db.collection("odemeler").whereEqualTo("FATURATIP", faturatip)
            .whereEqualTo("EMAIL",email).orderBy("TARIH",Query.Direction.ASCENDING).get()
        query.addOnSuccessListener {
           if(!it.isEmpty){
               for(data in it.documents){
                   val user = data.toObject(AddPayment::class.java)
                   allList.add(user!!)
               }
               binding.recyclerview.visible()
               binding.imageView.gone()
               binding.titleTextView.gone()
               mAdapter = AddPaymentAdapter(allList)
               binding.recyclerview.apply {
                   layoutManager = LinearLayoutManager(requireContext())
                   this.adapter = mAdapter
               }
           }else{
               binding.recyclerview.gone()
               binding.imageView.visible()
               binding.titleTextView.visible()
               toastError(str(R.string.veri_yok))
           }

        }.addOnFailureListener {
            toastError(it.message.toString())
        }
        allList = ArrayList()
    }

}