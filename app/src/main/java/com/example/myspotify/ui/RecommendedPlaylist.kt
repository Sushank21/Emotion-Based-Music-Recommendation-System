package com.example.myspotify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myspotify.R
import com.example.myspotify.SearchPlaylistResponse
import com.example.myspotify.adapters.PlaylistAdapter
import com.example.myspotify.api.LoggingInterceptor
import com.example.myspotify.api.SpotifyService
import com.example.myspotify.databinding.ActivityRecommendedPlaylistBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecommendedPlaylist : AppCompatActivity() {
    lateinit var binding: ActivityRecommendedPlaylistBinding
    lateinit var emotion:String
    lateinit var padapter:PlaylistAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendedPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        if (intent != null) {
            val receivedValue = intent.getStringExtra("CameraActivity")
            if (receivedValue != null) {
                emotion = receivedValue
            }
        }
        val token = MainActivity.retrieveToken(this)
        requestForSongs(token)
    }

    private fun requestForSongs(token:String?) {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .build()
        Log.i("SearchActivity", "$token")
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val spotifyService = retrofit.create(SpotifyService::class.java)
        playlists(spotifyService, token)
    }

    private fun playlists(spotifyService: SpotifyService, token: String?) {

        val playlistCall: Call<SearchPlaylistResponse> = spotifyService.searchPlaylist(
            "$emotion Songs",
            "IN",
            "playlist",
            "Bearer $token",
        )
        playlistCall.enqueue(object : Callback<SearchPlaylistResponse> {
            override fun onResponse(
                call: Call<SearchPlaylistResponse>,
                response: Response<SearchPlaylistResponse>
            ) {
                if (response.isSuccessful) {
                    val playlistResponse = response.body()
                    if(playlistResponse != null) {
                        padapter = PlaylistAdapter(this@RecommendedPlaylist, playlistResponse.playlists.items)
                        binding.rpRecy.adapter = padapter
                        binding.rpRecy.layoutManager = LinearLayoutManager(this@RecommendedPlaylist)
                    }
                } else {
                    Log.e(
                        "PlaylistResponse",
                        "Search playlist API request failed: ${response.code()}"
                    )
                }
            }
            override fun onFailure(call: Call<SearchPlaylistResponse>, t: Throwable) {
                Log.e("SearchActivity", "Search Artists API request failed: ", t)
            }
        })
    }
}