

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.WebSockets
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import io.ktor.util.hex
import io.ktor.gson.*
import io.ktor.jackson.*
import kotlinx.css.header


val spotifyOAuthProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = "Spotify",
    authorizeUrl = "https://accounts.spotify.com/authorize",
    accessTokenUrl = "https://accounts.spotify.com/api/token",
    requestMethod = HttpMethod.Post,

    clientId = "0b2b8eeca00344bb81cc5fd1f46a36eb",
    clientSecret = "24980ae0558a406a95e33a6904b4af32",
    defaultScopes = listOf("user-read-private","user-read-email")
)

class MySession(val userId: String)

fun main(args: Array<String>) {
    embeddedServer(Netty,host = "www.thejukebox.com", port = 8080) {
        install(WebSockets)
        install(Sessions) {
            cookie<MySession>("oauthSampleSessionId") {
                val secretSignKey = hex("000102030405060708090a0b0c0d0e0f") // @TODO: Remember to change this!
                transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
            }
        }
        install(Authentication) {
            oauth("spotify-oauth") {
                client = HttpClient(CIO)
                providerLookup = { spotifyOAuthProvider }
                urlProvider = {
                    redirectUrl("/login")
                }
            }
        }
        routing {
            get("/") {
                val session = call.sessions.get<MySession>()
                call.respondText("HI ${session?.userId}")
            }
            authenticate("spotify-oauth") {
                route("/login") {
                    handle {
                        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                            ?: error("No principal")

                        val json = HttpClient(CIO).get<String>("https://api.spotify.com/v1/me") {
                            header("Authorization", "Bearer ${principal.accessToken}")
                        }

                        val data = ObjectMapper().readValue<Map<String, Any?>>(json)
                        val id = data["id"] as String?

                        if (id != null) {
                            call.sessions.set(MySession(id))
                        }
                        call.respondRedirect("/")
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