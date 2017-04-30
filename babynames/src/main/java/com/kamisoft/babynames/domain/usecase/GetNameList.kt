package com.kamisoft.babynames.domain.usecase

import android.os.Handler
import android.os.Looper
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.repository.NamesRepository

class GetNameList(val namesRepository: NamesRepository) {

    class CallBacks {
        interface NamesCallback {
            fun namesLoaded(nameList: List<String>)
        }
    }

    fun getNames(gender: NamesDataSource.Gender, callback: CallBacks.NamesCallback) {
        Thread(GetNames(gender, callback)).start()
    }

    private fun loadNames(gender: NamesDataSource.Gender, callback: CallBacks.NamesCallback) {
        val nameList = namesRepository.getAllNamesByGender(gender)
        Handler(Looper.getMainLooper()).post(NamesLoaded(callback, nameList))
    }

    private inner class GetNames(val gender: NamesDataSource.Gender, val callback: CallBacks.NamesCallback) : Runnable {
        override fun run() {
            loadNames(gender, callback)
        }
    }

    private inner class NamesLoaded(val callback: CallBacks.NamesCallback, val names: List<String>) : Runnable {
        override fun run() {
            callback.namesLoaded(names)
        }
    }

}