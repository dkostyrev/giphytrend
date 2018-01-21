package com.kostyrev.giphytrend.details.action

import com.kostyrev.giphytrend.api.model.Gif

sealed class LoadAction : DetailsAction {

    class Loading : LoadAction()

    class Loaded(val result: Gif) : LoadAction()

    class Error(val error: String) : LoadAction()

}