package com.kamisoft.babynames

import android.app.Application
import com.facebook.stetho.Stetho
import com.kamisoft.babyname.BuildConfig
import com.kamisoft.babynames.commons.extensions.DelegatesExt
import com.kamisoft.babynames.logger.Logger


class BabyNamesApp : Application() {

    companion object {
        var instance: BabyNamesApp by DelegatesExt.notNullSingleValue()
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Logger.start()
        }
    }

}