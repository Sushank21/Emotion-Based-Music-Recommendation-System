package com.example.myspotify.models

import com.example.myspotify.Playlists
import java.io.Serializable

data class FeaturedPlaylistsResponse (
    val playlists: Playlists
        ): Serializable
