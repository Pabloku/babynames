package com.kamisoft.babynames.commons.shared_preferences


interface PreferencesManager {

    fun getFirstUseInMillis(): Long

    fun saveFirstUse(firstUseInMillis: Long)

    fun getParent1(): String

    fun setParent1(parent: String)

    fun getParent1Name(): String

    fun setParent1Name(name: String)

    fun getParent2(): String

    fun setParent2(parent: String)

    fun getParent2Name(): String

    fun setParent2Name(name: String)

}