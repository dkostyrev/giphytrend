package com.kostyrev.giphytrend.details.action

sealed class DetailsViewAction : DetailsAction {

    class UserProfileClicked : DetailsViewAction()

    class Retry : DetailsViewAction()

}