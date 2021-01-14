package com.zaki.twiscodeshop.ui.fragment

import android.os.Bundle
import android.view.*
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
            when(response) {
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

        shopAdapter.setOnItemClickListener {
            viewModel.saveShopData(it)
            Snackbar.make(requireActivity().findViewById(R.id.fragmentList), "Item Successfully Added to Cart", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuCart) {
            findNavController().navigate(R.id.action_listFragment_to_keranjangFragment)
            return true
        }
        return super.onOptionsItemSelected(item)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}