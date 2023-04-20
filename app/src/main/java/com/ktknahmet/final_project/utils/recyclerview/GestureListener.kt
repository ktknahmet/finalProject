package com.ktknahmet.final_project.utils.recyclerview

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ktknahmet.final_project.utils.Constant.DOUBLE_TAP
import com.ktknahmet.final_project.utils.Constant.LONG_PRESS
import com.ktknahmet.final_project.utils.Constant.SINGLE_TAP_UP

class GestureListener(
    private val recyclerView: RecyclerView,
    private val listener: (View, Int) -> Unit,
    private val type: Int
) : GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapUp(e: MotionEvent): Boolean = e.getChild(SINGLE_TAP_UP)

    override fun onDoubleTap(e: MotionEvent): Boolean = e.getChild(DOUBLE_TAP)

    override fun onLongPress(e: MotionEvent) { e.getChild(LONG_PRESS) }

    private fun MotionEvent.getChild(t: Int): Boolean {
        recyclerView.findChildViewUnder(x, y)?.let {
            if (type == t)
                listener(it, recyclerView.getChildAdapterPosition(it))
        }
        return true
    }
}