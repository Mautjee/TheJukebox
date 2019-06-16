//
//  Jukebox_TableViewCell.swift
//  TheJukebox
//
//  Created by mauro on 11/06/2019.
//  Copyright © 2019 nl.fhict. All rights reserved.
//
import Foundation
import UIKit

class Jukebox_TableViewCell: UITableViewCell {

    //MARK: Ui Elements
    
    @IBOutlet weak var albumCover: UIImageView!
    @IBOutlet weak var songTitle: UILabel!
    @IBOutlet weak var songArtist: UILabel!
    @IBOutlet weak var songDuration: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
