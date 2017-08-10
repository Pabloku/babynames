package com.kamisoft.babynames.presentation.find_matches

import com.hannesdorfmann.mosby3.mvp.MvpView

interface FindMatchesView : MvpView {

    fun initViews()

    fun showChooseGenderView()

    fun showWhoChooseFirstView()

    fun showFirstChooseNameView()

    fun showSecondChooseNameView()

    fun showMatchesView()

    fun updateFavoriteCounter(favoriteCount: Int)

    fun loadAds()

    fun showAdInterstitial()
}