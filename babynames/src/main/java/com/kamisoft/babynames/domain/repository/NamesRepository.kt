package com.kamisoft.babynames.domain.repository

import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.data.entity.FireBaseBabyName

interface NamesRepository {
    fun getAllNamesByGender(gender: NamesDataSource.Gender): List<FireBaseBabyName>
}