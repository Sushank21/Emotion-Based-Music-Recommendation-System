package com.example.myspotify

import com.example.myspotify.models.ExternalUrls

//
data class ArtistSearch(
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)