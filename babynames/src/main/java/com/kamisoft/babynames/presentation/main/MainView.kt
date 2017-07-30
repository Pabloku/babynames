package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.kamisoft.babynames.domain.model.Parent

interface MainView : MvpView {

    fun initViews()

    fun openFinMatchesActivity()

    fun openParentNamesActivity()

    fun openFavoritesActivity(parent: Parent)

    fun openMatchesActivity()

    fun openContactActivity()

    fun close()
}