package com.zaki.twiscodeshop.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaki.twiscodeshop.ShopApplication
import com.zaki.twiscodeshop.database.DatabaseModel
import com.zaki.twiscodeshop.model.ShopItem
import com.zaki.twiscodeshop.repository.ShopRepository
import com.zaki.twiscodeshop.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ShopViewModel(
        val app: Application,
        private val repository: ShopRepository
) : AndroidViewModel(app) {

    val shopData: MutableLiveData<Resource<List<ShopItem>>> = MutableLiveData()
    private var shopResponse: List<ShopItem> = listOf()

    init {
        getAllShop()
    }

    private fun getAllShop() = viewModelScope.launch { safeShopItemCall() }

    fun saveShopData(shop: DatabaseModel) = viewModelScope.launch { repository.insertShopData(shop) }

    fun updateItem(id: String, quantity: Int) = viewModelScope.launch { repository.updateQuantityItem(id, quantity) }

    fun deleteShopItem(shop: DatabaseModel) = viewModelScope.launch { repository.deleteShopItem(shop) }

    fun getSavedShop() = repository.getAllSavedShop()

    private suspend fun safeShopItemCall() {
        shopData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getAllItemShop()
                shopData.postValue(handleShopResponse(response))
            } else {
                shopData.postValue(Resource.Error("No Internet Connection!"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> shopData.postValue(Resource.Error("Network Failure!"))
                else -> shopData.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private fun handleShopResponse(response: Response<List<ShopItem>>): Resource<List<ShopItem>> {
        if (response.isSuccessful) {
            response.body()?.let {
                shopResponse = it
                return Resource.Success(shopResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ShopApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                    connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}