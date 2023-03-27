package com.ktknahmet.final_project.utils


import android.app.Activity
import androidx.fragment.app.Fragment

fun Activity.te(message: String) {
    ToastMessage.createColorToast(
        this,
        message,
        ToastMessage.TOAST_ERROR,
        ToastMessage.GRAVITY_TOP,
        ToastMessage.LONG_DURATION
    )
}
fun Fragment.te(message: String) {
    ToastMessage.createColorToast(
        this.requireActivity(),
        message,
        ToastMessage.TOAST_ERROR,
        ToastMessage.GRAVITY_TOP,
        ToastMessage.LONG_DURATION
    )
}
fun Fragment.te(message: String, duration: Long) {
    ToastMessage.createColorToast(
        this.requireActivity(),
        message,
        ToastMessage.TOAST_ERROR,
        ToastMessage.GRAVITY_TOP,
        duration
    )
}
fun Activity.ts(message: String) {
    ToastMessage.createColorToast(
        this,
        message,
        ToastMessage.TOAST_SUCCESS,
        ToastMessage.GRAVITY_TOP,
        ToastMessage.LONG_DURATION
    )
}
fun Fragment.ts(message: String) {
    ToastMessage.createColorToast(
        this.requireActivity(),
        message,
        ToastMessage.TOAST_SUCCESS,
        ToastMessage.GRAVITY_TOP,
        ToastMessage.LONG_DURATION
    )
}
fun Activity.tw(message: String) {
    ToastMessage.createColorToast(
        this,
        message,
        ToastMessage.TOAST_WARNING,
        ToastMessage.GRAVITY_TOP,
        ToastMessage.LONG_DURATION
    )
}
fun Fragment.tw(message: String) {
    ToastMessage.createColorToast(
        this.requireActivity(),
        message,
        ToastMessage.TOAST_WARNING,
        ToastMessage.GRAVITY_TOP,
        ToastMessage.LONG_DURATION
    )
}
@Suppress("unused")
fun Activity.ti(message: String) {
    ToastMessage.createColorToast(
        this,
        message,
        ToastMessage.TOAST_INFO,
        ToastMessage.GRAVITY_TOP,
        ToastMessage.LONG_DURATION
    )
}
fun Fragment.ti(message: String) {
    ToastMessage.createColorToast(
        this.requireActivity(),
        message,
        ToastMessage.TOAST_INFO,
        ToastMessage.GRAVITY_TOP,
        ToastMessage.LONG_DURATION
    )
}
