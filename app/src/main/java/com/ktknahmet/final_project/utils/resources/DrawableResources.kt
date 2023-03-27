@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.ktknahmet.final_project.utils.resources

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.ktknahmet.final_project.utils.appCtx

private val tmpValue by lazy { TypedValue() }

/**
 * @see [androidx.core.content.ContextCompat.getDrawable]
 */
/*@SuppressLint("UseCompatLoadingForDrawables")
fun Context.drawable(@DrawableRes drawableResId: Int): Drawable? {
    return getDrawable(drawableResId)
}*/

fun Context.drawable(@DrawableRes drawableResId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableResId)
}

inline fun Fragment.drawable(@DrawableRes drawableResId: Int) = context!!.drawable(drawableResId)
inline fun View.drawable(@DrawableRes drawableResId: Int) = context.drawable(drawableResId)
/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
inline fun appDrawable(@DrawableRes drawableResId: Int) = appCtx.drawable(drawableResId)

// Styled resources below

fun Context.styledDrawable(@AttrRes attr: Int): Drawable? = drawable(resolveThemeAttribute(attr))

inline fun Fragment.styledDrawable(@AttrRes attr: Int) = context!!.styledDrawable(attr)
inline fun View.styledDrawable(@AttrRes attr: Int) = context.styledDrawable(attr)
/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
inline fun appStyledDrawable(@AttrRes attr: Int) = appCtx.styledDrawable(attr)
