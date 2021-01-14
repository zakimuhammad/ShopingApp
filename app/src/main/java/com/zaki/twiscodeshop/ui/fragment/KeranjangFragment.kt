package com.zaki.twiscodeshop.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaki.twiscodeshop.adapter.CartAdapter
import com.zaki.twiscodeshop.databinding.FragmentKeranjangBinding
import com.zaki.twiscodeshop.ui.MainActivity
import com.zaki.twiscodeshop.ui.viewmodel.ShopViewModel

class KeranjangFragment : Fragment() {

    private lateinit var binding: FragmentKeranjangBinding
    lateinit var viewModel: ShopViewModel
    lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentKeranjangBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = "Cart"

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.getSavedShop().observe(viewLifecycleOwner, { shop ->
            cartAdapter.differ.submitList(shop)

            var total = 0
            for (i in shop.indices) {
                total += shop[i].price.toInt() * shop[i].quantity
            }

            binding.tvTotalPrice.text = String.format("Rp. %s", total)

            cartAdapter.notifyDataSetChanged()
        })
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(viewModel)
        binding.rvCart.adapter = cartAdapter
        binding.rvCart.layoutManager = LinearLayoutManager(context)
    }
}