package com.kamisoft.babynames.domain.repository

import com.kamisoft.babynames.data.datasource.NamesDataSource

interface NamesRepository {
    fun getAllNamesByGenre(genre: NamesDataSource.Genre): List<String>
}