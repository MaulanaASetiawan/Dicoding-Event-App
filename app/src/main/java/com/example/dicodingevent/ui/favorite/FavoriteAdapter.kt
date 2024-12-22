package com.example.dicodingevent.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.local.entity.FavoriteEntity
import com.example.dicodingevent.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val listener: (FavoriteEntity) -> Unit) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var listFavorite : List<FavoriteEntity> = emptyList()

    fun setFavorite(favorite: List<FavoriteEntity>?) {
        if (favorite != null) {
            this.listFavorite = favorite
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemsFavoriteBinding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(itemsFavoriteBinding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = listFavorite[position]
        holder.bind(favorite, listener)
    }

    override fun getItemCount(): Int = listFavorite.size

    class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: FavoriteEntity, listener: (FavoriteEntity) -> Unit) {
                binding.txtNameUpcoming.text = favorite.name
                binding.txtOwnerUpcoming.text = favorite.owner
                Glide.with(binding.imgUpcoming.context)
                    .load(favorite.imageLogo)
                    .into(binding.imgUpcoming)

            itemView.setOnClickListener { listener(favorite) }
        }
    }
}


