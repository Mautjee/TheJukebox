package com.sugarpeanut.thejukebox

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.GsonBuilder
import com.sugarpeanut.thejukebox.Models.searchResult
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.*
import java.io.IOException

class Search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        recyclresViw_search.layoutManager = LinearLayoutManager(this)



    }

    fun searchSong(view:View){
        val searchWord = searchWordText.text.toString()
        fetchJson(searchWord)
    }

    fun fetchJson(serchWord:String){
        println("Attempting to Fetch JSON")


        val url = "http://10.0.2.2:8080/search/$serchWord"


        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                // println(body)

                val gson = GsonBuilder().create()

                val result = gson.fromJson(body, searchResult::class.java)


                runOnUiThread{
                    recyclresViw_search.adapter = mainAdapter(result)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Faild to execute" + e.message)
            }
        })
    }
}
