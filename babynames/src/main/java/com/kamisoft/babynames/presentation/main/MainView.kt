package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.kamisoft.babynames.data.datasource.NamesDataSource

interface MainView : MvpView {

    fun showChooseGenderView()

    fun showWhoChooseFirstView()

    fun showFirstChooseNameView(gender: NamesDataSource.Gender)

    fun showWhoChooseSecondView()

    fun showSecondChooseNameView(gender: NamesDataSource.Gender)

    fun showMatchesView() //TODO need a param at least
}