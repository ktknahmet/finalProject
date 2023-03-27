package com.ktknahmet.final_project.utils.niceDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


class MyDialog<VB : ViewBinding> : BaseMyDialog<VB>() {
    private var convertListener: ViewConvertListener<VB>? = null

    fun setListener(listener: ViewConvertListener<VB>): MyDialog<*> {
        this.convertListener = listener
        return this
    }

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB? = convertListener?.provideViewBinding(inflater, container)

    override fun convertView(vb: VB, dialog: BaseMyDialog<VB>) {
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
