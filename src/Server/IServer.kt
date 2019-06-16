package Server

import Models.Playlist
import Models.Song
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession

interface IServer {
    suspend fun addSong(sessionId:String,song: Song)
    suspend fun memberJoin(member:String,socket: WebSocketSession)
    suspend fun memberLeft(member: String, socket: WebSocketSession)
    fun getFullPlaylist(): Playlist
    suspend fun List<WebSocketSession>.send(frame: Frame)
    suspend fun likeASong(sessionId:String,songId: String)
}