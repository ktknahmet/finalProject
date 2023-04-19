package com.ktknahmet.final_project.ui.graphic


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.databinding.FragmentGraphBinding
import com.ktknahmet.final_project.model.Contact
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.Constant
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import java.text.SimpleDateFormat
import java.util.*


class GraphFragment : BaseFragment<FragmentGraphBinding>(FragmentGraphBinding::inflate)  {
    @SuppressLint("SimpleDateFormat")
    private val sdf2 = SimpleDateFormat("dd-MM-yyyy")
    private var dateNow = Date()
    private var arkadas=""
    private var email = ""
    private lateinit var  pref : MainSharedPreferences
    private var selectDateTime = sdf2.format(dateNow)
    private lateinit var listFriends: ArrayList<String>
    private lateinit var listOdemeTip: ArrayList<String>
    private var faturaTip = ""
    private var odemeTip = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        pref = MainSharedPreferences(requireActivity(), MyPref.bilgiler)
        email = pref.getString(MyPref.email, "").toString()
        binding.selectDate.setText(selectDateTime)
        
        faturalar()
        allFriends()
        odemeTipi()

        binding.imgShow.onClick {
            graph()
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
                    selectDateTime = ("$d-$ay-$y")
                    binding.selectDate.setText(selectDateTime)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }
    private fun faturalar() {
        val listFatura: ArrayList<String> = ArrayList()
        listFatura.addAll(Constant.BORCTIP)

        val spinnerAdapter = activity?.let {
            ArrayAdapter(it, R.layout.view_spinner_dropdown_item, listFatura)
        }!!

        binding.spinnerFatura.setAdapter(spinnerAdapter)
        binding.spinnerFatura.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                faturaTip = parent.getItemAtPosition(position) as String

            }

    }
    private fun allFriends(){
        listFriends = ArrayList()
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


    private fun graph(){
       val list :ArrayList<BarEntry> = ArrayList()

        list.add(BarEntry(100f,3f))
        list.add(BarEntry(102f,5f))
        list.add(BarEntry(104f,6f))
        list.add(BarEntry(108f,8f))
        list.add(BarEntry(110f,11f))

        val barDataSet = BarDataSet(list,"ahmet")
        barDataSet.setColors(Constant.LISTCOLOR,255)
        barDataSet.valueTextColor = Color.BLACK
        val barData = BarData(barDataSet)
        binding.chartView.setFitBars(true)
        binding.chartView.data = barData
        binding.chartView.description.text = "Aktekin"
        binding.chartView.animateY(20)
        binding.chartView.visible()


    }

}