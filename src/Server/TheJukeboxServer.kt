package Server

import Models.Playlist
import Models.SharedMessageWebsocket
import Models.Song
import Models.WebsocketActions
import com.google.gson.Gson
import io.ktor.http.cio.websocket.*

import kotlinx.coroutines.channels.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.*

class TheJukeboxServer:IServer{

    //playlist
    //private val playlists = ConcurrentHashMap<Int,MutableList<Song>>()

    //for a unic name
    val userCounter = AtomicInteger()

    //Session lists
    val memberNames = ConcurrentHashMap<String,String>()
    val members = ConcurrentHashMap<String,MutableList<WebSocketSession>>()

    //Playlist
    val playlist = Playlist()

    val gson =Gson()

    override suspend fun addSong(sessionId:String, song: Song){
        playlist.addSong(song)
        //if (sessionId != ""){

            val sharedmessage = SharedMessageWebsocket(WebsocketActions.UpdatedPlaylist,"232323232", gson.toJson(getFullPlaylist(),Playlist::class.java))
            val message = gson.toJson(sharedmessage)
            this.broadcast(message)
            println("Song is added")
        //}
    }


    override suspend fun memberJoin(member:String, socket:WebSocketSession){

        val name = memberNames.computeIfAbsent(member){"user${userCounter.incrementAndGet()}"}

        val list = members.computeIfAbsent(member){ CopyOnWriteArrayList<WebSocketSession>() }
        list.add(socket)

        if (list.size == 1) {
            broadcast("server", "Member joined: $name.")
        }

        socket.send(Frame.Text("new member"))
    }

    private suspend fun broadcast(message: String) {
        members.values.forEach { socket ->
            socket.send(Frame.Text(message))
        }
    }
    private suspend fun broadcast(sender: String, message: String) {
        val name = memberNames[sender] ?: sender
        broadcast("[$name] $message")
    }

    override suspend fun likeASong(sessionId:String, songId: String){
        playlist.likeASong(songId)
    }
    override suspend fun memberLeft(member: String, socket: WebSocketSession) {
        // Removes the socket connection for this member
        val connections = members[member]
        connections?.remove(socket)

        // If no more sockets are connected for this member, let's remove it from the server
        // and notify the rest of the users about this event.
        if (connections != null && connections.isEmpty()) {
            val name = memberNames.remove(member) ?: member
            broadcast("server", "Member left: $name.")
            println("removed from session")
        }
    }

    override fun getFullPlaylist(): Playlist{
        if( playlist.getplaylist().isEmpty()){
            val song = Song("1","No numbers in the playlist","11", "https://previews.123rf.com/images/leventegyori/leventegyori1510/leventegyori151000012/47713326-geschilderd-x-teken-op-wit-wordt-ge%C3%AFsoleerd.jpg","No Songs",1)

            val tempplaylist = Playlist()
            tempplaylist.addSong(song)

            return tempplaylist
        }else{
            return playlist
        }

    }
    override suspend fun List<WebSocketSession>.send(frame: Frame) {
        forEach {
            try {
                it.send(frame.copy())
            } catch (t: Throwable) {
                try {
                    it.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, ""))
                } catch (ignore: ClosedSendChannelException) {
                    // at some point it will get closed
                }
            }
        }
    }
}