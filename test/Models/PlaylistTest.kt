package Models

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertFails

internal class PlaylistTest {



    @Test
    fun addSong() {
        //Arange
        val playlist = Playlist()
        val song = Song("1","No numbers in the playlist","11", "test.url","No Songs",1)

        //Act
        playlist.addSong(song)

        //Assert
        assertEquals(1,playlist.getplaylist().count())
    }
    @Test
    fun addSong_CheckAddedSong() {
        //Arange
        val playlist = Playlist()
        val song = Song("1","No numbers in the playlist","11", "test.url","No Songs",1)

        //Act
        playlist.addSong(song)

        val addedSong = playlist.getplaylist().get(0)
        //Assert
        assertEquals(song,addedSong)
    }


    @Test
    fun likeASong() {
        //Arange
        val playlist = Playlist()
        playlist.addSong(Song("1","No numbers in the playlist","11", "test.url","No Songs",1))

        //Act
        playlist.likeASong("1")

        //Assert
        val song = playlist.getSpecificSong("1")
        if (song != null) {
            assertEquals(1,song.getSongLikes())
        }
    }

    @Test
    fun getSpecificSong(){
        //Arange
        val playlist = Playlist()
        val expectsong = Song("1","No numbers in the playlist","11", "test.url","No Songs",1)
        playlist.addSong(expectsong)

        //Act
        playlist.likeASong("1")

        //Assert
        val actual = playlist.getSpecificSong("1")
        if (actual != null) {
            assertEquals(actual,expectsong)

        }
    }
    @Test
    fun getSpecificSong_Fail(){

        //Arange
        val playlist = Playlist()
        val song1 = Song("1","No numbers in the playlist","11", "test.url","No Songs",1)
        val song2 = Song("2","No numbers in the playlist","11", "test.url","No Songs",1)
        playlist.addSong(song1)
        playlist.addSong(song2)

        //Act
        playlist.likeASong("1")

        //Assert
        val actual = playlist.getSpecificSong("2")
        if (actual != null) {
            assertNotEquals(song1,actual)

        }
    }
}