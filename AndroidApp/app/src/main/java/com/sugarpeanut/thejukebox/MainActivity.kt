package com.sugarpeanut.thejukebox

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.sugarpeanut.thejukebox.Models.searchResult
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*

import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(findViewById(R.id.my_toolbar))

        //recyclresViw_main.setBackgroundColor(Color.BLUE)
        recyclresViw_main.layoutManager = LinearLayoutManager(this)
      fetchJson()


    }

    fun fetchJson(){
        println("Attempting to Fetch JSON")

        val url = "http://10.0.2.2:8080/search/frank%20ocean"
        val urlPlaylist = "http://10.0.2.2:8080/playlist/getall"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
               // println(body)

                val gson = GsonBuilder().create()

                val result = gson.fromJson(body,searchResult::class.java)


                runOnUiThread{
                    recyclresViw_main.adapter = mainAdapter(result)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Faild to execute" + e.message)
            }
        })
    }
}
