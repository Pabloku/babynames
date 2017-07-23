package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpView

interface MainView : MvpView {

    fun showChooseGenderView()

    fun showWhoChooseFirstView()

    fun showFirstChooseNameView()

    fun showWhoChooseSecondView()

    fun showSecondChooseNameView()

    fun showMatchesView()
}