package com.sugarpeanut.thejukebox

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

import com.sugarpeanut.thejukebox.Models.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*

import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    val spotifyConnect = SpotifyConnect.getInstance()


    val gson = GsonBuilder().create()
    var hasToken = false
    private val CLIENT_ID = "0b2b8eeca00344bb81cc5fd1f46a36eb"
    private val REDIRECT_URI = "http://www.thejukebox.com/login"

    private val REQUEST_CODE = 1337

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(findViewById(R.id.my_toolbar))

        //recyclresViw_main.setBackgroundColor(Color.BLUE)
        recyclresViw_main.layoutManager = LinearLayoutManager(this)
        deafautRecycleview()

        getFullPlaylist()


        if(!hasToken){
            getSpotifyToken()
            hasToken = true
        }



    }


    fun deafautRecycleview(){
        val song = Song("1","No numbers in the playlist","11", "https://previews.123rf.com/images/leventegyori/leventegyori1510/leventegyori151000012/47713326-geschilderd-x-teken-op-wit-wordt-ge%C3%AFsoleerd.jpg","No Songs",1)
        val songlist = listOf(song)
        val playlist = Playlist(0,songlist)
        recyclresViw_main.adapter = mainAdapter(playlist)
    }

    private fun getSpotifyToken() {

        val request = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
            .setScopes(arrayOf("user-read-private","user-read-email","streaming","playlist-modify-public"))
            .build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)


    }

    fun searchForSong(view: View){
        startActivity(Intent(this,Search::class.java))
    }

    fun getFullPlaylist(){
        println("Attempting to get the full playlist")

        val url = "http://10.0.2.2:8080/playlist/getall"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
               // println(body)


                val result = gson.fromJson(body,Playlist::class.java)
                if(result.songs.isNotEmpty()){
                    runOnUiThread{
                        recyclresViw_main.adapter = mainAdapter(result)
                    }
                }else{

                    println("somthing went wrong")
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Faild to execute" + e.message)
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {
                    val token = response.accessToken
                    SpotifyConnect.getInstance().setToken(token)
                }

                // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> Log.e("MainActivity", response.error)

            }// Most likely auth flow was cancelled
        }
    }

}
