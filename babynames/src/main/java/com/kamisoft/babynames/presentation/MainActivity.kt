package com.kamisoft.babynames.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import com.kamisoft.babyname.R
import com.kamisoft.babynames.data.repository.datasource.FirebaseNamesDataSource
import com.kamisoft.babynames.data.repository.datasource.NamesDataSource
import com.kamisoft.babynames.logger.Logger
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.start()

        doAsync {
            Logger.info("Process start")
            val names = Tasks.await(FirebaseNamesDataSource().getNameListTask(NamesDataSource.Genre.MALE))
            names.forEach { Logger.info(it) }
            Logger.info("Process end")
        }
    }
}
