package com.zurmati.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.zurmati.newsapp.R
import com.zurmati.newsapp.models.Sources

class SourcesAdapter(
    val context: Context,
    val sources: MutableList<Sources>,
    val listener: (Sources) -> Unit
) :
    RecyclerView.Adapter<SourcesAdapter.SourcesHolder>() {


    class SourcesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSource: TextView = itemView.findViewById(R.id.txt_source)
        val itemCard: MaterialCardView = itemView.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourcesHolder {
        return SourcesHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_source_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return sources.size
    }

    override fun onBindViewHolder(holder: SourcesHolder, position: Int) {

        holder.txtSource.text = sources[position].name

        holder.itemView.setOnClickListener {
            listener.invoke(sources[position])
        }

    }
}