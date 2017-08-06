package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.kamisoft.babynames.data.datasource.NamesDataSource

interface MainView : MvpView {

    fun initViews()

    fun openFinMatchesActivity()

    fun openParentNamesActivity()

    fun openNamesListActivity(gender: NamesDataSource.Gender)

    fun openContactActivity()

    fun close()
}