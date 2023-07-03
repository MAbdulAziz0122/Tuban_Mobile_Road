package com.example.jalan_tuban_mobile.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.jalan_tuban_mobile.R
import com.example.jalan_tuban_mobile.model.Road

class RoadListAdapter(
    private val onItemClickListener: (Road) -> Unit
): ListAdapter<Road, RoadListAdapter.RoadViewHolder>(WORDS_COMPARATOR){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int   ): RoadViewHolder{
        return RoadViewHolder.create(parent)
    }
    override fun onBindViewHolder(holder: RoadViewHolder, positon: Int){
        val road = getItem(positon)
        holder.bind(road)
        holder.itemView.setOnClickListener{
            onItemClickListener(road)
        }
    }
    class RoadViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        fun bind(road: Road?) {
            nameTextView.text = road?.name
            addressTextView.text = road?.address

        }

        companion object {
            fun create(parent: ViewGroup): RoadViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_road, parent, false)
                return RoadViewHolder(view)
            }
        }

    }

    companion object{
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Road>(){
            override fun areItemsTheSame(oldItem: Road, newItem: Road): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Road, newItem: Road): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}

