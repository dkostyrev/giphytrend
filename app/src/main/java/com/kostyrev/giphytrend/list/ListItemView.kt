package com.kostyrev.giphytrend.list

interface ListItemView<in T : ListItem> {

    fun render(item: T)

}