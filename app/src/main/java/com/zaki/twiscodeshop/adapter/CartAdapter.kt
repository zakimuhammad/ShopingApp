package com.zaki.twiscodeshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaki.twiscodeshop.R
import com.zaki.twiscodeshop.database.DatabaseModel
import com.zaki.twiscodeshop.databinding.ItemCartBinding
import com.zaki.twiscodeshop.ui.viewmodel.ShopViewModel

class CartAdapter (private val viewModel: ShopViewModel): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var binding: ItemCartBinding

    private val differCallback = object : DiffUtil.ItemCallback<DatabaseModel>() {
        override fun areItemsTheSame(oldItem: DatabaseModel, newItem: DatabaseModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DatabaseModel, newItem: DatabaseModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val shop = differ.currentList[position]
        holder.itemView.apply {
            val image = shop.default_photo
            val weight = shop.weight.toDouble() / 1000 * shop.quantity

            Glide.with(this)
                .load("https://ranting.twisdev.com/uploads/$image")
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(binding.ivImageCart)

            binding.btnPlus.setOnClickListener {
                shop.quantity++
                viewModel.updateItem(shop.id, shop.quantity)
            }

            if (shop.quantity > 0) {
                binding.btnMinus.setOnClickListener {
                    shop.quantity--
                    viewModel.updateItem(shop.id, shop.quantity)
                }
            }

            binding.tvItemWeight.text = String.format("%s kg", weight)
            binding.tvItemName.text = shop.title
            binding.tvItemPrice.text = String.format("Rp. %s", shop.price)
            binding.tvItemCondition.text = shop.condition_of_item
            binding.tvItemQuantity.text = shop.quantity.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}