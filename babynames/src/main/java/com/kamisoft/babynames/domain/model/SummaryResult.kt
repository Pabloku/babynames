package com.kamisoft.babynames.domain.model

import com.kamisoft.babynames.data.datasource.NamesDataSource

data class SummaryResult (val gender: NamesDataSource.Gender = NamesDataSource.Gender.MALE,
                          val parent1: String? = null,
                          val parent1NamesChosen: List<BabyName>? = null,
                          val parent2: String? = null,
                          val parent2NamesChosen: List<BabyName>? = null)
