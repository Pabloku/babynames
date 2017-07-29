package com.kamisoft.babynames.presentation.main.BabyNamesSearchView

import com.hannesdorfmann.mosby3.mvp.MvpView

interface BabyNamesSearchViewView : MvpView {

    fun initSearchView()

    fun doSearch(query: String, searchCallback: (String) -> Unit)

    fun clearFocus()

    fun clearSearchText()
}