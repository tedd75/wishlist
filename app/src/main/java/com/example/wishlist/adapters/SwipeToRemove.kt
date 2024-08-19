package com.example.wishlist.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToRemove(
    private val onSwipe: (id: Int) -> Unit
): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipe(viewHolder.layoutPosition)
    }
}