package com.sugarpeanut.thejukebox

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.sugarpeanut.thejukebox.Models.Image
import com.sugarpeanut.thejukebox.Models.Song
import com.sugarpeanut.thejukebox.Models.searchResult
import kotlinx.android.synthetic.main.songmainrow.view.*
import kotlinx.android.synthetic.main.songsearchrow.view.*
import kotlinx.android.synthetic.main.songsearchrow.view.imageView_albumcover
import kotlinx.android.synthetic.main.songsearchrow.view.textView_songartist
import kotlinx.android.synthetic.main.songsearchrow.view.textView_songduration
import kotlinx.android.synthetic.main.songsearchrow.view.textview_songname
import android.app.Activity
import com.sugarpeanut.thejukebox.Models.addSong
import okhttp3.*
import java.io.IOException


class searchAdapter(val searchResult: searchResult): RecyclerView.Adapter<CustomViewHolder_search>(){

    val songTitles = listOf<String>("First song title","Second song tilte", "Third song title")

    // number of rows
    override fun getItemCount(): Int {
        return searchResult.tracks.items.count()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder_search {
        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRo = layoutInflater.inflate(R.layout.songsearchrow,p0,false)
        return CustomViewHolder_search(cellForRo)
    }

    override fun onBindViewHolder(p0: CustomViewHolder_search, p1: Int) {
        val song = searchResult.tracks.items.get(p1)
        p0.view.textview_songname.text = song.name
        p0.view.textView_songartist.text = song.artists[0].name
        p0.view.textView_songduration.text = song.duration_ms.toString()
        p0.view.textView_songlink.text = song.href
        p0.view.textView_songId.text = song.id
        p0.view.textView_songAlbumCoverLink.text = song.album.images[2].url
        val albumcover = p0.view.imageView_albumcover
        Picasso.get().load(song.album.images[2].url).into(albumcover)
    }
}

class CustomViewHolder_search(val view:View): RecyclerView.ViewHolder(view){
    val gson = Gson()

    init {
        view.setOnClickListener {

            val song = Song(view.textView_songId.text.toString(),view.textview_songname.text.toString(),
                view.textView_songlink.text.toString(), view.textView_songAlbumCoverLink.text.toString(),view.textView_songartist.text.toString(),
               10000)

            val songJson = gson.toJson(song)

            addNewSong(songJson)

        }
    }

    fun addNewSong(song:String){
        println("Attempting to add a song to the playlist")

        val MEDIA_TYPE = MediaType.parse("application/json")

        val url = "http://10.0.2.2:8080/playlist/addsong"

        val client = OkHttpClient()



        val songToAdd = addSong(0,song)


        val body = RequestBody.create(MEDIA_TYPE, gson.toJson(songToAdd))

        val request = Request.Builder()
            .url(url)
            .post(body)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                (view.context as Activity).finish()
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Faild to execute" + e.message)
            }
        })
    }
}