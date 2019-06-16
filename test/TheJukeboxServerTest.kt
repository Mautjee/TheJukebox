import Models.Playlist
import Models.Song
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.Asserter

internal class TheJukeboxServerTest {


    @Test
    fun addSong_CountListSize() {
        //Arange
        val playlist = Playlist()
        val song = Song("1","No numbers in the playlist","11", "test.url","No Songs",1)

        //Act
        playlist.addSong(song)

        //Assert
        assertEquals(1,playlist.getplaylist().count())
    }


}