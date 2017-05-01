package com.kamisoft.babynames.domain.model

enum class Parent(val value: String) {
    DAD("dad"), MOM("mom");

    override fun toString(): String {
        return value
    }
}