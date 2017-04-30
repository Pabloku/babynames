package com.kamisoft.babynames.presentation.chooseName

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.usecase.GetNameList

class ChooseNamePresenter(private val getNamesUseCase: GetNameList) : MvpBasePresenter<ChooseNameView>() {


    fun loadNames(gender: NamesDataSource.Gender) {
        getNamesUseCase.getNames(gender, NamesCallBack())
    }

    fun onNamesLoaded(nameList: List<String>) {
        view?.setData(nameList)
        view?.showContent()
    }

    inner class NamesCallBack : GetNameList.CallBacks.NamesCallback {
        override fun namesLoaded(nameList: List<String>) {
            onNamesLoaded(nameList)
        }

    }
}