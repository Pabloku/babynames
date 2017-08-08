package com.kamisoft.babynames

import android.app.Application
import com.facebook.stetho.Stetho
import com.google.android.gms.ads.MobileAds
import com.kamisoft.babynames.alarms.AlarmCreator
import com.kamisoft.babynames.commons.extensions.DelegatesExt
import com.kamisoft.babynames.logger.Logger


class BabyNamesApp : Application() {

    companion object {
        var instance: BabyNamesApp by DelegatesExt.notNullSingleValue()
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        AlarmCreator.withContext(this).initAlarms()
        MobileAds.initialize(applicationContext, "ca-app-pub-9682684081624763~3541460628");

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Logger.start()
        }
    }

}