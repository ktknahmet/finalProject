@file:Suppress("unused")

package com.ktknahmet.final_project.utils

import android.app.Activity
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import kotlin.math.min

fun tabletGridLayout(mActivity: Activity, spanCount: Int): GridLayoutManager {
    val screenWidth = screenSize(mActivity)
    return when {
        screenWidth >= 720 -> {
            // Device is a 10" tablet
            GridLayoutManager(mActivity.applicationContext, spanCount)
        }
        screenWidth >= 600 -> {
            // Device is a 7" tablet
            GridLayoutManager(mActivity.applicationContext, spanCount)
        }
        else -> {
            // Device is a phone
            GridLayoutManager(mActivity.applicationContext, spanCount - 1)
        }
    }
}

fun isTablet(activity: Activity): Boolean {
    val screenWidth = screenSize(activity)
    val orientation = activity.resources.configuration.orientation
    return when {
        screenWidth >= 600 && orientation == Configuration.ORIENTATION_PORTRAIT -> {
            // Device is a 7" tablet and landscape
            true
        }
        else -> {
            // Device is a phone
            false
        }
    }
}

fun isTabletLandscape(activity: Activity): Boolean {
    val screenWidth = screenSize(activity)
    val orientation = activity.resources.configuration.orientation
    return when {
        screenWidth >= 600 && orientation == Configuration.ORIENTATION_LANDSCAPE -> {
            // Device is a 7" tablet and landscape
            true
        }
        else -> {
            // Device is a phone
            false
        }
    }
}

fun screenSize(activity: Activity): Float {
    val metrics = activity.applicationContext.resources.displayMetrics
    // @Suppress("DEPRECATION")
    // activity.windowManager.defaultDisplay.getMetrics(metrics)
    val widthPixels: Int = metrics.widthPixels
    val heightPixels: Int = metrics.heightPixels
    val scaleFactor: Float = metrics.density
    val widthDp: Float = widthPixels / scaleFactor
    val heightDp: Float = heightPixels / scaleFactor
    return min(widthDp, heightDp)
}
