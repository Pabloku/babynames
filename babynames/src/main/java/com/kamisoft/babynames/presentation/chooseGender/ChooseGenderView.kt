package com.kamisoft.babynames.presentation.chooseGender

import com.hannesdorfmann.mosby3.mvp.MvpView

interface ChooseGenderView : MvpView {

    fun boyGenderSelected()

    fun girlGenderSelected()
}