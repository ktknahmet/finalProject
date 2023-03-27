package com.ktknahmet.final_project.ui.addfriends

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.google.firebase.firestore.FirebaseFirestore

import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.FriendsAdapter
import com.ktknahmet.final_project.databinding.DialogAddGrupnameBinding
import com.ktknahmet.final_project.databinding.FragmentAddFriendsBinding
import com.ktknahmet.final_project.model.Contact
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.*
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.niceDialog.BaseMyDialog
import com.ktknahmet.final_project.utils.niceDialog.ViewConvertListener
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import com.ktknahmet.final_project.utils.swipe.SwipeGesture

import java.util.HashMap


class AddFriendsFragment : BaseFragment<FragmentAddFriendsBinding>(FragmentAddFriendsBinding::inflate),
    MultiplePermissionsListener {
    private lateinit var allList :ArrayList<Contact>
    private lateinit var mAdapter: FriendsAdapter
    private var grupName=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allList= ArrayList()
        checkPermission()
        init()
        binding.fabIcon.onClick {
            importContact()
        }
        binding.imgBtnSave.onClick {
            if(allList.isNotEmpty()){
                showNameDialog()
            }else{
                toastError(str(R.string.arkadas_bos))
            }

        }
    }

    private fun checkPermission() {
        Dexter.withContext(requireActivity())
            .withPermissions(
                android.Manifest.permission.READ_CONTACTS
            )
            .withListener(this)
            .check()
    }

    override fun onPermissionRationaleShouldBeShown(request: MutableList<PermissionRequest>?, token: PermissionToken?) {
        token?.continuePermissionRequest()
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
    @SuppressLint("IntentReset")
    fun importContact() {
        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContact.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(pickContact, 1)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("Range", "Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1 && resultCode == Activity.RESULT_OK){
            val contactData: Uri = data!!.data!!
            val c: Cursor =
                requireActivity().contentResolver.query(contactData, null, null, null, null)!!
            if (c.moveToFirst()) {
                val phone= c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                val x = Contact(name,phone)

                val check= allList.find {list->
                    list.phone==phone
                }
                if(check!=null){
                    toastError(str(R.string.arkadas_var))
                }else{
                    allList.add(x)
                }
                mAdapter = FriendsAdapter(allList)
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = mAdapter
                }
                binding.recyclerview.visible()
                binding.imageView.gone()
                mAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun init() {

        val swipeToDelete = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                allList.removeAt(position)
                mAdapter.notifyItemRemoved(position)

                if (allList.isEmpty()) {
                    binding.recyclerview.gone()
                    binding.imageView.visible()
                } else {
                    binding.recyclerview.visible()
                    binding.imageView.gone()
                }
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = mAdapter
                }
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)
    }
    private fun showNameDialog() {
        showDialog(object : ViewConvertListener<DialogAddGrupnameBinding> {
            override fun provideViewBinding(
                inflater: LayoutInflater,
                container: ViewGroup?
            ): DialogAddGrupnameBinding {
                return DialogAddGrupnameBinding.inflate(inflater, container, false)
            }

            override fun convertView(vb: DialogAddGrupnameBinding, dialog: BaseMyDialog<DialogAddGrupnameBinding>) {

                vb.btnOK.onClick {
                    if (vb.edtName.text.toString().isEmpty()) {
                        vb.tilName.error = Errors.BosGecilemez
                    } else {
                        grupName = vb.edtName.text.toString()
                        dialog.dismiss()
                        saveFriends()

                    }
                }
                vb.btnCancel.onClick {
                    dialog.dismiss()
                }
            }
        })
    }

private fun saveFriends(){
    binding.pgBar.visible()
    val pref = MainSharedPreferences(requireContext(), MyPref.bilgiler)
    val email = pref.getString(MyPref.email, "").toString()
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val add = HashMap<String,Any>()
        add["EMAIL"] = email
        add["GRUPINFO"] = allList
        add["GRUPNAME"] = grupName

        db.collection("arkadaslarim").add(add)
            .addOnSuccessListener {

               toastSuccess(str(R.string.arkdas_eklendi))
                allList.clear()
                mAdapter.notifyDataSetChanged()
                binding.pgBar.gone()
            }
            .addOnFailureListener {
               toastError(str(R.string.hata))
                binding.pgBar.gone()
            }

}
}