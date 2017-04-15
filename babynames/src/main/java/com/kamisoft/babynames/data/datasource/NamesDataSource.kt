package com.kamisoft.babynames.data.datasource

interface NamesDataSource {

    //TODO [Paloga] Think about the best place for this enum https://kotlinlang.org/docs/reference/delegated-properties.html
    enum class Genre(val value: String) {
        FEMALE("female"), MALE("male");

        override fun toString(): String {
            return value
        }
    }

    fun getNamesList(genre: Genre): List<String>
}