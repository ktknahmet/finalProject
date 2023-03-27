package com.ktknahmet.final_project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ktknahmet.final_project.databinding.AllFriendsRowBinding
import com.ktknahmet.final_project.model.AllFriends


class AllFriendsAdapter (private val alldata:List<AllFriends>):
    RecyclerView.Adapter<AllFriendsAdapter.DetailsHolder>() {
    private var list:List<AllFriends> = ArrayList()
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
            val p: AllFriends = list[position]
            binding.tvItemGroupName.text = p.GRUPNAME
            binding.tvItemCount.text = p.COUNT.toString()
        }
    }

    override fun getItemCount(): Int {
        return alldata.size
    }
    fun getList(): List<AllFriends> = list

}