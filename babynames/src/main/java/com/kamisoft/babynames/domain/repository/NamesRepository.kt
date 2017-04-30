package com.kamisoft.babynames.domain.repository

import com.kamisoft.babynames.data.datasource.NamesDataSource

interface NamesRepository {
    fun getAllNamesByGender(gender: NamesDataSource.Gender): List<String>
}