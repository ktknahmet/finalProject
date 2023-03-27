package com.ktknahmet.final_project.utils.niceDialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface ViewConvertListener2<VB : ViewBinding> {
    fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    fun convertView(vb: VB, dialog: BaseMyFullScreenDialog<VB>)
}
