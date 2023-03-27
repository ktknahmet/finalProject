package com.ktknahmet.final_project.ui.page

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.BorcAdapter
import com.ktknahmet.final_project.databinding.FragmentAddDataBinding
import com.ktknahmet.final_project.model.Borclar
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.Constant.BORCTIP
import com.ktknahmet.final_project.utils.Errors
import com.ktknahmet.final_project.utils.MainSharedPreferences
import com.ktknahmet.final_project.utils.appCtx
import com.ktknahmet.final_project.utils.resources.str
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import com.ktknahmet.final_project.utils.showConfirmDialog
import com.ktknahmet.final_project.utils.swipe.SwipeGesture
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddDataFragment : BaseFragment<FragmentAddDataBinding>(FragmentAddDataBinding::inflate),
    MultiplePermissionsListener {
    private lateinit var listFatura: ArrayList<String>
    private lateinit var allList :ArrayList<Borclar>
    private lateinit var pref: MainSharedPreferences
    private lateinit var mAdapter: BorcAdapter
    private var faturaTip=""
    @SuppressLint("SimpleDateFormat")
    private var selectDateTime: String = SimpleDateFormat("dd-MM-yyyy").format(Date())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()
        allList = ArrayList()
        binding.selectDate.setText(selectDateTime)
        init()
        faturalar()
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
                    selectDateTime = ("$d-$ay-$y")
                    binding.selectDate.setText(selectDateTime)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        binding.btnAdd.onClick {
            if(faturaTip.isEmpty()){
                binding.tilfaturaTip.error = Errors.BosGecilemez
            }else{
                details()
            }
        }
    }
    private fun faturalar(){
        listFatura= ArrayList()
        listFatura.addAll(BORCTIP)

       val  spinnerAdapter = activity?.let {
            ArrayAdapter(it,android.R.layout.simple_list_item_1, listFatura)
        }!!

        binding.spinnerFatura.setAdapter(spinnerAdapter)
        binding.spinnerFatura.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                faturaTip = parent.getItemAtPosition(position) as String

            }
    }
    private fun details(){
        pref = MainSharedPreferences(requireContext(), MyPref.bilgiler)
        val adSoyad = pref.getString("fullname", "").toString()

        val x = Borclar(faturaTip,selectDateTime,adSoyad)
        allList.add(x)

        mAdapter = BorcAdapter(allList)
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = BorcAdapter(allList)
        }

    }
    private fun init(){

        val swipeToDelete = object :SwipeGesture(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                allList.removeAt(position)
                mAdapter.notifyItemRemoved(position)

                if(allList.isEmpty()){
                    binding.recyclerview.gone()
                    binding.imageView.visible()
                    binding.titleTextView.visible()
                }else{
                    binding.recyclerview.visible()
                    binding.imageView.gone()
                    binding.titleTextView.gone()
                }
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = BorcAdapter(allList)
                }
            }

        }
        val itemTouchHelper =ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)
    }

    private fun checkPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_PHONE_STATE
            )
            .withListener(this)
            .check()
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        when {
            report!!.isAnyPermissionPermanentlyDenied -> {
                showConfirmDialog(
                    str(R.string.lutfen_izinleri_veriniz),
                    confirm = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", appCtx.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    },
                    buttonPositiveText = str(R.string.ayarlari_ac)
                )
            }
            report.areAllPermissionsGranted() -> {
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(request: MutableList<PermissionRequest>?, token: PermissionToken?) {
        token?.continuePermissionRequest()


    }
    }