package com.ktknahmet.final_project.ui.base

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.utils.te
import com.ktknahmet.final_project.utils.ti
import com.ktknahmet.final_project.utils.ts
import com.ktknahmet.final_project.utils.tw

abstract class BaseActivity: AppCompatActivity() {

    fun setUI(id: Int, myActivity: Activity) {
        if (id == 0) {
            myActivity.setTheme(R.style.MaterialTheme_NoActionBar_Light)
        } else if (id == 1) {
            myActivity.setTheme(R.style.MaterialTheme_NoActionBar_Dark)
        }
    }

    fun toastError(message: String) {
        te(message)
    }

    fun toastSuccess(message: String) {
        ts(message)
    }

    fun toastWarning(message: String) {
        tw(message)
    }

    fun toastInfo(message: String) {
        ti(message)
    }
}