package Models

class Song(val songId: String,val name:String,val songUri:String,val image: Image,
           val artist:String,val durationInSec:Int){

    private var songLikes:Int
    init {
        songLikes = 0
    }
    fun likeASong(){
        songLikes++
    }
    fun dislikeSong(){
        songLikes--
    }
    fun getSongLikes():Int{
        return songLikes
    }
}