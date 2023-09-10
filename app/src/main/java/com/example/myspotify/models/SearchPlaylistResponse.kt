package com.example.myspotify

import com.example.myspotify.models.Item
import java.io.Serializable

data class SearchPlaylistResponse(
    val playlists: Playlists
):Serializable

data class Playlists(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<Item>
)



//data class PlaylistItem(
//    val collaborative: Boolean,
//    val description: String,
//    val external_urls: ExternalUrls,
//    val href: String,
//    val id: String,
//    val images: List<Image>,
//    val name: String,
//    val owner: Owner,
//    val primary_color: String?,
//    val public: Boolean?,
//    val snapshot_id: String,
//    val tracks: Tracks,
//    val type: String,
//    val uri: String
//)
//
//data class ExternalUrls(
//    val spotify: String
//)
//
//data class Image(
//    val height: Int,
//    val url: String,
//    val width: Int
//)
//
//data class Owner(
//    val display_name: String,
//    val external_urls: ExternalUrls,
//    val href: String,
//    val id: String,
//    val type: String,
//    val uri: String
//)
//
//data class Tracks(
//    val href: String,
//    val total: Int
//)
//
//data class SearchPlaylistResponse(
//    val playlists: PlaylistData
//)
//
//data class PlaylistData(
//    val href: String,
//    val items: List<PlaylistItem>,
//    val limit: Int,
//    val next: String?,
//    val offset: Int,
//    val previous: String?,
//    val total: Int
//)
