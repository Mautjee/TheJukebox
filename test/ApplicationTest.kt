package TheJukebox

import Models.Playlist
import Models.Song
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import freemarker.cache.*
import io.ktor.freemarker.*
import io.ktor.websocket.*
import io.ktor.http.cio.websocket.*
import java.time.*
import io.ktor.auth.*
import io.ktor.client.features.auth.basic.*
import io.ktor.gson.*
import io.ktor.features.*
import java.io.*
import java.util.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.network.util.*
import kotlin.coroutines.*
import kotlinx.coroutines.*
import kotlinx.coroutines.io.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.jetty.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication() {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Welcome to the Jukebox API", response.content)
            }
        }
    }
    @Test
    fun getplaylist(){
//        val song = Song()
        withTestApplication {
            with(handleRequest(HttpMethod.Get, "/playlist/addsong")) {

            assertEquals("Song has been added", response.content)
        } }
    }
}
