package com.kamisoft.babynames.data.repository

import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.entity.FireBaseBabyName
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.repository.NamesRepository

class NamesDataRepository(val namesDataFactory: NamesDataFactory) : NamesRepository {

    override fun getAllNamesByGender(gender: Gender): List<FireBaseBabyName> {
        val namesDataSource = namesDataFactory.create()
        return namesDataSource.getNamesList(gender)
    }
}