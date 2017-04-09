package com.kamisoft.babynames.data.repository.datasource

interface NamesDataSource {

    //TODO [Paloga] Think about the best place for this enum https://kotlinlang.org/docs/reference/delegated-properties.html
    enum class Genre(val genreName: String) {
        FEMALE("female"), MALE("male")
    }

    fun getNamesList(genre: Genre): List<String>
}