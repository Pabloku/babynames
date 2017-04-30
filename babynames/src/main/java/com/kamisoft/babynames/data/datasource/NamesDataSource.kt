package com.kamisoft.babynames.data.datasource

interface NamesDataSource {

    //TODO [Paloga] Think about the best place for this enum https://kotlinlang.org/docs/reference/delegated-properties.html
    enum class Gender(val value: String) {
        FEMALE("female"), MALE("male");

        override fun toString(): String {
            return value
        }
    }

    fun getNamesList(gender: Gender): List<String>
}