package com.sugarpeanut.thejukebox.Models

import android.app.Activity
import org.json.JSONArray



object SpotifyConnect {
    // static variable single_instance of type Singleton
    private var single_instance: SpotifyConnect? = null

    var jsonArray = JSONArray()

    // variable of type String
    private var token: String? = null
    var done = false

    // private constructor restricted to this class itself
    private fun SpotifyConnect() {
        token = ""
    }

    // static method to create instance of Singleton class
    fun getInstance(): SpotifyConnect {
        if (single_instance == null)
            single_instance = SpotifyConnect

        return single_instance as SpotifyConnect
    }

    fun getToken(): String? {
        return token
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun start(activity: Activity) {

    }
}