package com.ktknahmet.final_project.ui.payment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.views.gone
import com.ktknahmet.final_project.utils.niceDialog.ViewConvertListener
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.google.firebase.firestore.FirebaseFirestore
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.AddPaymentAdapter
import com.ktknahmet.final_project.databinding.DialogAmountBinding
import com.ktknahmet.final_project.databinding.FragmentPaymentBinding
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.*
import com.ktknahmet.final_project.utils.Constant.ODENECEK
import com.ktknahmet.final_project.utils.Constant.TAKSITLIODEME
import com.ktknahmet.final_project.utils.Constant.sdf2
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.niceDialog.BaseMyDialog
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PaymentFragment : BaseFragment<FragmentPaymentBinding>(FragmentPaymentBinding::inflate) {
    private lateinit var listFatura: ArrayList<String>
    private lateinit var listOdemeTip: ArrayList<String>
    private var faturaTip = ""
    private var odemeTip = ""
    private var miktar = 0.0
    private lateinit var allList: ArrayList<AddPayment>
    private var dateNow = Date()
    var selectDateTime = sdf2.format(dateNow)
    var odemeDate = sdf2.format(dateNow)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allList = ArrayList()
        faturalar()
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
                    selectDateTime = ("$d.$ay.$y")
                    binding.selectDate.setText(selectDateTime)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        binding.imgBtnSave.onClick {
            if (faturaTip.isEmpty() || odemeTip.isEmpty()) {
                binding.tilfaturaTip.error = Errors.BosGecilemez
                binding.tilodemeTip.error = Errors.BosGecilemez
            } else {
                showAskAmountDialog()

            }
        }
    }


    private fun faturalar() {
        listFatura = ArrayList()
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

                if (odemeTip == ODENECEK || odemeTip == TAKSITLIODEME) {
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
                            odemeDate = ("$d.$ay.$y")
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
                    } else if (selectDate < odemeDatee) {
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
        val pref = MainSharedPreferences(requireActivity(), MyPref.bilgiler)
        val adSoyad = pref.getString(MyPref.fullName, "").toString()
        val email = pref.getString(MyPref.email, "").toString()
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
        add["FATURATIP"] = faturaTip
        add["TARIH"] = selectDate
        add["ODEMETARIH"] = odemeDatee


        val a = AddPayment(miktar, email, faturaTip, adSoyad, odemeTip, phone, selectDate, odemeDatee)

        db.collection("odemeler").add(add)
            .addOnSuccessListener {
                toastSuccess(str(R.string.ödeme_kaydedildi))
                allList.add(a)
                val mAdapter = AddPaymentAdapter(allList)
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