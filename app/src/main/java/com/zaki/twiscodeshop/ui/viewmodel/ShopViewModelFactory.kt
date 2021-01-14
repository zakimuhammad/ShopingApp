package com.zaki.twiscodeshop.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaki.twiscodeshop.repository.ShopRepository

@Suppress("UNCHECKED_CAST")
class ShopViewModelFactory(
        private val application: Application,
        private val shopRepository: ShopRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShopViewModel(application, shopRepository) as T
    }
}