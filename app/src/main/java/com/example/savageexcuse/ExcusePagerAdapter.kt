package com.example.savageexcuse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savageexcuse.data.Excuse
import com.example.savageexcuse.databinding.ItemExcuseCardBinding

class ExcusePagerAdapter(
    private val onFavoriteClick: (Excuse) -> Unit,
    private val onShareClick: (Excuse) -> Unit
) : ListAdapter<Excuse, ExcusePagerAdapter.ExcuseViewHolder>(ExcuseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExcuseViewHolder {
        val binding = ItemExcuseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExcuseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExcuseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExcuseViewHolder(private val binding: ItemExcuseCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(excuse: Excuse) {
            binding.tvCategory.text = excuse.category
            binding.tvExcuse.text = "“${excuse.text}”"

            val favoriteIcon = if (excuse.isFavorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
            binding.btnFavorite.setImageResource(favoriteIcon)

            binding.btnFavorite.setOnClickListener { onFavoriteClick(excuse) }
            // Optional: Long click to copy text
        }
    }
}

class ExcuseDiffCallback : DiffUtil.ItemCallback<Excuse>() {
    override fun areItemsTheSame(oldItem: Excuse, newItem: Excuse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Excuse, newItem: Excuse): Boolean {
        return oldItem == newItem
    }
}
