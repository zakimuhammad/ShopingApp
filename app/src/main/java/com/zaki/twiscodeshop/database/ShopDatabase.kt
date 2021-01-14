package com.zaki.twiscodeshop.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [DatabaseModel::class],
        version = 1
)
abstract class ShopDatabase : RoomDatabase() {

    abstract fun getShopDao(): ShopDao

    companion object {
        @Volatile
        private var instance: ShopDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                ShopDatabase::class.java,
                "shop.db"
        ).build()
    }
}