@file:Suppress("IllegalIdentifier")

package com.kostyrev.giphytrend.details.middleware

import android.net.Uri
import com.jakewharton.rxrelay2.PublishRelay
import com.kostyrev.giphytrend.api.model.User
import com.kostyrev.giphytrend.details.DetailsState
import com.kostyrev.giphytrend.details.action.DetailsAction
import com.kostyrev.giphytrend.details.action.DetailsViewAction
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.observers.TestObserver
import org.junit.Test

class NavigationMiddlewareTest {

    private val router: NavigationMiddleware.Router = mock()
    private val state = PublishRelay.create<DetailsState>()
    private val actions = PublishRelay.create<DetailsAction>()

    @Test
    fun `user profile clicked - calls router with user profile uri - user is not null`() {
        val profile: Uri = mock()
        create()

        state.accept(DetailsState(user = createUser(profile)))
        actions.accept(DetailsViewAction.UserProfileClicked())

        verify(router).followUri(profile)
    }

    @Test
    fun `user profile clicked - does not call router with user profile uri - user is null`() {
        create()

        state.accept(DetailsState(user = null))
        actions.accept(DetailsViewAction.UserProfileClicked())

        verify(router, never()).followUri(any())
    }

    private fun createUser(profile: Uri): User {
        return User(
                username = "username",
                displayName = "display name",
                avatar = "avatar",
                profile = profile,
                twitter = null
        )
    }

    private fun create(): TestObserver<DetailsAction> {
        return NavigationMiddleware(router).create(actions, state).test()
    }

}