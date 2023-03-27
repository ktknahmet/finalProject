package com.ktknahmet.final_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ktknahmet.final_project.databinding.OdeneceklRowBinding
import com.ktknahmet.final_project.model.Borclar

class BorcAdapter (private val alldata:List<Borclar>):
    RecyclerView.Adapter<BorcAdapter.DetailsHolder>() {
    private var list:List<Borclar> = ArrayList()
    init {
        list = alldata
    }


    class DetailsHolder(val binding: OdeneceklRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHolder{
        val binding = OdeneceklRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //bağlama işlemi
        return DetailsHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DetailsHolder, position: Int) {


        with(holder){
            val p: Borclar = list[position]
            binding.tvTarihGoster.text = p.TARIH
            binding.tvFaturaGoster.text = p.FATURATIP

        }
    }

    override fun getItemCount(): Int {
        return alldata.size
    }

}