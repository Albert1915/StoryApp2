package com.alz19.storyapp2.helper.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.alz19.storyapp2.databinding.CardStoryBinding
import com.alz19.storyapp2.model.entity.ListStoryItem
import com.alz19.storyapp2.view.detail.DetailActivity

class StoryListAdapter : PagingDataAdapter<ListStoryItem, StoryListAdapter.StoryListViewHolder>(
    DIFF_CALLBACK
) {

    override fun onBindViewHolder(holder: StoryListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListViewHolder {
        val binding = CardStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryListViewHolder(binding)
    }

    inner class StoryListViewHolder(private val binding: CardStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.textStoryListTitle.text = data.name
            binding.textStoryListDesc.text = data.description
            binding.textStoryListDate.text = data.createdAt
            Glide.with(itemView.context)
                .load(data.photoUrl ?: "https://i.stack.imgur.com/l60Hf.png")
                .into(binding.imageStoryList)
            itemView.setOnClickListener {
                val moveIntent = Intent(itemView.context, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_ID, data.id)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imageStoryList, "profile"),
                        Pair(binding.textStoryListTitle, "title"),
                        Pair(binding.textStoryListDesc, "description"),
                        Pair(binding.textStoryListDate, "date")
                    )
                itemView.context.startActivity(moveIntent, optionsCompat.toBundle())
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}