package com.kamisoft.babynames.remote_config

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kamisoft.babynames.logger.Logger
import java.util.*
import java.util.concurrent.ExecutionException


class FirebaseRemoteConfigManager {

    private val firebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    private var param: RemoteConfigParam = RemoteConfigParam.UNKNOWN
    private var defaultValue: Any = ""

    enum class RemoteConfigParam(val value: String) {

        UNKNOWN("unknown"),
        MIN_APP_VERSION_REQUIRED("min_app_version_required"),
        LATEST_APP_VERSION("latest_app_version");

        companion object {
            fun get(value: String): RemoteConfigParam {
                return RemoteConfigParam.values().find { it.value == value } ?: UNKNOWN
            }
        }
    }

    private fun setupFirebaseRemoteConfig(param: RemoteConfigParam, defaultValue: Any) {
        val firebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(false).build()

        val defaultConfigMap = HashMap<String, Any>()
        defaultConfigMap.put(param.value, defaultValue)

        // Apply config settings and default values.
        firebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings)
        firebaseRemoteConfig.setDefaults(defaultConfigMap)
    }

    fun withRemoteParam(param: RemoteConfigParam): DefaultValueUpdater {
        this.param = param
        return DefaultValueUpdater()
    }

    private fun getIntegerParamTask(): Task<Int> {
        val taskCompletionSource = TaskCompletionSource<Int>()
        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener({
                    // Make the fetched config available via
                    // FirebaseRemoteConfig get<type> calls.
                    firebaseRemoteConfig.activateFetched()
                    val integerValue = firebaseRemoteConfig.getLong(param.value)
                    taskCompletionSource.setResult(integerValue.toInt())
                })
                .addOnFailureListener({ e ->
                    // There has been an error fetching the config
                    Logger.error(e, "Error fetching config")
                    val integerValue = firebaseRemoteConfig.getLong(param.value)
                    taskCompletionSource.setResult(integerValue.toInt())
                })

        return taskCompletionSource.getTask()
    }

    private val cacheExpiration: Long
        get() {
            var cacheExpiration: Long = 86400// 1 day in seconds
            // If developer mode is enabled reduce cacheExpiration to 0 so that
            // each fetch goes to the server. This should not be used in release
            // builds.
            if (firebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
                cacheExpiration = 0
            }
            return cacheExpiration
        }

    inner class DefaultValueUpdater {

        fun withDefaultValue(value: Any?): Fetcher {
            defaultValue = value ?: ""
            setupFirebaseRemoteConfig(param, defaultValue)

            return Fetcher()
        }
    }

    inner class Fetcher {

        fun fetchInteger(): Int {
            var intValue = defaultValue as Int
            try {
                intValue = Tasks.await(getIntegerParamTask())
            } catch (e: ExecutionException) {
                Logger.error(e, "fetchInteger error")
            } catch (e: InterruptedException) {
                Logger.error(e, "fetchInteger error")
            }

            return intValue
        }
    }
}
