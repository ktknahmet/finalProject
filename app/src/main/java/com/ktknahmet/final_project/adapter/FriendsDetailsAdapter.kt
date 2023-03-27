package com.ktknahmet.final_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ktknahmet.final_project.databinding.AllFriendsRowBinding
import com.ktknahmet.final_project.model.Contact

class FriendsDetailsAdapter (private val alldata:List<Contact>):
    RecyclerView.Adapter<FriendsDetailsAdapter.DetailsHolder>() {
    private var list:List<Contact> = ArrayList()
    init {
        list = alldata
    }


    class DetailsHolder(val binding: AllFriendsRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHolder{
        val binding = AllFriendsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //bağlama işlemi
        return DetailsHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DetailsHolder, position: Int) {


        with(holder){
            val p: Contact = list[position]
            binding.tvItemGroupName.text = p.name
            binding.tvItemCount.text = p.phone
        }
    }

    override fun getItemCount(): Int {
        return alldata.size
    }
    fun getList(): List<Contact> = list

}