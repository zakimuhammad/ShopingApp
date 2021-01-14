package com.zaki.twiscodeshop.model

data class ShopItem(
    val id: String,
    val price: String,
    val title: String,
    val location_name: String,
    val user: User,
    val default_photo: DefaultPhoto,
    val weight: String,
    val condition_of_item: ItemCondition
)