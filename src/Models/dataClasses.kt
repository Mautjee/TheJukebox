package Models

// These dataclasses are for the spotify API
data class searchResult(val tracks: Tracks)

data class Tracks(val items: List<songRaw>, val limit: Int)

// These dataclasses are for this API

data class addSong(val playlistId:Int, val songLink:String)

data class likeSong(val songId:String)