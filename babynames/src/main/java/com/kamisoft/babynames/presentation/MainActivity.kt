package com.kamisoft.babynames.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.data.repository.NamesDataRepository
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.logger.Logger

class MainActivity : AppCompatActivity(), GetNameList.CallBacks.NamesCallback {
    override fun namesLoaded(nameList: List<String>) {
        nameList.forEach { Logger.info(it) }
        Logger.info("Process end")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.start()

        /*doAsync {
            Logger.info("Process start")
            val names = Tasks.await(FirebaseNamesDataSource().getNameListTask(NamesDataSource.Genre.MALE))
            names.forEach { Logger.info(it) }
            Logger.info("Process end")
        }*/

        Logger.info("Process start")
        GetNameList(NamesDataRepository(NamesDataFactory())).getNames(NamesDataSource.Genre.FEMALE, this)
    }
}
