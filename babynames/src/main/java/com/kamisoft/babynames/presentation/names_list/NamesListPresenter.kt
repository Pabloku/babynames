package com.kamisoft.babynames.presentation.names_list

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.usecase.GetNameList

class NamesListPresenter(private val getNamesUseCase: GetNameList) : MvpBasePresenter<NamesListView>() {

    fun start() {
        view?.initViews()
    }

    fun loadData(gender: Gender) {
        val pullToRefresh = true
        view?.showLoading(!pullToRefresh)
        getNamesUseCase.getNames(gender, { namesLoaded(it) })
    }

    private fun namesLoaded(namesList: List<BabyName>) {
        view?.setData(namesList)
        view?.showContent()
    }
}
