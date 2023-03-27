package com.ktknahmet.final_project.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.views.onClick
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.utils.te
import com.ktknahmet.final_project.utils.ti
import com.ktknahmet.final_project.utils.ts
import com.ktknahmet.final_project.utils.tw


private typealias FragmentFullDialogViewBindingInflater<VB> = (
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) -> VB

abstract class BaseFullScreenDialog<VB : ViewBinding>(private val bindingInflater: FragmentFullDialogViewBindingInflater<VB>) :
    FullScreenDialogFragment(), LifecycleOwner {
    val prefVM: SharedPrefViewModel by activityViewModels()
    protected val binding: VB
        get() = _binding!!
    private var _binding: VB? = null
    private var toolbarImage: ImageView? = null
    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = bindingInflater.invoke(inflater, container, false)
        val mView: View = binding.root
        toolbarImage = mView.findViewById(R.id.toolbarImage)
        toolbarImage?.onClick {
            dismiss()
        }
        return mView
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun toastError(message: String) {
        te(message)
    }

    fun toastInfo(message: String) {
        ti(message)
    }

    fun toastSuccess(message: String) {
        ts(message)
    }

    fun toastWarning(message: String) {
        tw(message)
    }
}
