package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.BuildConfig
import com.kamisoft.babynames.commons.shared_preferences.PreferencesManager
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.tracking.TrackerConstants
import com.kamisoft.babynames.tracking.TrackerManager

//TODO I'd be nice to have a BaseTrackeablePresenter or something like that
class MainPresenter(private val preferencesManager: PreferencesManager,
                    private val trackerManager: TrackerManager) : MvpBasePresenter<MainView>() {

    fun start() {
        trackPage()
        val currentAppVersion = BuildConfig.VERSION_CODE
        view?.initViews()
        view?.loadAds()
        if (newAppVersionRequiredAvailable(currentAppVersion)) {
            trackEvent(TrackerConstants.Label.MainScreen.REQUIRED_APP_VERSION, TrackerConstants.Action.SHOW)
            view?.showNewRequiredVersionAvailable()
        } else {
            if (!areParentNamesSet()) {
                view?.openParentNamesActivity(requestForResult = true)
            } else {
                if (newAppVersionAvailable(currentAppVersion)) {
                    trackEvent(TrackerConstants.Label.MainScreen.NEW_APP_VERSION, TrackerConstants.Action.SHOW)
                    view?.showNewVersionAvailable()
                    preferencesManager.setLastNewVersionCheckDate(System.currentTimeMillis())
                }
            }
        }
    }

    fun onRequiredAppVersionDialogOkClicked() {
        trackEvent(TrackerConstants.Label.MainScreen.REQUIRED_APP_OK)
        view?.openPlayStoreLink()
    }

    fun onNewAppVersionAvailableDialogOkClicked() {
        trackEvent(TrackerConstants.Label.MainScreen.NEW_APP_OK)
        view?.openPlayStoreLink()
    }

    fun onRequiredAppVersionDialogDismissed() {
        trackEvent(TrackerConstants.Label.MainScreen.REQUIRED_APP_DISMISSED)
        view?.close()
    }

    fun onNewAppVersionAvailableDialogDismissed() {
        trackEvent(TrackerConstants.Label.MainScreen.NEW_APP_DISMISSED)
    }

    fun onGoClicked() {
        trackEvent(TrackerConstants.Label.MainScreen.GO)
        view?.openFinMatchesActivity()
    }

    fun onDrawerItemDadMomClicked() {
        trackEvent(TrackerConstants.Label.MainScreen.DRAWER_PARENTS_SCREEN)
        view?.openParentNamesActivity()
    }

    fun onDrawerItemBoyNamesListClicked() {
        trackEvent(TrackerConstants.Label.MainScreen.DRAWER_BOY_NAMES_SCREEN)
        view?.openNamesListActivity(Gender.MALE)
    }

    fun onDrawerItemGirlNameListClicked() {
        trackEvent(TrackerConstants.Label.MainScreen.DRAWER_GIRL_NAMES_SCREEN)
        view?.openNamesListActivity(Gender.FEMALE)
    }

    fun onDrawerItemContactClicked() {
        trackEvent(TrackerConstants.Label.MainScreen.DRAWER_CONTACT_SCREEN)
        view?.openContactActivity()
    }

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

    private fun trackPage() {
        trackerManager.sendScreen(TrackerConstants.Section.MAIN.value,
                TrackerConstants.Section.Main.MAIN.value)
    }

    private fun trackEvent(label: TrackerConstants.Label.MainScreen,
                           action: TrackerConstants.Action = TrackerConstants.Action.CLICK) {
        trackerManager.sendEvent(
                category = TrackerConstants.Section.Main.MAIN.value,
                action = action.value,
                label = label.value)
    }
}
