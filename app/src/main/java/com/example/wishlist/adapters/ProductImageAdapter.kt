package com.example.wishlist.adapters


import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wishlist.R
import com.example.wishlist.databinding.ProductImageBinding


class ProductImageViewHolder(val binding: ProductImageBinding)
    : RecyclerView.ViewHolder(binding.root){

    fun bind(resId: Int, isSelected: Boolean) {
        binding.image.setImageResource(resId)
        binding.selectedFrame.visibility = if(isSelected) View.VISIBLE else View.INVISIBLE
    }
}
class ProductImagesAdapter() : RecyclerView.Adapter<ProductImageViewHolder>() {
    //miejsce na zdjecia
  //  private val images = listOf(R.drawable.aparat, R.drawable.okulary)
    private val images: List<Int> by lazy {
        // Retrieve the list of photo IDs from the Photos folder using the MediaStore API
        val imageList = mutableListOf<Int>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val selection = "${MediaStore.Images.Media.DATA} like ?"
        val selectionArgs = arrayOf("%/Photos/%")


        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(idColumn)
                imageList.add(id)
            }
        }
        imageList
    }

    private var selectedPosition: Int = 0
    val selectedIdRes: Int
        get() = images[selectedPosition]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val binding = ProductImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductImageViewHolder(binding).also{ vh ->
            binding.root.setOnClickListener{
                notifyItemChanged(selectedPosition)
                selectedPosition = vh.layoutPosition
                notifyItemChanged(selectedPosition)

            }
        }
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        holder.bind(images[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = images.size


}

