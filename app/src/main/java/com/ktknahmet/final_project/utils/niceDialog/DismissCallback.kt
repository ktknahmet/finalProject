package com.ktknahmet.final_project.utils.niceDialog

import androidx.fragment.app.DialogFragment

interface DismissCallback {
    fun onDismiss(dialog: DialogFragment, dismissManually: Boolean)
}
