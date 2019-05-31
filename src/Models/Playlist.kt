package Models

class Playlist(){

    private val playlistId = 0
    private val songs = mutableListOf<Song>()



    fun getplaylist():List<Song>{
        return songs
    }

    fun addSong(song:Song){
        //if(_songs.any { it.songId != song.songId }){
            songs.add(song)
        //}else{
          //  println("Song already in playlist")
        //}

    }

    fun likeASong(songId:String){

       val song = songs.find{ it.songId == songId }

        if (song != null) {
            song.likeASong()
        }else{
            println("Song does not exits")
        }
    }
    fun dislikeASong(songId:String){

        val song = songs.find{ it.songId == songId }

        if (song != null) {
            song.dislikeSong()
        }else{
            println("Song does not exits")
        }
    }


}