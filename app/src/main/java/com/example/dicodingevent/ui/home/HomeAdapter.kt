package com.example.dicodingevent.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemFinishedBinding
import com.example.dicodingevent.databinding.ItemUpcomingBinding

class HomeAdapter(
    private val events: List<ListEventsItem>, private val isList: Boolean
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var onItemClick: ((ListEventsItem) -> Unit)? = null

    fun setOnItemClickListener (listener: (ListEventsItem) -> Unit){
        onItemClick = listener
    }

    class HomeViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(event: ListEventsItem, onItemClick: ((ListEventsItem) -> Unit)?){
            when(binding){
                is ItemUpcomingBinding -> {
                    val quota = event.quota - event.registrants
                    binding.txtNameUpcoming.text = event.name
                    binding.txtOwnerUpcoming.text = event.ownerName
                    binding.txtQuotaUpcoming.text = quota.toString()
                    Glide.with(binding.imgUpcoming.context)
                        .load(event.imageLogo)
                        .into(binding.imgUpcoming)
                }
                is ItemFinishedBinding -> {
                    binding.txtNameFinished.visibility = View.GONE
                    Glide.with(binding.imgFinished.context)
                        .load(event.imageLogo)
                        .into(binding.imgFinished)
                }
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(event)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = if(isList){
            ItemFinishedBinding.inflate(inflater, parent, false)
        } else {
            ItemUpcomingBinding.inflate(inflater, parent, false)
        }
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event, onItemClick)
    }

    override fun getItemCount(): Int = events.size
}