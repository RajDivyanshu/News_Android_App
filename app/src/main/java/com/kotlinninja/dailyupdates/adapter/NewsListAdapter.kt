package com.kotlinninja.dailyupdates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinninja.dailyupdates.model.News

class NewsListAdapter(private val listener: NewsItemClicked,val items:ArrayList<News>) :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {


//    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)

        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.name.text = currentItem.publishedAt
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)

//        holder.rowContent.setOnClickListener {

    }

//    fun updateNews(updatedNews: ArrayList<News>){
//        items.clear()
//        items.addAll(updatedNews)
//    }

    override fun getItemCount(): Int {
        return items.size
    }


    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.txtTitle)
        val image: ImageView = itemView.findViewById(R.id.imgNews)
        val name: TextView = itemView.findViewById(R.id.txtAuthor)
    }


}

//class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val titleView: TextView = itemView.findViewById(R.id.txtTitle)
//    val image: ImageView = itemView.findViewById(R.id.imgNews)
//    val author: TextView = itemView.findViewById(R.id.txtAuthor)
//}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}