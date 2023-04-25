package com.ktknahmet.final_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setbackgroundColorResource
import com.crazylegend.kotlinextensions.views.visible
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.databinding.AddPaymentRowBinding
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.utils.Constant.ALACAK
import com.ktknahmet.final_project.utils.Constant.BORC
import com.ktknahmet.final_project.utils.Constant.ODENDI
import com.ktknahmet.final_project.utils.Constant.ODENECEK
import com.ktknahmet.final_project.utils.Constant.TAKSITLIODEME
import com.ktknahmet.final_project.utils.Constant.TLICON
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong

class AddPaymentAdapter (private val alldata:List<AddPayment>): RecyclerView.Adapter<AddPaymentAdapter.DetailsHolder>() {

    private var list: List<AddPayment> = ArrayList()

    init {
        list = alldata
    }


    class DetailsHolder(val binding: AddPaymentRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHolder {
        val binding = AddPaymentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DetailsHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DetailsHolder, position: Int) {
        val dateNow = Date()
        val sdf2 = SimpleDateFormat("dd.MM.yyyy")

        val selectDateTime = sdf2.format(dateNow)
        val date = sdf2.parse(selectDateTime)
        val selectDate = date!!.time


        with(holder) {
            val p: AddPayment = list[position]
            val dateFormat = SimpleDateFormat("dd.MM.yyyy")
            val dateShow = Date(p.TARIH!!)


            var gun = p.ODEMETARIH!! -selectDate
            gun = (gun / (1000*60*60*24))

            binding.tvTarihGoster.text = dateFormat.format(dateShow)
            binding.tvFaturaGoster.text = p.FATURATIP

            binding.tvMiktarGoster.text = p.BUTCE.toString() +" " + TLICON


            if (p.ODEMETIP == ODENDI) {
                binding.tvOdemeGoster.text = ODENDI
                binding.tvDurum.setbackgroundColorResource(R.color.lightSuccess)
            }
            if (p.ODEMETIP == ODENECEK) {
                binding.tvSonOdeme.visible()
                if(gun <0){
                    val g =gun.toString().replace("-","")
                    binding.tvOdemeTarihGoster.text = ("$g Gün geçti")
                }else{
                    binding.tvOdemeTarihGoster.text = ("$gun Gün Kaldı")
                }

                binding.tvOdemeGoster.text = ODENECEK
                binding.tvDurum.setbackgroundColorResource(R.color.lightWarning)
            }
            if (p.ODEMETIP == TAKSITLIODEME) {
                binding.tvSonOdeme.visible()
                if(gun <0){
                    val g =gun.toString().replace("-","")
                    binding.tvOdemeTarihGoster.text = ("$g Gün geçti")
                }else{
                    binding.tvOdemeTarihGoster.text = ("$gun Gün Kaldı")
                }
                binding.tvOdemeGoster.text = TAKSITLIODEME
                binding.tvDurum.setbackgroundColorResource(R.color.lightShadow)
            }
            if (p.ODEMETIP == BORC) {
                binding.tvOdemeGoster.text = BORC
                binding.tvDurum.setbackgroundColorResource(R.color.lightError)
            }
            if (p.ODEMETIP == ALACAK) {
                binding.tvOdemeGoster.text = ALACAK
                binding.tvDurum.setbackgroundColorResource(R.color.lightPrimary)
            }

        }
    }


    override fun getItemCount(): Int {
        //item count ile listenin size kadar veriyi döndürme işlemi
        return alldata.size
    }
}

