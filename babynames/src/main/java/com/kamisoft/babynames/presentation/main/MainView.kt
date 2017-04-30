package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.kamisoft.babynames.data.datasource.NamesDataSource

interface MainView : MvpView {

    fun showChooseGenderView()

    fun showChooseNameView(gender: NamesDataSource.Gender)
}