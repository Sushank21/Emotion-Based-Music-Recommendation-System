package com.example.myspotify.models

import com.example.myspotify.models.ExternalUrls
import com.example.myspotify.models.Followers

data class Owner(
    val display_name: String?,
    val external_urls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)