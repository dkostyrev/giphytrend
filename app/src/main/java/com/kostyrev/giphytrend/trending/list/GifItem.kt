package com.kostyrev.giphytrend.trending.list

import com.kostyrev.giphytrend.api.model.Image
import com.kostyrev.giphytrend.list.ListItem

class GifItem(override val id: String,
              val image: Image) : ListItem