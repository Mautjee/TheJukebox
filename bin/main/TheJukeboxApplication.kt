

import Models.*
import com.google.gson.GsonBuilder
import io.ktor.websocket.WebSockets
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.*
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import io.ktor.util.generateNonce
import io.ktor.util.hex
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.consumeEach


var accesTokenSpotify : OAuthAccessTokenResponse.OAuth2? = null


val gson = GsonBuilder().create()
class JukeSession(val userId: String)

val server = TheJukeboxServer()

fun main(args: Array<String>) {
    embeddedServer(Netty,host = "www.thejukebox.com", port = 8099) {
        install(WebSockets){

        }


        install(Sessions) {
            cookie<JukeSession>("TheJukeboxSes1") {
                val secretSignKey = hex("000102030405060708090a0b0c0d0e0f")
                transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
            }
        }
        install(ContentNegotiation){
            gson{
                setPrettyPrinting()
            }
        }
        intercept(ApplicationCallPipeline.Features) {
            if (call.sessions.get<JukeSession>() == null) {
                call.sessions.set(JukeSession(generateNonce()))
            }
        }
        routing {
            get("/") {

                
            }
            route("/playlist"){
                get("/getall"){
                    val playlist = server.getFullPlaylist()


                    call.respond(server.getFullPlaylist())
                }
                post("/addsong") {

                    val addSong: addSong = call.receive()
                    val session =  call.sessions.get<JukeSession>()
                    val song:Song = gson.fromJson(addSong.songjson,Song::class.java)
                    server.addSong(session!!.userId,song)

                    call.respondText("Song has been added")

                }
                post("/removesong"){

                    val postParameters: Parameters = call.receiveParameters()

                    call.respondText("remove a song form the playlist")
                }
                post("/likesong"){
                    val obj: likeSong = call.receive()

                    server.likeASong("",obj.songId)

                    call.respondText("Song is liked")
                }
                post("/dislikesong"){

                }
            }
            route("/juke"){
                post("/make"){

                }
                get("/get"){

                }
                delete("/delete"){

                }

            }
            routing {
                webSocket("/ws") {

                    println("onConnect")
                    val session = call.sessions.get<JukeSession>()

                    if (session == null) {
                        close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
                        return@webSocket
                    }

                    server.memberJoin(session.userId, this)

                    try {
                        incoming.consumeEach {frame ->

                            if (frame is Frame.Text){
                                reivedMessage(session.userId,frame.readText())

                        }

                        }
                    } finally {
                        //server.memberLeft(session.userId,this)
                    }

                }
            }
        }
    }.start(wait = true)


}
private suspend fun reivedMessage(userId:String,command:String){
    val message = gson.fromJson(command,SharedMessageWebsocket::class.java)

    when(message.Action){
        WebsocketActions.NewSong -> {
            val song = gson.fromJson(message.Message,Song::class.java)
           server.addSong(userId,song)
        }
    }

}
private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host()!! + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}




