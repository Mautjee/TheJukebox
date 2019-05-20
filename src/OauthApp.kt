

import Models.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.GsonBuilder
import io.ktor.websocket.WebSockets
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.*
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import io.ktor.util.hex
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.ClosedReceiveChannelException


var accesTokenSpotify : OAuthAccessTokenResponse.OAuth2? = null

val playlist = Playlist()

val spotifyOAuthProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = "Spotify",
    authorizeUrl = "https://accounts.spotify.com/authorize",
    accessTokenUrl = "https://accounts.spotify.com/api/token",
    requestMethod = HttpMethod.Post,

    clientId = "0b2b8eeca00344bb81cc5fd1f46a36eb",
    clientSecret = "24980ae0558a406a95e33a6904b4af32",
    defaultScopes = listOf("user-read-private","user-read-email","streaming","playlist-modify-public")
)


val gson = GsonBuilder().create()
class MySession(val userId: String)

fun main(args: Array<String>) {
    embeddedServer(Netty,host = "www.thejukebox.com", port = 8080) {
        install(WebSockets){

        }


        install(Sessions) {
            cookie<MySession>("TheJukeboxSes1") {
                val secretSignKey = hex("000102030405060708090a0b0c0d0e0f")
                transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
            }
        }
        install(ContentNegotiation){
            gson{
                setPrettyPrinting()
            }
        }

        install(Authentication) {
            oauth("spotify-oauth") {
                client = HttpClient(CIO)
                providerLookup = { spotifyOAuthProvider }
                urlProvider = {
                    redirectUrl("/user/login/spotify")
                }
            }
        }
        routing {
            get("/") {

                
            }

            route("/user"){
                post("/login") {
                }
            }
            authenticate("spotify-oauth") {
                route("/user/login/spotify") {
                    handle {
                        accesTokenSpotify = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                            ?: error("No principal")

                        call.respondRedirect("/")
                    }
                }
            }



            route("/search"){

                get("/{searchWord}") {
                    val searchvar = call.parameters["searchWord"]!!.toString()
                    val json =
                        HttpClient(CIO).get<String>("https://api.spotify.com/v1/search?q='$searchvar'&type=track&market=NL&limit=10") {
                            header("Authorization", "Bearer ${accesTokenSpotify?.accessToken}")
                        }


                    val searchResult = gson.fromJson(json, searchResult::class.java)



                    //val song = Song(1,searchResult.tracks.items[0].name)

                    call.respondText(json)
                }
            }
            route("/playlist"){
                get("/getall"){

                    call.respond(playlist.getplaylist())
                }
                post("/addsong") {

                    val obj: addSong = call.receive()


                   val result =  HttpClient(CIO).get<String>(obj.songLink) {
                        header("Authorization", "Bearer ${accesTokenSpotify?.accessToken}")
                    }
                    val songresult = gson.fromJson(result,songRaw::class.java)

                    val durationINSeconds = songresult.duration_ms / 1000

                    val song = Song(songresult.id,songresult.name,songresult.uri,songresult.album.images[0],songresult.artists[0].name,durationINSeconds)

                    playlist.addSong(song)

                    call.respondText("Song has been added")
                }
                post("/removesong"){

                    val postParameters: Parameters = call.receiveParameters()

                    call.respondText("remove a song form the playlist")
                }
                post("/likesong"){
                    val obj: likeSong = call.receive()

                    playlist.likeASong(obj.songId)

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
                webSocket("/echo") {
                    println("onConnect")
                    try {
                        for (frame in incoming) {
                            val text = (frame as Frame.Text).readText()
                            println("onMessage")
                            println(text)
                            outgoing.send(Frame.Text(text))
                        }
                    } catch (e: ClosedReceiveChannelException) {
                        println("onClose ${closeReason.await()}")
                    } catch (e: Throwable) {
                        println("onError ${closeReason.await()}")
                        e.printStackTrace()
                    }
                }
            }
        }
    }.start(wait = true)
}

private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host()!! + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}




