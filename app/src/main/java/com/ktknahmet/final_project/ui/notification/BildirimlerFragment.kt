package com.ktknahmet.final_project.ui.notification


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.AddFriendPaymentAdapter
import com.ktknahmet.final_project.adapter.AddPaymentAdapter
import com.ktknahmet.final_project.databinding.FragmentBildirimlerBinding
import com.ktknahmet.final_project.model.AddFriendPayment
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.model.Contact
import com.ktknahmet.final_project.utils.Constant.ODENECEK
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import kotlinx.coroutines.*


class BildirimlerFragment : BaseFragment<FragmentBildirimlerBinding>(FragmentBildirimlerBinding::inflate)  {
    private lateinit var allList: ArrayList<AddPayment>
    private lateinit var allListFriend: ArrayList<AddFriendPayment>
    private lateinit var pref: MainSharedPreferences
    private lateinit var mAdapter: AddPaymentAdapter
    private lateinit var mAdapterFriends : AddFriendPaymentAdapter
    private lateinit var listFriends: ArrayList<String>
    private var arkadas=""
    private var email = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listFriends = ArrayList()
        allList = ArrayList()
        allListFriend = ArrayList()
        pref = MainSharedPreferences(requireActivity(), MyPref.bilgiler)
        email = pref.getString(MyPref.email, "").toString()
        allFriends()


    }
    private fun allFriends(){
        binding.pgBar.visible()
        val contactList= ArrayList<Contact>()
        var listGrup: String
        val gson = Gson()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val query =db.collection("arkadaslarim").whereEqualTo("EMAIL", email).get()

        query.addOnSuccessListener {
            if(!it.isEmpty){
                for(i in it.documents.indices){
                    val grupinfo = it.documents[i].get("GRUPINFO")
                    listGrup = gson.toJson(grupinfo)
                    contactList.addAll(gson.fromJson(listGrup, object: TypeToken<ArrayList<Contact>>() {}.type))
                }
                for(i in contactList){
                    listFriends.add(i.name.toString())
                }
                listFriends.add("Hiçbiri")
                val spinnerAdapter = activity?.let {list->
                    ArrayAdapter(list, R.layout.view_spinner_dropdown_item, listFriends)
                }!!

                binding.spinnerArkadas.setAdapter(spinnerAdapter)
                binding.spinnerArkadas.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, _, position, _ ->
                        arkadas = parent.getItemAtPosition(position) as String

                        if(arkadas=="Hiçbiri"){
                            if(allList.isNotEmpty()){
                                allList.clear()
                            }
                            allData()
                        }else{
                            if(allListFriend.isNotEmpty()){
                                allListFriend.clear()
                            }
                            allDataFriends(arkadas)
                        }
                    }
            }else{
                toastInfo("Arkdaşlarınız arasındaki durumu görmek için arkadaş ekleyiniz")
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000)
                    allData()
                }
            }
            binding.pgBar.gone()

        }.addOnFailureListener {
            toastError(it.message.toString())
            binding.pgBar.gone()
        }

    }

    private fun allData() {
        binding.pgBar.visible()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val query =db.collection("odemeler").whereEqualTo("ODEMETIP",ODENECEK)
            .whereEqualTo("EMAIL",email)
            .orderBy("ODEMETARIH", Query.Direction.ASCENDING).get()
        query.addOnSuccessListener {
            if(!it.isEmpty){
                for(data in it.documents){
                    val user = data.toObject(AddPayment::class.java)
                    allList.add(user!!)
                }
                mAdapter = AddPaymentAdapter(allList)
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = mAdapter
                }
            }else{
                toastInfo("Ödenecek fatura bilgisi bulunmamaktadır")
            }
            binding.pgBar.gone()

        }.addOnFailureListener {
            toastError(it.message.toString())
            binding.pgBar.gone()
        }

    }
    private fun allDataFriends(friend:String) {
        binding.pgBar.visible()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val query =db.collection("arkadasOdeme").whereEqualTo("ODEMETIP",ODENECEK)
            .whereEqualTo("EMAIL",email).whereEqualTo("ARKADAS",friend)
            .orderBy("ODEMETARIH", Query.Direction.ASCENDING).get()
        query.addOnSuccessListener {
            if(!it.isEmpty){
                for(data in it.documents){
                    val user = data.toObject(AddFriendPayment::class.java)
                    allListFriend.add(user!!)
                }

                mAdapterFriends = AddFriendPaymentAdapter(allListFriend)
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = mAdapterFriends
                }
            }else{
                toastError("$arkadas ödenecek miktar yoktur")
            }
            binding.pgBar.gone()

        }.addOnFailureListener {
            toastError(it.message.toString())
            binding.pgBar.gone()
        }

    }
}