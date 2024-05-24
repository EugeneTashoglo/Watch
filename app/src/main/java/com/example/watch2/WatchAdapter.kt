package com.example.watch2


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class WatchAdapter(private val watchList: List<Watch>) : RecyclerView.Adapter<WatchAdapter.WatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_watch, parent, false)
        return WatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchViewHolder, position: Int) {
        val watch = watchList[position]
        holder.bind(watch)
    }

    override fun getItemCount(): Int = watchList.size

    inner class WatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewWatch)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)

        fun bind(watch: Watch) {
            textViewName.text = watch.name
            Picasso.get()
                .load(watch.imageFileName)
                .into(imageView)

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, WatchDetailActivity::class.java).apply {
                    putExtra("name", watch.name)
                    putExtra("rating", watch.rating)
                    putExtra("price", watch.price)
                    putExtra("description", watch.description)
                    putExtra("imageFrameName", watch.imageFrameName)
                }
                context.startActivity(intent)
            }
        }
    }
}
