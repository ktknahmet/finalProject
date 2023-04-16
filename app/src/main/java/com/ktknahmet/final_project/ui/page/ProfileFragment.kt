package com.ktknahmet.final_project.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.adapter.ProfileAdapter
import com.ktknahmet.final_project.databinding.FragmentProfileBinding
import com.ktknahmet.final_project.model.ProfileDetails
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.*
import com.ktknahmet.final_project.utils.Constant.PROFILE_TYPE
import com.ktknahmet.final_project.utils.resources.str
import com.ktknahmet.final_project.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate),
    MultiplePermissionsListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        showData()

       binding.recyclerview.itemClick { _, position ->
           when (position) {

               0 -> navigate(R.id.action_profileFragment_to_spendingFragment)
               1 -> navigate(R.id.action_profileFragment_to_bildirimFragment)
               2 -> navigate(R.id.action_profileFragment_to_paymentFragment)
               3 -> navigate(R.id.action_profileFragment_to_addfriendsFragment)
               4 -> navigate(R.id.action_profileFragment_to_friendsFragment)
               5 -> navigate(R.id.action_profileFragment_to_graphFragment)
               6 -> {
                   openPdfFile()
               }
               7 -> {
                   navigate(R.id.action_profileFragment_to_borcDurum)
               }
           }
       }

    }
    private fun openPdfFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 200)
    }
    private fun showData(){
        val mAdapter = ProfileAdapter(PROFILE_TYPE.toList())
        binding.recyclerview.apply {
            binding.recyclerview.layoutManager = tabletGridLayout(requireActivity(), 3)
            binding.recyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            binding.recyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            this.adapter = mAdapter
        }

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