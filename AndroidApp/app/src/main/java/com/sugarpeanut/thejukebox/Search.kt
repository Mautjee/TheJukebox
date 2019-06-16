package com.sugarpeanut.thejukebox

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.GsonBuilder
import com.sugarpeanut.thejukebox.Mocks.searchMocks
import com.sugarpeanut.thejukebox.Models.SpotifyConnect
import com.sugarpeanut.thejukebox.Models.searchResult
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.*
import java.io.IOException

class Search : AppCompatActivity() {

    val spotifyConnect = SpotifyConnect.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        recyclresViw_search.layoutManager = LinearLayoutManager(this)



    }

    fun searchSong(view:View){
        val searchWord = searchWordText.text.toString()
        fetchJsonSpotify(searchWord)
    }

    fun fetchJsonSpotify(serchWord:String){
        println("Attempting to Fetch JSON")


        val url = "https://api.spotify.com/v1/search?q='$serchWord'&type=track&market=NL&limit=10"


        val request = Request.Builder().url(url).header("Authorization","Bearer ${spotifyConnect.getToken()}").build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                // println(body)

                val gson = GsonBuilder().create()

                val result = gson.fromJson(body, searchResult::class.java)


                runOnUiThread{
                    recyclresViw_search.adapter = searchAdapter(result)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Faild to execute" + e.message)
            }
        })
    }

    fun fetchJsonLocal(searchWord:String){
        val gson = GsonBuilder().create()

        val result = gson.fromJson(searchMocks.getJson(searchWord), searchResult::class.java)

        recyclresViw_search.adapter = searchAdapter(result)
    }
}
