package com.ktknahmet.final_project.ui.base

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ktknahmet.final_project.R

/**
 * BottomSheetDialog fragment that uses a custom
 * theme which sets a rounded background to the dialog
 * and doesn't dim the navigation bar
 */
open class RoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
}
