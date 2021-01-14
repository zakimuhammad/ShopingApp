package com.zaki.twiscodeshop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zaki.twiscodeshop.R
import com.zaki.twiscodeshop.database.ShopDatabase
import com.zaki.twiscodeshop.repository.ShopRepository
import com.zaki.twiscodeshop.ui.viewmodel.ShopViewModel
import com.zaki.twiscodeshop.ui.viewmodel.ShopViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ShopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shopRepository = ShopRepository(ShopDatabase(this))
        val viewModelFactory = ShopViewModelFactory(application, shopRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShopViewModel::class.java)
    }
}