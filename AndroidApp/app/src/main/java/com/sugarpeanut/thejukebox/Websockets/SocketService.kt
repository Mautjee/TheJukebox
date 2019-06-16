package com.sugarpeanut.thejukebox.Websockets

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.sugarpeanut.thejukebox.MainActivity
import com.sugarpeanut.thejukebox.Models.Playlist
import com.sugarpeanut.thejukebox.Models.Song
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class SocketService(val activity: MainActivity) : WebSocketListener(){

    private val gson = Gson()

    private val TAG = "SocketService"

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.i(TAG,"Message recievd from Server")
        Log.i(TAG,text)
        handelMessageFromServer(text)

    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("Connection is made")

       activity.runOnUiThread {
           run{
                Toast.makeText(activity,"Connection Established",Toast.LENGTH_LONG).show()
              // webSocket.send("Hello server")
           }
       }

    }

    fun handelMessageFromServer(message:String){

        val sharedMessage = gson.fromJson(message,SharedMessageWebsocket::class.java)

        when(sharedMessage.Action){
            WebsocketActions.UpdatedPlaylist -> updatePlayist(sharedMessage.Message)
            }
        }

    fun updatePlayist(newPlaylist:String) {


        activity.runOnUiThread {
            val playlist = gson.fromJson(newPlaylist,Playlist::class.java)
            activity.adapter.changePlalist(playlist)
        }
    }

}


