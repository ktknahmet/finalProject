package com.ktknahmet.final_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ktknahmet.final_project.databinding.ProfileDetailsRowBinding
import com.ktknahmet.final_project.model.ProfileDetails

class ProfileAdapter(private val alldata:List<ProfileDetails>):
    RecyclerView.Adapter<ProfileAdapter.DetailsHolder>() {
    private var list:List<ProfileDetails> = ArrayList()
    init {
        list = alldata
    }


    class DetailsHolder(val binding: ProfileDetailsRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHolder{
        val binding = ProfileDetailsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //bağlama işlemi
        return DetailsHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DetailsHolder, position: Int) {

        with(holder){
            val p: ProfileDetails = list[position]
            binding.ivAlbum.setImageResource(p.IMAGE)
            binding.tvItemDetay.text=p.NAME

        }
    }

    override fun getItemCount(): Int {
        return alldata.size
    }
    fun getList(): List<ProfileDetails> = list
}