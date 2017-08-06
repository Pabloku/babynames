package com.kamisoft.babynames.domain.model

import com.kamisoft.babynames.presentation.model.BabyNameLikable

data class SummaryResult(val gender: Gender = Gender.MALE,
                         val parent1Name: String = "",
                         val parent1NamesChosen: List<BabyNameLikable>? = null,
                         val parent2Name: String = "",
                         val parent2NamesChosen: List<BabyNameLikable>? = null)
