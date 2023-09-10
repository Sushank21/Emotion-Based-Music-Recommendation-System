package com.example.myspotify

import java.io.Serializable

data class SearchResponse(
    val artists: Artists?
):Serializable

data class Artists(
    val items: List<Artist>?
):Serializable

data class Artist(
    val id: String,
    val name: String,
    val popularity: Int,
    val genres: List<String>
) : Serializable