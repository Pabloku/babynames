package com.kamisoft.babynames.presentation.names_list

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView
import com.kamisoft.babynames.domain.model.BabyName

interface NamesListView : MvpLceView<List<BabyName>> {

    fun initViews()

}