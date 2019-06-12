//
//  SharedWebsocketMessage.swift
//  TheJukebox
//
//  Created by mauro on 12/06/2019.
//  Copyright © 2019 nl.fhict. All rights reserved.
//

class sharedmessagewebsocket : Decodable{
    var Action:WebsocketActions
    var User:String?
    var Message:String?

}

enum WebsocketActions:Decodable{
    
    case UpdatedPlaylist
    case NewLike
    case NewSong
    case NewUser
    case Other
    
    init(from decoder: Decoder) throws {
        let label = try decoder.singleValueContainer().decode(String.self)
        switch label {
        case "UpdatedPlaylist": self = .UpdatedPlaylist
        case "NewLike": self = .NewLike
        case "NewSong":self = .NewSong
        case "NewUser":self = .NewUser
        default:
            self = .Other
        }
    }
}
