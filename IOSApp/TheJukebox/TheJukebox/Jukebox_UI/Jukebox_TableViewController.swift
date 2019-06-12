//
//  Jukebox_TableViewController.swift
//  TheJukebox
//
//  Created by mauro on 11/06/2019.
//  Copyright © 2019 nl.fhict. All rights reserved.
//

import UIKit
import Starscream

class Jukebox_TableViewController: UITableViewController {

    var songs = [Song]()
    var albumCover = UIImage()
    var socket = WebSocket(url: URL(string: "ws://127.0.0.1:8099/ws")!)

    override func viewDidLoad() {
        super.viewDidLoad()

        getSongs();
        socket.delegate = self
        
        socket.connect()
        
        
        
        print("has tryd to connect")
    }

    
    // MARK: - Table view data source

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 105
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return songs.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
       let cellIdentifyer =  "JukeboxCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifyer, for: indexPath) as? Jukebox_TableViewCell  else {
            fatalError("The dequeued cell is not an instance of MealTableViewCell.")
        }
        // Fetches the appropriate meal for the data source layout.
        let song = songs[indexPath.row]
        
        cell.songTitle.text = song.name
        
        cell.songArtist.text = song.artist
        cell.songDuration.text = "\(song.durationInSec)"
        let url = URL(string: song.image!)
        getData(from: url!) { data, response, error in
            guard let data = data, error == nil else { return }
            print(response?.suggestedFilename ?? url!.lastPathComponent)
            print("Download Finished")
            DispatchQueue.main.async() {
                cell.albumCover.image = UIImage(data: data)!
            }
        }
        

        return cell
    }
    
    func getData(from url: URL, completion: @escaping (Data?, URLResponse?, Error?) -> ()) {
        URLSession.shared.dataTask(with: url, completionHandler: completion).resume()
    }
    
    func getAlbumCover(from url:URL){
        print("Download Started")
        getData(from: url) { data, response, error in
            guard let data = data, error == nil else { return }
            print(response?.suggestedFilename ?? url.lastPathComponent)
            print("Download Finished")
            DispatchQueue.main.async() {
                self.albumCover = UIImage(data: data)!
            }
        }
    }

    func getSongs(){
        let url = URL(string:"http://127.0.0.1:8099/playlist/getall")
        
        URLSession.shared.dataTask(with: url!) { (data, response, error) in
            
            if error != nil{
                print("\(error!.localizedDescription)")
            }
            
            guard let retreivedData = data else{
                return
            }
            do{
                let songData =  try
                    JSONDecoder().decode(Playlist.self, from: retreivedData)
                
                DispatchQueue.main.async {
                    self.songs = songData.songs
                    self.tableView.reloadData()
                }
                
                
            }catch let err{
                print("\(err)")
            }
            
            
            }.resume()
    }
}
// MARK: - WebSocketDelegate
extension Jukebox_TableViewController : WebSocketDelegate {
    func websocketDidReceiveData(socket: WebSocketClient, data: Data) {
        
    }
    
    func websocketDidConnect(socket: WebSocketClient) {
        
        print("did connect")
    }
    
    func websocketDidDisconnect(socket: WebSocketClient, error: Error?) {
        socket.disconnect()
    }
    
    func websocketDidReceiveMessage(socket: WebSocketClient, text: String) {
        handelMessageFromServer(Message: text)
        
    }
    
    func handelMessageFromServer(Message:String){
        let retreivedData = Message.data(using: .utf8)!
        do{
            let websocketmessage =  try
                JSONDecoder().decode(sharedmessagewebsocket.self, from: retreivedData)
            switch websocketmessage.Action {
            case WebsocketActions.UpdatedPlaylist:
                let playlistData = websocketmessage.Message!.data(using: .utf8)!
                let playlist = try
                    JSONDecoder().decode(Playlist.self,from: playlistData)
                DispatchQueue.main.async {
                    self.songs = playlist.songs
                    self.tableView.reloadData ()
                }
            default:
                print("No corrct Action")
            }
            
            
            
        }catch let err{
            print("\(err)")
        }
    }
}

