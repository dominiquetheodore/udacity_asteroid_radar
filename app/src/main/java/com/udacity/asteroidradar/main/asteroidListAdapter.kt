package com.udacity.asteroidradar.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

class AsteroidsAdapter(private val onClickListener: OnClickListener) : ListAdapter<Asteroid, AsteroidsAdapter.AsteroidMyViewHolder>(DiffCallback) {
    class AsteroidMyViewHolder(private var binding: ItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidMyViewHolder {
        return AsteroidMyViewHolder(ItemAsteroidBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AsteroidMyViewHolder, position: Int) {
        val asteroid = getItem(position)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }

        holder.bind(asteroid)
    }

    object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

}