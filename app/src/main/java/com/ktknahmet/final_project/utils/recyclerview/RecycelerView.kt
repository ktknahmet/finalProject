package com.ktknahmet.final_project.utils

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktknahmet.final_project.utils.Constant.DOUBLE_TAP
import com.ktknahmet.final_project.utils.Constant.LONG_PRESS
import com.ktknahmet.final_project.utils.Constant.SINGLE_TAP_UP
import androidx.recyclerview.widget.RecyclerView
import com.ktknahmet.final_project.utils.recyclerview.SimpleOnItemTouchListener


/**
 * `false` by default.
 * If you can guarantee that all the items that this [RecyclerView] will display
 * (as defined from its adapter), have all the same size (e.g. homogeneous height for a list
 * displayed with a vertical [LinearLayoutManager]), set this property to `true` so the
 * [RecyclerView] can enable some optimizations that will improve efficiency.
 *
 * @see RecyclerView.setHasFixedSize
 */
inline var RecyclerView.fixedSize: Boolean
    get() = hasFixedSize()
    set(value) = setHasFixedSize(value)

private var onClickListener: SimpleOnItemTouchListener? = null
private var onDoubleClickListener: SimpleOnItemTouchListener? = null
private var onnLongPressListener: SimpleOnItemTouchListener? = null

fun RecyclerView.itemClick(listener: (View, position: Int) -> Unit) {
    onClickListener?.let {
        removeOnItemTouchListener(it)
    }
    onClickListener = SimpleOnItemTouchListener(this, listener, SINGLE_TAP_UP)
    addOnItemTouchListener(onClickListener!!)
}

fun RecyclerView.itemDoubleClick(listener: (View, position: Int) -> Unit) {
    onDoubleClickListener?.let {
        removeOnItemTouchListener(it)
    }
    onDoubleClickListener = SimpleOnItemTouchListener(this, listener, DOUBLE_TAP)
    addOnItemTouchListener(onDoubleClickListener!!)
}

fun RecyclerView.itemLongClick(listener: (View, position: Int) -> Unit) {
    onnLongPressListener?.let {
        removeOnItemTouchListener(it)
    }
    onnLongPressListener =SimpleOnItemTouchListener(this, listener, LONG_PRESS)
    addOnItemTouchListener(onnLongPressListener!!)
}
