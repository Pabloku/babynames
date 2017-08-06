package com.kamisoft.babynames.domain.usecase

import android.content.Context
import com.kamisoft.babynames.commons.Constants
import com.kamisoft.babynames.commons.shared_preferences.AndroidPrefsManager
import com.kamisoft.babynames.remote_config.FirebaseRemoteConfigManager


class CheckAppVersions(context: Context) : Runnable {

    val preferences = AndroidPrefsManager(context)

    override fun run() {
        preferences.setAppVersionRequired(getAppVersionRequired())
        preferences.setAppLastVersion(getAppLastVersion())
    }

    private fun getAppVersionRequired(): Int {
        return FirebaseRemoteConfigManager().withRemoteParam(
                FirebaseRemoteConfigManager.RemoteConfigParam.MIN_APP_VERSION_REQUIRED)
                .withDefaultValue(Constants.NOT_SPECIFIED)
                .fetchInteger()
    }

    private fun getAppLastVersion(): Int {
        return FirebaseRemoteConfigManager().withRemoteParam(
                FirebaseRemoteConfigManager.RemoteConfigParam.LATEST_APP_VERSION)
                .withDefaultValue(Constants.NOT_SPECIFIED)
                .fetchInteger()
    }
}