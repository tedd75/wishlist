package com.example.wishlist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wishlist.data.model.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductDataBase: RoomDatabase() {
    abstract val product: ProductDao

    companion object{
        fun open(context: Context): ProductDataBase = Room.databaseBuilder(
            context, ProductDataBase::class.java, "product.db"
        ).build()
    }
}