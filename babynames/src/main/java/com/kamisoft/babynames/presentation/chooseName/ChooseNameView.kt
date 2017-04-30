package com.kamisoft.babynames.presentation.chooseName

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView
import com.kamisoft.babynames.domain.model.BabyName

interface ChooseNameView : MvpLceView<List<BabyName>> {

}