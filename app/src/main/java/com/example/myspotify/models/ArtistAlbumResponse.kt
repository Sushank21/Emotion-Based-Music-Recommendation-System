package com.example.myspotify

import com.example.myspotify.models.Copyright
import com.example.myspotify.models.ExternalUrls
import com.example.myspotify.models.Image
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class ArtistAlbumResponse(
    val album : Albums
): Serializable

data class Albums(
    val items: List<AlbumResponse>
):Serializable

data class AlbumResponse(
    val limit: Int, // indicates the number of items in the list
    @JsonProperty("next") val nextPageUrlString: String,
    val offset: Int,
    @JsonProperty("previous") val previousPageUrlString: String?,
    @JsonProperty("total") val totalNumberOfItemsAvailable: Int
): Serializable

data class AlbumItem(
    val album_type: String,
    val total_tracks: Int,
    val available_markets: List<String>,
    val external_urls: ExternalUrls,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val restrictions: Restrictions?,
    val uri: String,
    val copyrights: List<Copyright>,
    //val external_ids: ExternalIds?,
    val genres: List<String>,
    val label: String?,
    val popularity: Int,
    val album_group: String?,
    //val artists: List<SimplifiedArtistObject>
): Serializable

data class Restrictions(
    val type: String
)

