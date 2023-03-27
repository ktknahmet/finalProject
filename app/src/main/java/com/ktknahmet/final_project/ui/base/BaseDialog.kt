package com.ktknahmet.final_project.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.views.onClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.utils.*


private typealias FragmentDialogViewBindingInflater<VB> = (
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) -> VB

abstract class BaseDialog<VB : ViewBinding>(private val bindingInflater: FragmentDialogViewBindingInflater<VB>) :
    RoundedBottomSheetDialogFragment(), LifecycleOwner {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Tablet landscape modunda bottomsheet gözükmeme sorununu çözer
        if (isTabletLandscape(requireActivity())) {
            view.viewTreeObserver.addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val myDialog: BottomSheetDialog = dialog as BottomSheetDialog
                        val bottomSheet =
                            myDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                        val behavior = BottomSheetBehavior.from(bottomSheet)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        behavior.peekHeight = 0
                    }
                }
            )
        }
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
