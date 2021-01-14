package com.zaki.twiscodeshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaki.twiscodeshop.R
import com.zaki.twiscodeshop.database.DatabaseModel
import com.zaki.twiscodeshop.databinding.ItemShopBinding
import com.zaki.twiscodeshop.model.ShopItem

class ShopAdapter: ListAdapter<ShopItem, ShopAdapter.ShopViewHolder>(ShopDiffCallback()) {

    class ShopViewHolder(binding: ItemShopBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var binding: ItemShopBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        binding = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val shop = getItem(position)
        holder.itemView.apply {
            val image = shop.default_photo.img_path

            var name = shop.user.user_name
            if (name.length > 15) {
                name = name.substring(0, 15)
                binding.tvItemSeller.text = String.format("%s...", name)
            } else {
                binding.tvItemSeller.text = name
            }

            Glide.with(this)
                .load("https://ranting.twisdev.com/uploads/$image")
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(binding.ivImage)
            binding.tvItemName.text = shop.title
            binding.tvItemPrice.text = String.format("Rp. %s", shop.price)
            binding.tvItemLocation.text = shop.location_name

            val databaseModel = DatabaseModel(
                    shop.id,
                    shop.price,
                    shop.title,
                    shop.location_name,
                    shop.user.user_name,
                    shop.default_photo.img_path,
                    shop.weight,
                    shop.condition_of_item.name,
                    1
            )

            binding.btnCart.setOnClickListener {
                onItemClickListener?.let { it(databaseModel) }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private var onItemClickListener: ((DatabaseModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (DatabaseModel) -> Unit) {
        onItemClickListener = listener
    }
}

class ShopDiffCallback : DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}