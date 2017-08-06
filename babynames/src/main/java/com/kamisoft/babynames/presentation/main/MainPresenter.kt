package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babyname.BuildConfig
import com.kamisoft.babynames.commons.shared_preferences.PreferencesManager
import com.kamisoft.babynames.domain.model.Gender

class MainPresenter(private val preferencesManager: PreferencesManager) : MvpBasePresenter<MainView>() {

    fun start() {
        val currentAppVersion = BuildConfig.VERSION_CODE
        view?.initViews()
        if (newAppVersionRequiredAvailable(currentAppVersion)) {
            view?.showNewRequiredVersionAvailable()
        } else {
            if (!areParentNamesSet()) {
                view?.openParentNamesActivity()
                view?.close()
            } else {
                if (newAppVersionAvailable(currentAppVersion)) {
                    view?.showNewVersionAvailable()
                    preferencesManager.setLastNewVersionCheckDate(System.currentTimeMillis())
                }
            }
        }
    }

    fun onGoClicked() = view?.openFinMatchesActivity()

    fun onDrawerItemDadManClicked() = view?.openParentNamesActivity()

    fun onDrawerItemBoyNamesListClicked() = view?.openNamesListActivity(Gender.MALE)

    fun onDrawerItemGirlNameListClicked() = view?.openNamesListActivity(Gender.FEMALE)

    fun onDrawerItemContactClicked() = view?.openContactActivity()

    private fun areParentNamesSet() = preferencesManager.getParentNamesSetDatetime() != 0L

    private fun newAppVersionRequiredAvailable(currentAppVersion: Int): Boolean {
        val appVersionRequired = preferencesManager.getAppVersionRequired()
        return appVersionRequired > currentAppVersion
    }

    private fun newAppVersionAvailable(currentAppVersion: Int): Boolean {
        val appLastVersion = preferencesManager.getAppLastVersion()
        val lastNewVersionCheckDate = preferencesManager.getLastNewVersionCheckDate()
        return isOneWeekOrMoreDifference(lastNewVersionCheckDate, System.currentTimeMillis()) && appLastVersion > currentAppVersion
    }

    private fun isOneWeekOrMoreDifference(dateTime1: Long, dateTime2: Long): Boolean {
        val oneWeekInMillis = 1000 * 60 * 60 * 24 * 7
        return dateTime2 - dateTime1 > oneWeekInMillis
    }
}
