package com.kamisoft.babynames.data.datasource

import com.kamisoft.babynames.data.entity.FireBaseBabyName
import com.kamisoft.babynames.domain.model.Gender

interface NamesDataSource {
    fun getNamesList(gender: Gender): List<FireBaseBabyName>

    fun increaseNameLikedCounter(gender: Gender, name: String)

    fun decreaseNameLikedCounter(gender: Gender, name: String)
}