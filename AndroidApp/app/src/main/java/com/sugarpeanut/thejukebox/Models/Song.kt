package com.sugarpeanut.thejukebox.Models

class Song(val songId: String,val name:String,val songlink:String,val image: String,
           val artist:String,val durationInSec:Int){

    private var songLikes:Int
    init {
        songLikes = 0
    }
    fun setSongLikes(likes:Int){
        songLikes = likes
    }
    fun getSongLikes():Int{
        return songLikes
    }
}