package com.kostyrev.giphytrend.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseAdapter<V : ListItemView<I>, I : ListItem>(data: List<I>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<I> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var inflater: LayoutInflater? = null

    abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createViewHolder(inflater ?: parent.createInflater(), parent, viewType)
    }

    private fun ViewGroup.createInflater() = LayoutInflater.from(context)
            .also { inflater = it }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder as V
        view.render(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long {
        return data[position].id.hashCode().toLong()
    }

}