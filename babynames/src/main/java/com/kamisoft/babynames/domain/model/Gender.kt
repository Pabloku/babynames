package com.kamisoft.babynames.domain.model

enum class Gender(val value: String) {
    FEMALE("female"), MALE("male");

    override fun toString(): String {
        return value
    }
}