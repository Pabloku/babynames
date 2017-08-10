package com.kamisoft.babynames.data.datasource

class FirebaseDBCommons {

    enum class Node(val value: String) {
        BABY_NAMES("babyNames"), FEMALE("female"), MALE("male"), FAVORITES("favorites");

        override fun toString(): String {
            return value
        }
    }
}