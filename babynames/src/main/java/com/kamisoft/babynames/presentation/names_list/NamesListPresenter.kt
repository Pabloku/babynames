package com.kamisoft.babynames.presentation.names_list

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.tracking.TrackerConstants
import com.kamisoft.babynames.tracking.TrackerManager

class NamesListPresenter(private val getNamesUseCase: GetNameList,
                         private val trackerManager: TrackerManager) : MvpBasePresenter<NamesListView>() {

    fun start() {
        view?.initViews()
    }

    fun loadData(gender: Gender) {
        when (gender) {
            Gender.FEMALE -> trackGirlPage()
            Gender.MALE -> trackBoyPage()
        }
        val pullToRefresh = true
        view?.showLoading(!pullToRefresh)
        getNamesUseCase.getNames(gender, { namesLoaded(it) })
    }

    private fun namesLoaded(namesList: List<BabyName>) {
        if (namesList.isNotEmpty()) {
            view?.setData(namesList)
            view?.showContent()
        } else {
            val pullToRefresh = true
            view?.showError(Exception(), !pullToRefresh)
        }
    }

    private fun trackBoyPage() {
        trackerManager.sendScreen(TrackerConstants.Section.NAMES_LIST.value,
                TrackerConstants.Section.NamesList.BOY_NAMES.value)
    }

    private fun trackGirlPage() {
        trackerManager.sendScreen(TrackerConstants.Section.NAMES_LIST.value,
                TrackerConstants.Section.NamesList.GIRL_NAMES.value)
    }
}
