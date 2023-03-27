package com.ktknahmet.final_project.ui.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle

import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.AddPaymentAdapter
import com.ktknahmet.final_project.databinding.FragmentBildirimlerBinding
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.utils.Constant.ODENECEK
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref


class BildirimlerFragment : BaseFragment<FragmentBildirimlerBinding>(FragmentBildirimlerBinding::inflate)  {
    private lateinit var allList: ArrayList<AddPayment>
    private lateinit var pref: MainSharedPreferences
    private lateinit var mAdapter: AddPaymentAdapter
    private lateinit var notificationManager :NotificationManager
    private lateinit var notificationChannel :NotificationChannel
    private lateinit var builder :Notification.Builder
    private val channelId ="com.ktknahmet.final_project"
    private val description = "Deneme"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allData()

        notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager()
    }



    @SuppressLint("NewApi", "UnspecifiedImmutableFlag")
    private fun nManager(){

        val intent = Intent(requireActivity(),LauncherActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(requireActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(requireActivity(),channelId)
                .setContentTitle("Denemedir")
                .setContentTitle("Test Bildirimi")
                .setSmallIcon(R.drawable.iv_notification)
                .setContentIntent(pendingIntent)
        }else{
            builder = Notification.Builder(requireActivity())
                .setContentTitle("Denemedir")
                .setContentTitle("Test Bildirimi")
                .setSmallIcon(R.drawable.iv_notification)
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234,builder.build())

    }
    private fun allData() {
        pref = MainSharedPreferences(requireContext(), MyPref.bilgiler)
        val email = pref.getString(MyPref.email, "").toString()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val query =db.collection("odemeler").whereEqualTo("ODEMETIP",ODENECEK)
            .whereEqualTo("EMAIL",email)
            .orderBy("ODEMETARIH", Query.Direction.ASCENDING).get()
        query.addOnSuccessListener {
            if(!it.isEmpty){
                for(data in it.documents){
                    val user = data.toObject(AddPayment::class.java)
                    allList.add(user!!)
                }
                binding.recyclerview.visible()
                binding.imageView.gone()
                mAdapter = AddPaymentAdapter(allList)
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = mAdapter
                }
            }else{
                binding.recyclerview.gone()
                binding.imageView.visible()
                toastError(str(R.string.veri_yok))
            }

        }.addOnFailureListener {
            toastError(it.message.toString())
        }
        allList = ArrayList()
    }
}