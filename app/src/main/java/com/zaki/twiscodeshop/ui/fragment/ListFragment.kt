package com.zaki.twiscodeshop.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zaki.twiscodeshop.R
import com.zaki.twiscodeshop.adapter.ShopAdapter
import com.zaki.twiscodeshop.databinding.FragmentListBinding
import com.zaki.twiscodeshop.ui.MainActivity
import com.zaki.twiscodeshop.ui.viewmodel.ShopViewModel
import com.zaki.twiscodeshop.utils.Resource
import kotlin.math.min

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ShopViewModel
    private lateinit var shopAdapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.shopData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { shopResponse ->
                        if (shopAdapter.currentList.isNullOrEmpty()) {
                            shopAdapter.submitList(shopResponse)
                        }
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An Error Occurred: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.getSavedShop().observe(viewLifecycleOwner, { shop ->
            mCartItemCount = shop.size
        })

        shopAdapter.setOnItemClickListener {
            viewModel.saveShopData(it)
            mCartItemCount++
            setupBadge()
            Snackbar.make(requireActivity().findViewById(R.id.fragmentList), "Item Successfully Added to Cart", Snackbar.LENGTH_SHORT).show()
        }
    }

    private var isLoading = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView() {
        shopAdapter = ShopAdapter()
        binding.rvListItem.apply {
            adapter = shopAdapter
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
        }
    }

    private lateinit var textCartItemCount: TextView
    var mCartItemCount = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        val menuItem = menu.findItem(R.id.action_cart)

        val actionView = menuItem.actionView
        textCartItemCount = actionView.findViewById(R.id.cart_badge)

        setupBadge()

        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_cart) {
            findNavController().navigate(R.id.action_listFragment_to_keranjangFragment)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBadge() {
        if (mCartItemCount == 0) {
            if (textCartItemCount.visibility != View.GONE) {
                textCartItemCount.visibility = View.GONE
            }
        } else {
            textCartItemCount.text = min(mCartItemCount, 99).toString()
            if (textCartItemCount.visibility != View.VISIBLE) {
                textCartItemCount.visibility = View.VISIBLE
            }
        }
    }
}