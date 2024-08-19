package com.example.wishlist.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.DiffUtil


import androidx.recyclerview.widget.RecyclerView
import com.example.wishlist.ProductCallBack
import com.example.wishlist.model.Products
import com.example.wishlist.databinding.ListItemBinding


class ProductViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Products) {

        binding.name.text = product.name
        binding.address.text = product.address.joinToString(",\n")
        binding.image.setImageResource(product.resId)
    }
}

class ProductsAdapter : RecyclerView.Adapter<ProductViewHolder>() {
    private val data = mutableListOf<Products>()

    //    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var onItemClick: (Long) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                onItemClick(data[vh.layoutPosition].id)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun replace(newData: List<Products>) {
        val callback = ProductCallBack(data.toList(), newData)
        data.clear()
        data.addAll(newData)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)


    }

    fun sort() {
        val notsorted = data.toList()
        data.sortBy { it.name }
        val callback = ProductCallBack(notsorted, data)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)

    }

//    fun launchCamera() {
//
//    }
//    private fun <I> ActivityResultLauncher<I>.launch() {
//        TODO("Not yet implemented")
//    }


    fun removeItem(layoutPosition: Int): Products {
    val product = data.removeAt(layoutPosition)
    notifyItemRemoved(layoutPosition)
        return product
    }
}