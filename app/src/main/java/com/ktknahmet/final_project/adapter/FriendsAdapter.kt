package com.ktknahmet.final_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ktknahmet.final_project.databinding.FriendsRowBinding
import com.ktknahmet.final_project.model.Contact

class FriendsAdapter (private val alldata:List<Contact>):
    RecyclerView.Adapter<FriendsAdapter.DetailsHolder>() {
    private var list:List<Contact> = ArrayList()
    init {
        list = alldata
    }


    class DetailsHolder(val binding: FriendsRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHolder{
        val binding = FriendsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //bağlama işlemi
        return DetailsHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DetailsHolder, position: Int) {


        with(holder){
            val p: Contact = list[position]
             binding.itemName.text = p.name
             binding.itemPhone.text = p.phone
        }
    }

    override fun getItemCount(): Int {
        return alldata.size
    }

}