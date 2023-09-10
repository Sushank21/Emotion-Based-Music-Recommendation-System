package com.example.myspotify.models

import com.example.myspotify.models.Item

data class NewReleases(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<Item>
)
