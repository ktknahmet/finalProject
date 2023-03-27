@file:Suppress("unused")

package com.ktknahmet.final_project.utils.resources

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build.VERSION.SDK_INT
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.ktknahmet.final_project.utils.appCtx

/**
 * @see [androidx.core.content.ContextCompat.getColor]
 */
@ColorInt
fun Context.color(@ColorRes colorRes: Int): Int = if (SDK_INT >= 23) getColor(colorRes) else {
    @Suppress("DEPRECATION")
    resources.getColor(colorRes)
}

fun Fragment.color(@ColorRes colorRes: Int) = context!!.color(colorRes)
fun View.color(@ColorRes colorRes: Int) = context.color(colorRes)
/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appColor(@ColorRes colorRes: Int) = appCtx.color(colorRes)

/**
 * @see [androidx.core.content.ContextCompat.getColorStateList]
 */
@SuppressLint("UseCompatLoadingForColorStateLists")
fun Context.colorSL(@ColorRes colorRes: Int): ColorStateList {
    return (
        if (SDK_INT >= 23) getColorStateList(colorRes) else {
            @Suppress("DEPRECATION")
            resources.getColorStateList(colorRes)
        }
        )
}

fun Fragment.colorSL(@ColorRes colorRes: Int) = context!!.colorSL(colorRes)
fun View.colorSL(@ColorRes colorRes: Int) = context.colorSL(colorRes)
/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appColorSL(@ColorRes colorRes: Int) = appCtx.colorSL(colorRes)

// Styled resources below

private inline val defaultColor get() = Color.RED

@ColorInt
fun Context.styledColor(@AttrRes attr: Int): Int = color(resolveThemeAttribute(attr))

fun Fragment.styledColor(@AttrRes attr: Int) = context!!.styledColor(attr)
fun View.styledColor(@AttrRes attr: Int) = context.styledColor(attr)
/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledColor(@AttrRes attr: Int) = appCtx.styledColor(attr)

fun Context.styledColorSL(@AttrRes attr: Int): ColorStateList = colorSL(resolveThemeAttribute(attr))

fun Fragment.styledColorSL(@AttrRes attr: Int) = context!!.styledColorSL(attr)
fun View.styledColorSL(@AttrRes attr: Int) = context.styledColorSL(attr)
/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledColorSL(@AttrRes attr: Int) = appCtx.styledColorSL(attr)
