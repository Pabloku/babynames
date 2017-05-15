package com.kamisoft.babynames.data.datasource

class FavoritesDataFactory {

    fun create(): FavoritesDataSource = SqliteFavoritesDataSource()

}