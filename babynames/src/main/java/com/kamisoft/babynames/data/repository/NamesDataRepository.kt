package com.kamisoft.babynames.data.repository

import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.repository.NamesRepository

class NamesDataRepository(val namesDataFactory: NamesDataFactory) : NamesRepository {

    override fun getAllNamesByGenre(genre: NamesDataSource.Genre): List<String> {
        val namesDataSource = namesDataFactory.create()
        return namesDataSource.getNamesList(genre)
    }
}