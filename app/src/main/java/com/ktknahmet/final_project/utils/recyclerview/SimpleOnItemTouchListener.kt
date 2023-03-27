package com.ktknahmet.final_project.utils.recyclerview

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SimpleOnItemTouchListener(
    recyclerView: RecyclerView,
    listener: (View, Int) -> Unit,
    type: Int
) : RecyclerView.OnItemTouchListener {

    private val gestureListener = GestureListener(recyclerView, listener, type)
    private var gestureDetector: GestureDetector = GestureDetector(recyclerView.context, gestureListener)

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) { }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(e)
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }
}