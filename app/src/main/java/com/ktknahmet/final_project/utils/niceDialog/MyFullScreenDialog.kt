package com.ktknahmet.final_project.utils.niceDialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


class MyFullScreenDialog<VB : ViewBinding> : BaseMyFullScreenDialog<VB>() {
    private var convertListener: ViewConvertListener2<VB>? = null

    fun setListener(listener: ViewConvertListener2<VB>): MyFullScreenDialog<*> {
        this.convertListener = listener
        return this
    }

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB? = convertListener?.provideViewBinding(inflater, container)

    override fun convertView(vb: VB, dialog: BaseMyFullScreenDialog<VB>) {
        convertListener?.convertView(vb, dialog)
    }

    override fun onDialogVisibleChange(isVisible: Boolean) {
        super.onDialogVisibleChange(isVisible)
        if (isVisible) {
            if (convertListener == null) {
                // dismiss
                dismissManually(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (dismissManually) {
            convertListener = null
        }
    }
}
