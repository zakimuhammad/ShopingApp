package com.zaki.twiscodeshop.api

import com.zaki.twiscodeshop.model.ShopItem
import retrofit2.Response
import retrofit2.http.POST

interface ApiInterface {

    @POST("teampsisthebest")
    suspend fun getAllItem(): Response<List<ShopItem>>

}