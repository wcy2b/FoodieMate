package com.example.foodiemate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodiemate.R
import com.example.foodiemate.data.model.RestaurantRecommendation

class RecommendAdapter(
    private var items: List<RestaurantRecommendation> = emptyList()
) : RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder>() {

    fun submitList(newItems: List<RestaurantRecommendation>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend, parent, false)
        return RecommendViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvType: TextView = itemView.findViewById(R.id.tvType)
        private val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)

        fun bind(item: RestaurantRecommendation) {
            tvName.text = item.name
            tvRating.text = "${item.rating}分"
            tvPrice.text = item.price
            tvType.text = item.type
            tvDistance.text = item.distance
            
            ivImage.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_home_24) // Fallback icon
                error(R.drawable.ic_home_24)
            }
        }
    }
}

