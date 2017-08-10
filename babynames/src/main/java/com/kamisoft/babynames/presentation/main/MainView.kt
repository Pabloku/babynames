package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.kamisoft.babynames.domain.model.Gender

interface MainView : MvpView {

    fun initViews()

    fun openFinMatchesActivity()

    fun openParentNamesActivity(requestForResult: Boolean = false)

    fun openNamesListActivity(gender: Gender)

    fun openContactActivity()

    fun showNewVersionAvailable()

    fun showNewRequiredVersionAvailable()

    fun openPlayStoreLink()

    fun loadAds()

    fun close()
}