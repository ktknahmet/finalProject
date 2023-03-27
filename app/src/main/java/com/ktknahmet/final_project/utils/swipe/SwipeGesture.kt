package com.ktknahmet.final_project.utils.swipe


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView



abstract class SwipeGesture :ItemTouchHelper.Callback(){
    //recyclerview işlemlerinin swipe sayesinde silinmesini sağlayan yapıdır
    //itemtouchHelper.CallBack sınıfından extent alır ve bu bize 2 tane override methodu verir
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        //burada hangi tarafa kaydırarak işlem yapılması gerektiğini söyledik
        return makeMovementFlags(0,swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

}