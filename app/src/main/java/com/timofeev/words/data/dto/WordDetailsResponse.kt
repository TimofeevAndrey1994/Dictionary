package com.timofeev.words.data.dto

class WordDetailsResponse : ArrayList<WordDetailsResponseItem>(), Response {
    override var resultCode: Int = 0
}
