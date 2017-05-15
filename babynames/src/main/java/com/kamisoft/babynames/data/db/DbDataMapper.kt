package com.kamisoft.babynames.data.db

import com.kamisoft.babynames.data.entity.SqliteFavorite
import com.kamisoft.babynames.domain.model.Favorite

class DbDataMapper {

    fun convertFromDomain(favorite: Favorite) = with(favorite) {
        SqliteFavorite(parent, gender, babyName)
    }

    fun convertToDomain(sqliteFavorite: SqliteFavorite) = with(sqliteFavorite) {
        Favorite(parent, gender, babyName)
    }

}