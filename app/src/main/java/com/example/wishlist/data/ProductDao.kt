package com.example.wishlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.wishlist.data.model.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM product;")
    fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE id = :id;")
    fun getProduct(id: Long): ProductEntity

//    @Query("SELECT * FROM product ORDER BY name ASC;")
//    fun getAllSortedByName(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(newProduct: ProductEntity)

    @Update
    fun updateProduct(newProduct: ProductEntity)

    @Query("DELETE FROM product WHERE id= :id")
    fun remove(id: Long)
}