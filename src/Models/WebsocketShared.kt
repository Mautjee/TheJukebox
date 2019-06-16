package Models

data class SharedMessageWebsocket(val Action:WebsocketActions,val User:String,val Message:String)

enum class WebsocketActions(){
    UpdatedPlaylist,
    NewLike,
    NewSong,
    NewUser
}