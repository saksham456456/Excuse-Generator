package com.example.savageexcuse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savageexcuse.data.Excuse
import com.example.savageexcuse.databinding.ItemExcuseStashBinding

class StashAdapter(
    private val onRemoveFavorite: (Excuse) -> Unit
) : ListAdapter<Excuse, StashAdapter.StashViewHolder>(ExcuseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StashViewHolder {
        val binding = ItemExcuseStashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StashViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StashViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StashViewHolder(private val binding: ItemExcuseStashBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(excuse: Excuse) {
            binding.tvCategory.text = excuse.category
            binding.tvExcuseText.text = "“${excuse.text}”"

            binding.btnUnfavorite.setOnClickListener {
                onRemoveFavorite(excuse)
            }
        }
    }
}
