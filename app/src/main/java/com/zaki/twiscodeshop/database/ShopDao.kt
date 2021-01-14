package com.zaki.twiscodeshop.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopData(shop: DatabaseModel): Long

    @Query("SELECT * FROM shop")
    fun getAllShop(): LiveData<List<DatabaseModel>>

    @Query("UPDATE shop SET quantity=:quantity WHERE id=:id")
    suspend fun updateItemQuantity(id: String, quantity: Int)
}