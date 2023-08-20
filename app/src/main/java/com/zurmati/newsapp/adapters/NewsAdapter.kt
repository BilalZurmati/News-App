package com.zurmati.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zurmati.newsapp.R
import com.zurmati.newsapp.models.Articles

class NewsAdapter(
    private val context: Context,
    private val news: MutableList<Articles>,
    private val listener: (Articles) -> Unit
) :
    RecyclerView.Adapter<NewsAdapter.NewsHolder>() {


    class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSource: TextView = itemView.findViewById(R.id.txt_headline)
        val imgItem: ImageView = itemView.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        return NewsHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_news_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {

        holder.txtSource.text = news[position].title

        Glide.with(context).load(news[position].urlToImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imgItem)

        holder.itemView.setOnClickListener {
            listener.invoke(news[position])
        }

    }
}