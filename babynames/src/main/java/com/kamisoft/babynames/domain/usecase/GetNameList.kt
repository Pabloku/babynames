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

    fun getNames(genre: NamesDataSource.Genre, callback: CallBacks.NamesCallback) {
        Thread(GetNames(genre, callback)).start()
    }

    private fun namesLoaded(genre: NamesDataSource.Genre, callback: CallBacks.NamesCallback) {
        val nameList = namesRepository.getAllNamesByGenre(genre)
        Handler(Looper.getMainLooper()).post(NamesLoaded(callback, nameList))
    }

    private inner class GetNames(val genre: NamesDataSource.Genre, val callback: CallBacks.NamesCallback) : Runnable {
        override fun run() {
            namesLoaded(genre, callback)
        }
    }

    private inner class NamesLoaded(val callback: CallBacks.NamesCallback, val names: List<String>) : Runnable {
        override fun run() {
            callback.namesLoaded(names)
        }
    }

}