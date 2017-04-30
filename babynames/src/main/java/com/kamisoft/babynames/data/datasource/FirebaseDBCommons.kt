package com.kamisoft.babynames.data.datasource

class FirebaseDBCommons {

    enum class Node(val value: String) {
        NAMES("names"), GENDERS("genders"), FEMALE("female"), MALE("male");

        override fun toString(): String {
            return value
        }
    }
}