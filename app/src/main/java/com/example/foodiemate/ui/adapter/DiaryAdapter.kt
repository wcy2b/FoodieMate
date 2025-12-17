package com.example.foodiemate.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiemate.R
import com.example.foodiemate.data.entity.Diary

class DiaryAdapter(
    private val onItemClick: (Diary) -> Unit
) : ListAdapter<Diary, DiaryAdapter.DiaryViewHolder>(DiaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        return DiaryViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiaryViewHolder(itemView: View, val onItemClick: (Diary) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        private val ivFoodImage: ImageView = itemView.findViewById(R.id.ivFoodImage)

        fun bind(diary: Diary) {
            tvFoodName.text = diary.foodName
            tvDate.text = diary.date
            tvContent.text = diary.content
            
            if (!diary.imageUri.isNullOrEmpty()) {
                ivFoodImage.visibility = View.VISIBLE
                try {
                    ivFoodImage.setImageURI(Uri.parse(diary.imageUri))
                } catch (e: Exception) {
                    ivFoodImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } else {
                ivFoodImage.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onItemClick(diary)
            }
        }
    }

    class DiaryDiffCallback : DiffUtil.ItemCallback<Diary>() {
        override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }
    }
}
