package com.sugarpeanut.thejukebox.Mocks

import java.io.File

object searchMocks{

    val search_Drake = readFileDirectlyAsText("com/sugarpeanut/thejukebox/Mocks/search_MacMiller")
    val search_FrankOcean = readFileDirectlyAsText("com/sugarpeanut/thejukebox/Mocks/search_FrankOcean")
    val search_MacMiller = readFileDirectlyAsText("com/sugarpeanut/thejukebox/Mocks/search_Drake")
    val search_JetFuel = readFileDirectlyAsText("com/sugarpeanut/thejukebox/Mocks/search_JetFuel")


    fun readFileDirectlyAsText(fileName: String): String
            = File(fileName).readText(Charsets.UTF_8)

    fun getJson(searchWord:String):String{

        when(searchWord){
            "drake" -> return search_Drake
            "frank ocean" -> return search_FrankOcean
            "mac miller" -> return search_MacMiller
            "jet fuel" -> return search_JetFuel
            else ->""
        }
        return ""
    }
}