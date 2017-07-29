package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpView

interface MainView : MvpView {

    fun initViews()

    fun showChooseGenderView()

    fun showWhoChooseFirstView()

    fun showFirstChooseNameView()

    fun showSecondChooseNameView()

    fun showMatchesView()
}