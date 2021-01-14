package com.zaki.twiscodeshop.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopData(shop: DatabaseModel): Long

    @Query("SELECT * FROM shop")
    fun getAllShop(): LiveData<List<DatabaseModel>>

    @Query("UPDATE shop SET quantity=:quantity WHERE id=:id")
    suspend fun updateItemQuantity(id: String, quantity: Int)

    @Delete
    suspend fun deleteItemShop(shop: DatabaseModel)
}