package com.example.myspotify.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myspotify.*
import com.example.myspotify.adapters.FeaturedPlaylistAdapter
import com.example.myspotify.adapters.PlaylistAdapter
import com.example.myspotify.api.LoggingInterceptor
import com.example.myspotify.api.SpotifyService
import com.example.myspotify.databinding.ActivityMainBinding
import com.example.myspotify.models.CategoryPlaylistsResponse
import com.example.myspotify.models.FeaturedPlaylistsResponse
import com.example.myspotify.models.NewReleasesResponse
import com.google.gson.GsonBuilder
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val clientId = "1c8978c9e52d48c5bbc204d0676d061b"
    private val redirectUri = "com.example.myspotify://callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var loginLauncher: ActivityResultLauncher<Intent>
    private var accessToken: String? = null
    lateinit var fpAdapter: FeaturedPlaylistAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            loginResult(it.resultCode,it.data)
        }
        if(isLoggedIn()){
            Log.i("SearchActivity","$accessToken")
        }
        else{
           initiateLogin()
        }
        binding.searchBtn.setOnClickListener{
            val intent = Intent(this, RecommendedPlaylist::class.java)
            startActivity(intent)
        }
    }
    companion object {
        fun retrieveToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
            return sharedPreferences.getString("accessToken", null)
        }
    }
    private fun initiateLogin() {
        val request = createAuthorization()
        val intent = AuthorizationClient.createLoginActivityIntent(this,request)
        loginLauncher.launch(intent)
    }

    private fun storeToken(token: String?) {
        val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", token)
        editor.apply()
    }

    private fun createAuthorization():AuthorizationRequest {
        val builder = AuthorizationRequest.Builder(
            clientId,
            AuthorizationResponse.Type.TOKEN,
            redirectUri
        )
        builder.setScopes(arrayOf("streaming"))
        return builder.build()
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
        //playlists(spotifyService, token)
        featuredPlaylist(spotifyService, token)
        newReleases(spotifyService, token)
        categoryPlaylist(spotifyService,token)

    }

    private fun featuredPlaylist(spotifyService: SpotifyService, token: String?){
        Log.i("FeaturedPlaylistRequest", "$token")
        val playlistCall: Call<FeaturedPlaylistsResponse> = spotifyService.getFeaturedPlaylists(
            "IN",
            "en_IN",
            "2023-09-09T12:00:00",
            10,
            0,
            "Bearer $token",
        )
        playlistCall.enqueue(object :Callback<FeaturedPlaylistsResponse>{
            override fun onResponse(
                call: Call<FeaturedPlaylistsResponse>,
                response: Response<FeaturedPlaylistsResponse>
            ) {
                if (response.isSuccessful) {
                    val playlistResponse = response.body()
                    if(playlistResponse != null){
                        fpAdapter = FeaturedPlaylistAdapter(this@MainActivity, playlistResponse.playlists.items)
                        binding.feaRecy.adapter = fpAdapter
                        binding.feaRecy.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    }
                }
            }
            override fun onFailure(call: Call<FeaturedPlaylistsResponse>, t: Throwable) {
                Log.e("FeaturedPlaylistRequest", "FeaturedPlaylist API request failed: ", t)
            }

        })
    }

    private fun newReleases(spotifyService: SpotifyService, token: String?){

        val playlistCall: Call<NewReleasesResponse> = spotifyService.getFeaturedPlaylists(
            "IN",
            10,
            0,
            "Bearer $token",
        )
        playlistCall.enqueue(object :Callback<NewReleasesResponse>{
            override fun onResponse(
                call: Call<NewReleasesResponse>,
                response: Response<NewReleasesResponse>
            ) {
                if (response.isSuccessful) {
                    val playlistResponse = response.body()
                    if(playlistResponse != null){
                        Log.i("NewReleasesResponse", "${playlistResponse.albums.items[0].name}")
                        fpAdapter = FeaturedPlaylistAdapter(this@MainActivity, playlistResponse.albums.items)
                        binding.nrRecy.adapter = fpAdapter
                        binding.nrRecy.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    }
                }
            }

            override fun onFailure(call: Call<NewReleasesResponse>, t: Throwable) {
                Log.e("NewReleasesResponse", "NewReleasesResponse API request failed: ", t)
            }

        })
    }

    private fun categoryPlaylist(spotifyService: SpotifyService, token: String?){
        val playlistCall: Call<CategoryPlaylistsResponse> = spotifyService.getCategoryPlaylists(
            "party",
            "IN",
            10,
            0,
            "Bearer $token",
        )
        playlistCall.enqueue(object :Callback<CategoryPlaylistsResponse>{
            override fun onResponse(
                call: Call<CategoryPlaylistsResponse>,
                response: Response<CategoryPlaylistsResponse>
            ) {
                if (response.isSuccessful) {
                    Log.i("CategoryPlaylistsResponse", "IT IS NOT NULL")
                    val playlistResponse = response.body()
                    if(playlistResponse != null){
                        fpAdapter = FeaturedPlaylistAdapter(this@MainActivity, playlistResponse.playlists.items)
                        binding.cpRecy.adapter = fpAdapter
                        binding.cpRecy.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    }
                }
            }
            override fun onFailure(call: Call<CategoryPlaylistsResponse>, t: Throwable) {
                Log.e("CategoryPlaylistsResponse", "CategoryPlaylistsResponse API request failed: ", t)
            }

        })
    }

    private fun loginResult(resultCode: Int, data: Intent?) {
        val response = AuthorizationClient.getResponse(resultCode,data)
        when(response.type)
        {
            AuthorizationResponse.Type.TOKEN -> {
                accessToken = response.accessToken
                Log.i("MainActivity","Authorization Successful $accessToken")
                storeToken(accessToken)
                requestForSongs(accessToken)
            }
            AuthorizationResponse.Type.ERROR ->{

            }
            else -> {}
        }
    }
    private fun isLoggedIn() : Boolean{
        return accessToken != null
    }

}