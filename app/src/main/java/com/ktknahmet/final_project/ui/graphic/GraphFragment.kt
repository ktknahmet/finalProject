package com.ktknahmet.final_project.ui.graphic


import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.isEmpty
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.github.mikephil.charting.data.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.databinding.FragmentGraphBinding
import com.ktknahmet.final_project.model.AddFriendPayment
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.model.Contact
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.Constant
import com.ktknahmet.final_project.utils.Constant.ALACAK
import com.ktknahmet.final_project.utils.Constant.BORC
import com.ktknahmet.final_project.utils.Constant.HEPSI
import com.ktknahmet.final_project.utils.Constant.ODENDI
import com.ktknahmet.final_project.utils.Constant.ODENECEK
import com.ktknahmet.final_project.utils.Constant.TAKSITLIODEME
import com.ktknahmet.final_project.utils.Constant.TLICON
import com.ktknahmet.final_project.utils.Constant.sdf2
import com.ktknahmet.final_project.utils.Errors
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import java.util.*
import kotlin.collections.ArrayList


class GraphFragment : BaseFragment<FragmentGraphBinding>(FragmentGraphBinding::inflate)  {

    private var dateNow = Date()
    private var arkadas=""
    private var email = ""
    private lateinit var  pref : MainSharedPreferences
    private var selectDateTime = sdf2.format(dateNow)
    private lateinit var listFriends: ArrayList<String>
    private lateinit var listOdemeTip: ArrayList<String>
    private lateinit var allListOdeme: ArrayList<AddPayment>
    private lateinit var arkadasOdeme:ArrayList<AddFriendPayment>
    private var odemeTip = ""
    private val longDate= sdf2.parse(selectDateTime)!!.time

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allListOdeme = ArrayList()
        arkadasOdeme = ArrayList()
        listFriends = ArrayList()
        pref = MainSharedPreferences(requireActivity(), MyPref.bilgiler)
        email = pref.getString(MyPref.email, "").toString()
        binding.selectDate.setText(selectDateTime)

        allFriends()
        odemeTipi()

        binding.imgShow.onClick {
            if(binding.spinnerOdeme.isEmpty()){
                binding.tilodemeTip.error = Errors.BosGecilemez
            }else{
                if(arkadas=="Hiçbiri" || arkadas.isEmpty()){
                    allListOdeme.clear()
                    arkadasOdeme.clear()
                    odemeTipData()
                }else{
                    allListOdeme.clear()
                    arkadasOdeme.clear()
                    arkadasData()
                }
            }
        }
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
                    selectDateTime = ("$d.$ay.$y")
                    binding.selectDate.setText(selectDateTime)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
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

                    }
            }else{
                toastError(str(R.string.arkadas_bos))
            }

        }.addOnFailureListener {
            toastError(it.message.toString())
        }
        binding.pgBar.gone()
    }
    private fun odemeTipi() {
        listOdemeTip = ArrayList()
        listOdemeTip.addAll(Constant.ODEMETIPI)
        listOdemeTip.add(HEPSI)

        val spinnerAdapter = activity?.let {
            ArrayAdapter(it, R.layout.view_spinner_dropdown_item, listOdemeTip)
        }!!

        binding.spinnerOdeme.setAdapter(spinnerAdapter)
        binding.spinnerOdeme.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                odemeTip = parent.getItemAtPosition(position) as String

            }
    }
    private fun odemeTipData() {
        binding.pgBar.visible()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val query = if(odemeTip==HEPSI){
            db.collection("odemeler").whereEqualTo("EMAIL",email)
                .whereLessThanOrEqualTo("ODEMETARIH",longDate).get()
        }else{
            db.collection("odemeler").whereEqualTo("EMAIL",email)
                .whereEqualTo("ODEMETIP", odemeTip).whereLessThanOrEqualTo("ODEMETARIH",longDate)
                .get()
        }
        query.addOnSuccessListener {
            if(!it.isEmpty){
                for(data in it.documents){
                    val user = data.toObject(AddPayment::class.java)
                    allListOdeme.add(user!!)
                }
                println("allData :${allListOdeme}")
                graph(1)
            }else{
                allListOdeme.clear()
                toastInfo("$selectDateTime tarihinden sonra $odemeTip girilmemiştir")
            }
            binding.pgBar.gone()
        }.addOnFailureListener {
            toastError(it.message.toString())
            binding.pgBar.gone()
        }
    }

    private fun arkadasData() {
        binding.pgBar.visible()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val query = if(odemeTip==HEPSI){
            db.collection("arkadasOdeme").whereEqualTo("EMAIL",email)
                .whereEqualTo("ARKADAS", arkadas).whereLessThanOrEqualTo("ODEMETARIH",longDate)
                .get()
        }else{
            db.collection("arkadasOdeme").whereEqualTo("ODEMETIP", odemeTip).whereEqualTo("EMAIL",email)
                .whereEqualTo("ARKADAS", arkadas).whereLessThanOrEqualTo("ODEMETARIH",longDate)
                .get()
        }

        query.addOnSuccessListener {
            if(!it.isEmpty){
                for(data in it.documents){
                    val user = data.toObject(AddFriendPayment::class.java)
                    arkadasOdeme.add(user!!)
                }
                println("allData :${arkadasOdeme}")
                graph(2)
            }else{
                arkadasOdeme.clear()
                toastInfo("$selectDateTime tarihinden sonra $odemeTip girilmemiştir")
            }
            binding.pgBar.gone()
        }.addOnFailureListener {
            toastError(it.message.toString())
            binding.pgBar.gone()
        }
    }
    private fun graph(value:Int){
        val list :ArrayList<PieEntry> = ArrayList()
        var odendiMiktar =0.0
        var odenecekMiktar=0.0
        var taksitliMiktar=0.0
        var borcMiktar=0.0
        var alacakMiktar=0.0

        if(value==1){
            for(i in allListOdeme.indices){
                when (allListOdeme[i].ODEMETIP) {
                    ODENDI -> { odendiMiktar +=allListOdeme[i].BUTCE!! }
                    ODENECEK -> { odenecekMiktar +=allListOdeme[i].BUTCE!! }
                    TAKSITLIODEME -> { taksitliMiktar +=allListOdeme[i].BUTCE!! }
                    BORC -> { borcMiktar +=allListOdeme[i].BUTCE!! }
                    ALACAK -> { alacakMiktar +=allListOdeme[i].BUTCE!! }
                }
            }
        }else{
            for(i in arkadasOdeme.indices){
                when (arkadasOdeme[i].ODEMETIP) {
                    ODENDI -> { odendiMiktar +=arkadasOdeme[i].BUTCE!! }
                    ODENECEK -> { odenecekMiktar +=arkadasOdeme[i].BUTCE!! }
                    TAKSITLIODEME -> { taksitliMiktar +=arkadasOdeme[i].BUTCE!! }
                    BORC -> { borcMiktar +=arkadasOdeme[i].BUTCE!! }
                    ALACAK -> { alacakMiktar +=arkadasOdeme[i].BUTCE!! }
                }
            }
        }
        println("ödendi miktar :$odendiMiktar")
        if(value==1){ binding.chartView.centerText = "Arkdaş Seçilmedi" }
        else{ binding.chartView.centerText = arkadas }
        if(odendiMiktar>0){ list.add(PieEntry(odendiMiktar.toFloat(),"$TLICON $ODENDI")) }
        if(odenecekMiktar>0){ list.add(PieEntry(odenecekMiktar.toFloat(),"$TLICON $ODENECEK")) }
        if(taksitliMiktar>0){ list.add(PieEntry(taksitliMiktar.toFloat(), "$TLICON $TAKSITLIODEME")) }
        if(borcMiktar>0){ list.add(PieEntry(borcMiktar.toFloat(), "$TLICON $BORC")) }
        if(alacakMiktar>0){ list.add(PieEntry(alacakMiktar.toFloat(), "$TLICON $ALACAK")) }

        val pieDataSet = PieDataSet(list,"")
        pieDataSet.setColors(Constant.LISTCOLOR,255)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 20f
        binding.chartView.setEntryLabelColor(Color.BLACK)
        val barData = PieData(pieDataSet)
        binding.chartView.data = barData

        binding.chartView.description.text = "$selectDateTime -- ${sdf2.format(dateNow)}"
        binding.chartView.animateXY(1500,1500)
        binding.chartView.visible()
    }
}