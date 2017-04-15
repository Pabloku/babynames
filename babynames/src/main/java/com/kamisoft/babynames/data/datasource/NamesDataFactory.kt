package com.kamisoft.babynames.data.datasource

class NamesDataFactory {

    fun create(): NamesDataSource = FirebaseNamesDataSource()

}