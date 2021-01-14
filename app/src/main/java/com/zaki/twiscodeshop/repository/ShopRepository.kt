package com.zaki.twiscodeshop.repository

import com.zaki.twiscodeshop.api.RetrofitInstance
import com.zaki.twiscodeshop.database.DatabaseModel
import com.zaki.twiscodeshop.database.ShopDatabase

class ShopRepository(private val db: ShopDatabase) {
    suspend fun getAllItemShop() = RetrofitInstance().services.getAllItem()

    suspend fun insertShopData(shop: DatabaseModel) = db.getShopDao().insertShopData(shop)

    fun getAllSavedShop() = db.getShopDao().getAllShop()

    suspend fun updateQuantityItem(id: String, quantity: Int) = db.getShopDao().updateItemQuantity(id, quantity)
}