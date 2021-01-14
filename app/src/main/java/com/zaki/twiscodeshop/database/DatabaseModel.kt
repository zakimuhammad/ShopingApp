package com.zaki.twiscodeshop.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "shop"
)
data class DatabaseModel(
        @PrimaryKey
    val id: String,
        val price: String,
        val title: String,
        val location_name: String,
        val user: String,
        val default_photo: String,
        val weight: String,
        val condition_of_item: String,
        var quantity: Int
)