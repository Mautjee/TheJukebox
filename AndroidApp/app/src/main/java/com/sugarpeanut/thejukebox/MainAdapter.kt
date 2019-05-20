package com.sugarpeanut.thejukebox

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sugarpeanut.thejukebox.Models.searchResult
import kotlinx.android.synthetic.main.songsearchrow.view.*

class mainAdapter(val searchResult: searchResult):RecyclerView.Adapter<CustomViewHolder>(){

    val songTitles = listOf<String>("First song title","Second song tilte", "Third song title")

    // number of rows
    override fun getItemCount(): Int {
        return searchResult.tracks.items.count()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRo = layoutInflater.inflate(R.layout.songsearchrow,p0,false)
        return CustomViewHolder(cellForRo)
    }

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
        val song = searchResult.tracks.items.get(p1)
        p0.view.textview_songname.text = song.name
        p0.view.textView_songartist.text = song.artists[0].name
        p0.view.textView_songduration.text = song.duration_ms.toString()


    }
}

class CustomViewHolder(val view:View): RecyclerView.ViewHolder(view){

}