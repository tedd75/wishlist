package com.example.wishlist

import androidx.recyclerview.widget.DiffUtil
import com.example.wishlist.model.Products

class ProductCallBack(val notSorted: List<Products>, val sorted: List<Products>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = notSorted.size

    override fun getNewListSize(): Int = sorted.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] === sorted[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] == sorted[newItemPosition]

}