package com.kamisoft.babynames.data.datasource

import com.kamisoft.babynames.commons.extensions.deleteWhere
import com.kamisoft.babynames.commons.extensions.parseList
import com.kamisoft.babynames.commons.extensions.parseOpt
import com.kamisoft.babynames.commons.extensions.toVarargArray
import com.kamisoft.babynames.data.db.BabyNameDbHelper
import com.kamisoft.babynames.data.db.DbDataMapper
import com.kamisoft.babynames.data.db.FavoriteTable
import com.kamisoft.babynames.data.entity.SqliteFavorite
import com.kamisoft.babynames.domain.model.Favorite
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class SqliteFavoritesDataSource(val babyNameDbHelper: BabyNameDbHelper = BabyNameDbHelper.instance,
                                val dataMapper: DbDataMapper = DbDataMapper()) : FavoritesDataSource {

    override fun isFavorite(parent: String, gender: NamesDataSource.Gender, name: String): Boolean {
        return babyNameDbHelper.use {
            val where = "${FavoriteTable.PARENT} = ? AND ${FavoriteTable.GENDER} = ? AND ${FavoriteTable.BABY_NAME} = ?"
            val sqliteFavorite = select(FavoriteTable.NAME)
                    .whereSimple(where, parent, gender.ordinal.toString(), name)
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
        val where = "${FavoriteTable.PARENT} = '${favorite.parent}' AND ${FavoriteTable.BABY_NAME} = '${favorite.babyName}'"
        deleteWhere(FavoriteTable.NAME, where)
    }

    override fun getFavorites(parent: String, gender: NamesDataSource.Gender): List<String> {
        return babyNameDbHelper.use {
            val where = "${FavoriteTable.PARENT} = ? AND ${FavoriteTable.GENDER} = ?"
            select(FavoriteTable.NAME)
                    .whereSimple(where, parent, gender.ordinal.toString())
                    .parseList { SqliteFavorite(HashMap(it)).babyName }
        }
    }

}