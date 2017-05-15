package com.kamisoft.babynames.data.entity

import java.util.*

class SqliteFavorite(val map: MutableMap<String, Any?>) {
    var _id: Long by map
    var parent: String by map
    var gender: Int by map
    var babyName: String by map

    constructor(parent: String, gender: Int, babyName: String)
            : this(HashMap()) {
        this.parent = parent
        this.gender = gender
        this.babyName = babyName
    }
}