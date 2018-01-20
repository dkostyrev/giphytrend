package com.kostyrev.giphytrend.api.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

class User(@SerializedName("username") val username: String,
           @SerializedName("display_name") val displayName: String,
           @SerializedName("avatar_url") val avatar: String,
           @SerializedName("profile_url") val profile: Uri,
           @SerializedName("twitter") val twitter: String?)