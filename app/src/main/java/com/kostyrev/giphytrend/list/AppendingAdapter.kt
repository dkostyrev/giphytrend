package com.kostyrev.giphytrend.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class AppendingAdapter<V : ListItemView<I>, I : ListItem>(data: List<I>) : BaseAdapter<V, I>(data) {

    interface AppendingListener {

        fun onAppend()

    }

    var listener: AppendingListener? = null
    var canAppend: Boolean = true

    override fun getItemViewType(position: Int): Int {
        return if (isAppendingPosition(position)) {
            APPENDING_ITEM_VIEW_TYPE
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == APPENDING_ITEM_VIEW_TYPE) {
            val inflater = LayoutInflater.from(parent.context)
            return createAppendingViewHolder(inflater, parent)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isAppendingPosition(position)) {
            listener?.onAppend()
        } else {
            super.onBindViewHolder(holder, position)
        }
    }

    private fun isAppendingPosition(position: Int) = position == itemCount - 1 && canAppend

    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return if (canAppend) {
            count + 1
        } else {
            count
        }
    }

    protected abstract fun createAppendingViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder

}

private const val APPENDING_ITEM_VIEW_TYPE = -1