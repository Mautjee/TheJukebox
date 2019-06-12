//
//  Song.swift
//  TheJukebox
//
//  Created by mauro on 11/06/2019.
//  Copyright © 2019 nl.fhict. All rights reserved.
//

class Song :Decodable{
    var songId:String?
    var name:String?
    var image:String?
    var artist:String?
    var durationInSec:Int?
    var songlink:String?
    
    init?(songId:String,name: String, image:String, artist:String,durationInSec:Int,songlink:String) {
        
        // The name must not be empty
        guard !name.isEmpty else {
            return nil
        }
        
        // The rating must be between 0 and 5 inclusively
        guard !songId.isEmpty else {
            return nil
        }
        guard !artist.isEmpty else {
            return nil
        }

        guard !artist.isEmpty else {
            return nil
        }
        guard !songlink.isEmpty else {
            return nil
        }
        
        
        // Initialize stored properties.
        self.songId = songId
        self.name = name
        self.image = image
        self.artist = artist
        self.durationInSec = durationInSec
        self.songlink = songlink
    }
}
