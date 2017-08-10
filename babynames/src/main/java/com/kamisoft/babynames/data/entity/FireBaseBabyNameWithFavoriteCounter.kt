package com.kamisoft.babynames.data.entity

class FireBaseBabyNameWithFavoriteCounter() {
    lateinit var gender: String
    lateinit var origin: String
    lateinit var meaning: String
    var favoriteCount: Int = 0
}