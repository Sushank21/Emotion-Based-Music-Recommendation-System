package com.example.myspotify.models

data class Item(
    val collaborative: Boolean,
    val description: String,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val owner: Owner,
    val public: Boolean,
    val snapshot_id: String,
    val tracks: Tracks,
    val type: String,
    val uri: String
)