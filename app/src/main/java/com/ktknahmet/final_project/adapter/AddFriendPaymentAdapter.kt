package com.ktknahmet.final_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setbackgroundColorResource
import com.crazylegend.kotlinextensions.views.visible
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.databinding.AddFriendPaymentRowBinding
import com.ktknahmet.final_project.databinding.AddPaymentRowBinding
import com.ktknahmet.final_project.model.AddFriendPayment
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.utils.Constant
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddFriendPaymentAdapter (private val alldata:List<AddFriendPayment>): RecyclerView.Adapter<AddFriendPaymentAdapter.DetailsHolder>() {
    //recyclerview oluştururuz ve bize 3 tane method override etmemizi sağlar
    private var list: List<AddFriendPayment> = ArrayList()

    init {
        list = alldata
    }


    class DetailsHolder(val binding: AddFriendPaymentRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHolder {
        val binding =
            AddFriendPaymentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //bağlama işlemi gösterilecek tasarımı recyclerview bağlama işlemi
        return DetailsHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DetailsHolder, position: Int) {
        val dateNow = Date()
        val sdf2 = SimpleDateFormat("dd/MM/yyyy")

        val selectDateTime = sdf2.format(dateNow)
        val date = sdf2.parse(selectDateTime)
        val selectDate = date!!.time


        with(holder) {
            val p: AddFriendPayment = list[position]
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateShow = Date(p.TARIH!!)


            var gun = p.ODEMETARIH!! -selectDate
            gun = (gun / (1000*60*60*24))

            binding.tvTarihGoster.text = dateFormat.format(dateShow)
            binding.tvArkadasGoster.text = p.ARKADAS

            binding.tvMiktarGoster.text = p.BUTCE.toString() +" " + Constant.TLICON


            if (p.ODEMETIP == Constant.ODENDI) {
                binding.tvOdemeGoster.text = Constant.ODENDI
                binding.tvDurum.setbackgroundColorResource(R.color.lightSuccess)
            }
            if (p.ODEMETIP == Constant.ODENECEK) {
                binding.tvSonOdeme.visible()
                if(gun <0){
                    val g =gun.toString().replace("-","")
                    binding.tvOdemeTarihGoster.text = ("$g Gün geçti")
                }else{
                    binding.tvOdemeTarihGoster.text = ("$gun Gün Kaldı")
                }

                binding.tvOdemeGoster.text = Constant.ODENECEK
                binding.tvDurum.setbackgroundColorResource(R.color.lightWarning)
            }
            if (p.ODEMETIP == Constant.TAKSITLIODEME) {
                binding.tvSonOdeme.visible()
                if(gun <0){
                    val g =gun.toString().replace("-","")
                    binding.tvOdemeTarihGoster.text = ("$g Gün geçti")
                }else{
                    binding.tvOdemeTarihGoster.text = ("$gun Gün Kaldı")
                }
                binding.tvOdemeGoster.text = Constant.TAKSITLIODEME
                binding.tvDurum.setbackgroundColorResource(R.color.lightShadow)
            }
            if (p.ODEMETIP == Constant.BORC) {
                binding.tvOdemeGoster.text = Constant.BORC
                binding.tvDurum.setbackgroundColorResource(R.color.lightError)
            }
            if (p.ODEMETIP == Constant.ALACAK) {
                binding.tvOdemeGoster.text = Constant.ALACAK
                binding.tvDurum.setbackgroundColorResource(R.color.lightPrimary)
            }

        }
    }


    override fun getItemCount(): Int {
        //item count ile listenin size kadar veriyi döndürme işlemi
        return alldata.size
    }
}

