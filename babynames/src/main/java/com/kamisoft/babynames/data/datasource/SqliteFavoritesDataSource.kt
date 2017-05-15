package com.kamisoft.babynames.data.datasource

import com.kamisoft.babynames.commons.deleteWhere
import com.kamisoft.babynames.commons.parseOpt
import com.kamisoft.babynames.commons.toVarargArray
import com.kamisoft.babynames.data.db.BabyNameDbHelper
import com.kamisoft.babynames.data.db.DbDataMapper
import com.kamisoft.babynames.data.db.FavoriteTable
import com.kamisoft.babynames.data.entity.SqliteFavorite
import com.kamisoft.babynames.domain.model.Favorite
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class SqliteFavoritesDataSource(val babyNameDbHelper: BabyNameDbHelper = BabyNameDbHelper.instance,
                                val dataMapper: DbDataMapper = DbDataMapper()) : FavoritesDataSource {

    override fun existFavorite(favorite: Favorite) : Boolean {
        return babyNameDbHelper.use {
            val where = "${FavoriteTable.BABY_NAME} = ?"
            val sqliteFavorite = select(FavoriteTable.NAME).whereSimple(where, favorite.babyName)
                    .parseOpt { SqliteFavorite(HashMap(it)) }
            sqliteFavorite != null
        }
    }

    override fun saveFavorite(favorite: Favorite) = babyNameDbHelper.use {
        with(dataMapper.convertFromDomain(favorite)) {
            insert(FavoriteTable.NAME, *map.toVarargArray())
        }
    }

    override fun deleteFavorite(favorite: Favorite) = babyNameDbHelper.use {
        val where = "${FavoriteTable.BABY_NAME} = '${favorite.babyName}'"
        deleteWhere(FavoriteTable.NAME, where)
    }

}