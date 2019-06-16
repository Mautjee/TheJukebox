package com.sugarpeanut.thejukebox

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.sugarpeanut.thejukebox.Models.Playlist
import kotlinx.android.synthetic.main.songmainrow.view.*



class mainAdapter():RecyclerView.Adapter<MainActivatyViewHolder>(){

    private lateinit var playlist:Playlist

    // number of rows
    override fun getItemCount(): Int {
        return playlist.songs.count()

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MainActivatyViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRo = layoutInflater.inflate(R.layout.songmainrow,p0,false)
        return MainActivatyViewHolder(cellForRo)
    }

    override fun onBindViewHolder(p0: MainActivatyViewHolder, p1: Int) {
        val song = playlist.songs.get(p1)


        p0.view.textview_songname.text = song.name
        p0.view.textView_songartist.text = song.artist
        p0.view.textView_songlikes.text = song.getSongLikes().toString()
        p0.view.textView_songduration.text = song.durationInSec.toString()

        val albumcover = p0.view.imageView_albumcover
       Picasso.get().load(song.image).into(albumcover)


    }

    fun changePlalist(playlist: Playlist){
        this.playlist = playlist
        notifyDataSetChanged()
    }
}

class MainActivatyViewHolder(val view:View): RecyclerView.ViewHolder(view){

}