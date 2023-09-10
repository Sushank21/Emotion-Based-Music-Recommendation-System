package com.example.myspotify.api

import com.example.myspotify.SearchPlaylistResponse
import com.example.myspotify.SearchResponse
import com.example.myspotify.models.CategoryPlaylistsResponse
import com.example.myspotify.models.FeaturedPlaylistsResponse
import com.example.myspotify.models.NewReleasesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyService {

    @GET("v1/search")
    fun searchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artist",
        @Header("Authorization") token: String
    ):  Call<SearchResponse>

    @GET("v1/browse/featured-playlists")
    fun getFeaturedPlaylists(
        @Query("country") country: String,
        @Query("locale") locale: String,
        @Query("timestamp") timestamp: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Header("Authorization") token: String,
    ): Call<FeaturedPlaylistsResponse>

    @GET("v1/browse/new-releases")
    fun getFeaturedPlaylists(
        @Query("country") country: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Header("Authorization") token: String,
    ): Call<NewReleasesResponse>

    @GET("v1/browse/categories/{category_id}/playlists\n")
    fun getCategoryPlaylists(
        @Path("category_id") artistId: String,
        @Query("country") country: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Header("Authorization") token: String,
    ): Call<CategoryPlaylistsResponse>

    @GET("v1/search")
    fun searchPlaylist(
        @Query("q") query: String,
        @Query("market") market: String,
        @Query("type") type: String = "playlist",
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        //@Query("include_external") include_external: String = "audio"
    ): Call<SearchPlaylistResponse>

    //    @GET("v1/artists/{id}/albums")
//    fun searchArtistAlbum(
//        @Path("id") artistId: String,
//        @Query("market") market: String,
//        @Header("Authorization") token: String,
//        @Query("limit") limit: Int = 20,
//        @Query("offset") offset: Int = 0,
//        @Query("include_groups") includeGroups: List<String>? = listOf("single")
//    ): Call<ArtistAlbumResponse>


}