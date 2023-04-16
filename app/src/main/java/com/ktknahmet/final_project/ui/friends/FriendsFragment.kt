package com.ktknahmet.final_project.ui.friends

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.gson.gson
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.AllFriendsAdapter
import com.ktknahmet.final_project.adapter.FriendsDetailsAdapter
import com.ktknahmet.final_project.databinding.FragmentFriendsBinding
import com.ktknahmet.final_project.databinding.FriendsDetailsBinding
import com.ktknahmet.final_project.model.AllFriends
import com.ktknahmet.final_project.model.Contact
import com.ktknahmet.final_project.model.Friends
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.*
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.niceDialog.BaseMyDialog
import com.ktknahmet.final_project.utils.niceDialog.ViewConvertListener
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import com.ktknahmet.final_project.utils.swipe.SwipeGesture


class FriendsFragment :  BaseFragment<FragmentFriendsBinding>(FragmentFriendsBinding::inflate){
    private lateinit var list :ArrayList<AllFriends>
    private lateinit var allList:ArrayList<Friends>
    private lateinit var mAdapter: AllFriendsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnAdd.onClick {
            navigate(R.id.action_friendsFragment_to_addfriendsFragment)
        }
        init()
        allFriends()

        binding.recyclerview.itemClick { _, position ->
            if(mAdapter.getList().isNotEmpty()){
                showGroupDetails(mAdapter.getList()[position].GRUPNAME)
            }
        }
    }

    private fun init() {

        val swipeToDelete = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                showDialog(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showDialog(position: Int) {
        showDeleteDialog(
            confirm = {
                deleteFriends(position)
                list.removeAt(position)
                mAdapter.notifyItemRemoved(position)
                binding.recyclerview.adapter=mAdapter
                if (list.isEmpty()) {
                    binding.recyclerview.gone()
                } else {
                    binding.recyclerview.visible()
                }
            },
            cancel = {
                mAdapter.notifyDataSetChanged()
                binding.recyclerview.adapter=mAdapter

            }
        )
    }
    private fun deleteFriends(position: Int){
        val pref = MainSharedPreferences(requireContext(), MyPref.bilgiler)
        val email = pref.getString(MyPref.email, "").toString()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val query =db.collection("arkadaslarim").
        whereEqualTo("EMAIL", email).
        whereEqualTo("GRUPNAME",list[position].GRUPNAME)

        query.get().addOnSuccessListener { snapshot ->
            for (document in snapshot) {
                document.reference.delete()
            }
        }
    }

    private fun showGroupDetails(grupName:String) {
        showDialog(object : ViewConvertListener<FriendsDetailsBinding> {
            override fun provideViewBinding(
                inflater: LayoutInflater,
                container: ViewGroup?
            ): FriendsDetailsBinding {
                return FriendsDetailsBinding.inflate(inflater, container, false)
            }
            var listContact : ArrayList<Contact> = arrayListOf()
            var listGrup=""
            override fun convertView(vb: FriendsDetailsBinding, dialog: BaseMyDialog<FriendsDetailsBinding>) {
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                val query =db.collection("arkadaslarim").whereEqualTo("GRUPNAME", grupName).get()

                query.addOnSuccessListener {
                    if(!it.isEmpty){
                        for(i in it.documents.indices){
                            val grupinfo = it.documents[i].data!!["GRUPINFO"]

                            println("grup :$grupinfo")

                            val gson = Gson()
                            listGrup = gson.toJson(grupinfo)

                            println("list grup :$listGrup")
                        }
                        val contactList: ArrayList<Contact> = gson.fromJson(listGrup, object: TypeToken<ArrayList<Contact>>() {}.type)

                        println("contact :$contactList")
                        for(i in contactList){
                            val x = Contact(i.name,i.phone)
                            listContact.add(x)
                        }
                        val detailsAdapter = FriendsDetailsAdapter(listContact)
                        vb.recyclerview.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            this.adapter = detailsAdapter
                        }
                    }else{
                        binding.recyclerview.gone()
                        toastError(str(R.string.arkadas_bos))
                    }

                }.addOnFailureListener {
                    toastError(it.message.toString())
                }
            }

        })
    }

    private fun allFriends(){
        allList= ArrayList()
        list = ArrayList()
        val pref = MainSharedPreferences(requireContext(), MyPref.bilgiler)
        val email = pref.getString(MyPref.email, "").toString()

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val query =db.collection("arkadaslarim").whereEqualTo("EMAIL", email).get()

        query.addOnSuccessListener {
            if(!it.isEmpty){
                for(data in it.documents){
                    val user = data.toObject(Friends::class.java)

                    allList.add(user!!)
                    val x = AllFriends(user.GRUPNAME.toString(), user.GRUPINFO!!.size)
                    list.add(x)
                }
                binding.recyclerview.visible()
                mAdapter = AllFriendsAdapter(list)
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = mAdapter
                }
            }else{
                binding.recyclerview.gone()
                toastError(str(R.string.arkadas_bos))
            }

        }.addOnFailureListener {
            toastError(it.message.toString())
        }
    }
}