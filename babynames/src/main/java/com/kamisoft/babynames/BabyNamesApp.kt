package com.kamisoft.babynames

import android.app.Application
import com.kamisoft.babyname.BuildConfig
import com.kamisoft.babynames.logger.Logger


class BabyNamesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Logger.start()
        }
    }

}