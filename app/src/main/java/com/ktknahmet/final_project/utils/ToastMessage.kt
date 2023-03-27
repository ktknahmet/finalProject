package com.ktknahmet.final_project.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.utils.generalUtils.drawable

class ToastMessage {
    companion object {

        const val LONG_DURATION = 5000L // 5 seconds
        const val TOAST_SUCCESS = "SUCCESS"
        const val TOAST_ERROR = "FAILED"
        const val TOAST_WARNING = "WARNING"
        const val TOAST_INFO = "INFO"
        const val GRAVITY_TOP = 50

        private lateinit var layoutInflater: LayoutInflater

        private var successToastColor: Int = R.color.lightSuccess
        private var errorToastColor: Int = R.color.lightError
        private var warningToastColor: Int = R.color.lightWarning
        private var infoToastColor: Int = R.color.lightPrimary
        private var deleteToastColor: Int = R.color.lightPrimary


        @Suppress("unused")
        fun resetToastColors() {
            successToastColor = R.color.lightSuccess
            errorToastColor = R.color.lightError
            warningToastColor = R.color.lightWarning
            infoToastColor = R.color.lightPrimary
            deleteToastColor = R.color.lightPrimary

        }

        fun setSuccessColor(color: Int) {
            successToastColor = color
        }



        fun setErrorColor(color: Int) {
            errorToastColor = color
        }


        fun setWarningColor(color: Int) {
            warningToastColor = color
        }

        fun setInfoColor(color: Int) {
            infoToastColor = color
        }

        fun setDeleteColor(color: Int) {
            deleteToastColor = color
        }
        // all color toast CTA
        fun createColorToast(
            context: Activity,
            message: String,
            style: String,
            position: Int,
            duration: Long
        ) {
            layoutInflater = LayoutInflater.from(context)
            val layout = layoutInflater.inflate(
                R.layout.toast_layout,
                (context).findViewById(R.id.color_toast_view)
            )
            val ivToast: ImageView = layout.findViewById(R.id.color_toast_image)
            val tvToastMsg: TextView = layout.findViewById(R.id.color_toast_description)
            when (style) {
                // Function for Toast Success
                TOAST_SUCCESS -> {
                    ivToast.setImageDrawable(
                        context.drawable(R.drawable.ic_check_bg_filled)
                    )
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(ivToast.drawable),
                        ContextCompat.getColor(context, successToastColor)
                    )

                    // round background color
                    setBackgroundAndFilter(
                        successToastColor,
                        layout,
                        context
                    )

                    // Setting up the color for title & Message text

                    setDescriptionDetails(message, tvToastMsg)

                    // init toast
                    val toast = Toast(context.applicationContext)
                    startTimer(duration, toast)

                    // Setting Toast Gravity
                    setGravity(position, toast)

                    // Setting layout to toast
                    @Suppress("DEPRECATION")
                    toast.view = layout
                    toast.show()
                }
                // CTA for Toast Error
                TOAST_ERROR -> {
                    ivToast.setImageDrawable(
                        context.drawable(R.drawable.ic_close_bg_filled)
                    )
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(ivToast.drawable),
                        ContextCompat.getColor(context, errorToastColor)
                    )

                    // round background color
                    setBackgroundAndFilter(
                        errorToastColor,
                        layout,
                        context
                    )

                    // Setting up the color for title & Message text
                    setDescriptionDetails(message, tvToastMsg)

                    // init toast
                    val toast = Toast(context.applicationContext)
                    startTimer(duration, toast)

                    setGravity(position, toast)
                    toast.view = layout
                    toast.show()
                }
                TOAST_WARNING -> {
                    ivToast.setImageDrawable(
                        context.drawable(R.drawable.ic_error_sign_filled)
                    )
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(ivToast.drawable),
                        ContextCompat.getColor(context, warningToastColor)
                    )


                    // round background color
                    setBackgroundAndFilter(
                        warningToastColor,
                        layout,
                        context
                    )

                    // Setting up the color for title & Message text

                    setDescriptionDetails(message, tvToastMsg)

                    // init toast
                    val toast = Toast(context.applicationContext)
                    startTimer(duration, toast)

                    // Setting Toast Gravity
                    setGravity(position, toast)

                    // Setting layout to toast
                    toast.view = layout
                    toast.show()
                }
                // CTA for Toast Info
                TOAST_INFO -> {
                    ivToast.setImageDrawable(
                        context.drawable(R.drawable.ic_info_sign_filled)
                    )
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(ivToast.drawable),
                        ContextCompat.getColor(context, infoToastColor)
                    )


                    // round background color
                    setBackgroundAndFilter(
                        infoToastColor,
                        layout,
                        context
                    )

                    // Setting up the color for title & Message text

                    setDescriptionDetails(message, tvToastMsg)

                    // init toast
                    val toast = Toast(context.applicationContext)
                    startTimer(duration, toast)

                    // Setting Toast Gravity
                    setGravity(position, toast)

                    // Setting layout to toast
                    toast.view = layout
                    toast.show()
                }
            }
        }

        private fun startTimer(duration: Long, toast: Toast) {
            val timer = object : CountDownTimer(duration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // do nothing
                }

                override fun onFinish() {
                    toast.cancel()
                }
            }
            timer.start()
        }

        private fun setDescriptionDetails(
            message: String,
            layout: TextView
        ) {
            layout.setTextColor(Color.WHITE)
            layout.text = message
        }

        private fun setGravity(position: Int, toast: Toast) {
            toast.setGravity(position, 100, 100)
        }

        private fun setBackgroundAndFilter(
            @ColorRes colorFilter: Int,
            layout: View,
            context: Context
        ) {
            val drawable = context.drawable(R.drawable.toast_round_background)
            drawable?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(context, colorFilter),
                PorterDuff.Mode.MULTIPLY
            )
            layout.background = drawable
        }
    }
}