package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.commons.shared_preferences.PreferencesManager
import com.kamisoft.babynames.domain.model.Gender

class MainPresenter(private val preferencesManager: PreferencesManager) : MvpBasePresenter<MainView>() {

    fun start() {
        view?.initViews()
        if (!areParentNamesSet()) {
            view?.openParentNamesActivity()
            view?.close()
        }
    }

    fun onGoClicked() = view?.openFinMatchesActivity()

    fun onDrawerItemDadManClicked() = view?.openParentNamesActivity()

    fun onDrawerItemBoyNamesListClicked() = view?.openNamesListActivity(Gender.MALE)

    fun onDrawerItemGirlNameListClicked() = view?.openNamesListActivity(Gender.FEMALE)

    fun onDrawerItemContactClicked() = view?.openContactActivity()

    private fun areParentNamesSet() = preferencesManager.getParentNamesSetDatetime() != 0L
}
