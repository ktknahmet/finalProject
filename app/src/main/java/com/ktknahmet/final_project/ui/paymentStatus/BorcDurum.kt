package com.ktknahmet.final_project.ui.paymentStatus

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.gson.gson
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.AddFriendPaymentAdapter
import com.ktknahmet.final_project.adapter.AddPaymentAdapter
import com.ktknahmet.final_project.adapter.FriendsDetailsAdapter
import com.ktknahmet.final_project.databinding.DialogAmountBinding
import com.ktknahmet.final_project.databinding.FragmentBorcDurumBinding
import com.ktknahmet.final_project.model.AddFriendPayment
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.model.Contact
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.Constant
import com.ktknahmet.final_project.utils.Errors
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.niceDialog.BaseMyDialog
import com.ktknahmet.final_project.utils.niceDialog.ViewConvertListener
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import com.ktknahmet.final_project.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BorcDurum : BaseFragment<FragmentBorcDurumBinding>(FragmentBorcDurumBinding::inflate){
    private lateinit var listOdemeTip: ArrayList<String>
    private lateinit var listFriends: ArrayList<String>
    private var odemeTip = ""
    private var miktar = 0.0
    private var arkadas=""
    private lateinit var allList: ArrayList<AddFriendPayment>
    private var dateNow = Date()
    private lateinit var  pref : MainSharedPreferences
    private var email = ""
    @SuppressLint("SimpleDateFormat")
    private val sdf2 = SimpleDateFormat("dd-MM-yyyy")
    var selectDateTime = sdf2.format(dateNow)
    var odemeDate = sdf2.format(dateNow)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = MainSharedPreferences(requireActivity(), MyPref.bilgiler)
        email = pref.getString(MyPref.email, "").toString()

        allList = ArrayList()
        allFriends()
        odemeTipi()
        binding.selectDate.setText(selectDateTime)
        binding.selectDate.onClick {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                R.style.MaterialCalendarCustomStyle,
                { _, y, m, d ->
                    val ay = m + 1
                    selectDateTime = ("$d-$ay-$y")
                    binding.selectDate.setText(selectDateTime)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        binding.imgBtnSave.onClick {
            if (odemeTip.isEmpty() || arkadas.isEmpty()) {
                binding.tilodemeTip.error = Errors.BosGecilemez
                binding.tilarkadas.error = Errors.BosGecilemez
            } else {
                showAskAmountDialog()

            }
        }
    }
    private fun allFriends(){
        listFriends = ArrayList()
        var contactList= ArrayList<Contact>()
        var listGrup=""
        val gson = Gson()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val query =db.collection("arkadaslarim").whereEqualTo("EMAIL", email).get()

        query.addOnSuccessListener {
            if(!it.isEmpty){
                for(i in it.documents.indices){
                   val grupinfo = it.documents[i].get("GRUPINFO")


                    println("list grup :$grupinfo")

                    listGrup = gson.toJson(grupinfo)


                    contactList.addAll(gson.fromJson(listGrup, object: TypeToken<ArrayList<Contact>>() {}.type))
                }

                println("contact :$contactList  --${contactList.size}")
                for(i in contactList){
                    listFriends.add(i.name.toString())
                }

                val spinnerAdapter = activity?.let {list->
                    ArrayAdapter(list, R.layout.view_spinner_dropdown_item, listFriends)
                }!!

                binding.spinnerArkadas.setAdapter(spinnerAdapter)
                binding.spinnerArkadas.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, _, position, _ ->
                        arkadas = parent.getItemAtPosition(position) as String

                    }
            }else{
                binding.recyclerview.gone()
                toastError(str(R.string.arkadas_bos))
            }

        }.addOnFailureListener {
            toastError(it.message.toString())
        }
    }


    private fun odemeTipi() {
        listOdemeTip = ArrayList()
        listOdemeTip.addAll(Constant.ODEMETIPI)

        val spinnerAdapter = activity?.let {
            ArrayAdapter(it, R.layout.view_spinner_dropdown_item, listOdemeTip)
        }!!

        binding.spinnerOdeme.setAdapter(spinnerAdapter)
        binding.spinnerOdeme.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                odemeTip = parent.getItemAtPosition(position) as String

            }
    }
    private fun showAskAmountDialog() {
        showDialog(object : ViewConvertListener<DialogAmountBinding> {
            override fun provideViewBinding(
                inflater: LayoutInflater,
                container: ViewGroup?
            ): DialogAmountBinding {
                return DialogAmountBinding.inflate(inflater, container, false)
            }

            override fun convertView(
                vb: DialogAmountBinding,
                dialog: BaseMyDialog<DialogAmountBinding>
            ) {

                if (odemeTip == Constant.ODENECEK || odemeTip == Constant.TAKSITLIODEME) {
                    vb.tilSelectTarih.visible()
                    vb.selectTarih.setText(odemeDate)
                } else {
                    vb.tilSelectTarih.gone()

                }
                vb.selectTarih.onClick {
                    val cal = Calendar.getInstance()
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH)
                    val day = cal.get(Calendar.DAY_OF_MONTH)

                    val dpd = DatePickerDialog(
                        requireActivity(),
                        R.style.MaterialCalendarCustomStyle,
                        { _, y, m, d ->
                            val ay = m + 1
                            odemeDate = ("$d-$ay-$y")
                            vb.selectTarih.setText(odemeDate)
                        },
                        year,
                        month,
                        day
                    )
                    dpd.show()
                }
                vb.btnOK.onClick {
                    val date = sdf2.parse(selectDateTime)
                    val selectDate = date!!.time
                    val date2 = sdf2.parse(odemeDate)
                    val odemeDatee = date2!!.time

                    if (vb.edtAmount.text.toString().isEmpty()) {
                        vb.edtAmount.error = Errors.BosGecilemez
                    } else if (selectDate > odemeDatee) {
                        toastError(str(R.string.odeme_tarihi_buyuk_olmali))
                    } else {
                        miktar = vb.edtAmount.text.toString().toDouble()
                        addData()
                        dialog.dismiss()
                    }
                }
                vb.btnCancel.onClick {
                    dialog.dismiss()
                }
            }
        })
    }
    private fun addData() {
        val adSoyad = pref.getString(MyPref.fullName, "").toString()
        val phone = pref.getString(MyPref.phone, "").toString()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val date = sdf2.parse(selectDateTime)
        val selectDate = date!!.time
        val date2 = sdf2.parse(odemeDate)
        val odemeDatee = date2!!.time
        val add = HashMap<String, Any>()
        add["FULLNAME"] = adSoyad
        add["EMAIL"] = email
        add["PHONE"] = phone
        add["BUTCE"] = miktar
        add["ODEMETIP"] = odemeTip
        add["ARKADAS"] = arkadas
        add["TARIH"] = selectDate
        add["ODEMETARIH"] = odemeDatee


        val a = AddFriendPayment(miktar, email, arkadas, adSoyad, odemeTip, phone, selectDate, odemeDatee)

        db.collection("arkadasOdeme").add(add)
            .addOnSuccessListener {
                toastSuccess(str(R.string.ödeme_kaydedildi))
                allList.add(a)
                val mAdapter = AddFriendPaymentAdapter(allList)
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = mAdapter
                }
            }
            .addOnFailureListener {
                toastError(str(R.string.hata))
            }
    }
}