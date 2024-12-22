package com.example.dicodingevent.ui.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemUpcomingBinding

class UpcomingAdapter : ListAdapter<ListEventsItem, UpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClick: ((ListEventsItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (ListEventsItem) -> Unit) {
        onItemClick = listener
    }

    class MyViewHolder(private val binding: ItemUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: ((ListEventsItem) -> Unit)?) {
            binding.txtNameUpcoming.text = event.name
            binding.txtOwnerUpcoming.text = binding.root.context.getString(R.string.owner, event.ownerName)
            binding.txtQuotaUpcoming.text = binding.root.context.getString(R.string.quota_remaining, event.quota - event.registrants)
            Glide.with(binding.imgUpcoming.context)
                .load(event.imageLogo)
                .into(binding.imgUpcoming)

            binding.root.setOnClickListener {
                onItemClick?.invoke(event)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
