package com.kamisoft.babynames.domain.repository

import com.kamisoft.babynames.data.entity.FireBaseBabyName
import com.kamisoft.babynames.domain.model.Gender

interface NamesRepository {
    fun getAllNamesByGender(gender: Gender): List<FireBaseBabyName>
}