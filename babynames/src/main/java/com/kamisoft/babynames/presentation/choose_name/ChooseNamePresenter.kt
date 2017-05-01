package com.kamisoft.babynames.presentation.choose_name

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.usecase.GetNameList

class ChooseNamePresenter(private val getNamesUseCase: GetNameList) :
        MvpBasePresenter<ChooseNameView>() {

    fun loadNames(gender: NamesDataSource.Gender) {
        getNamesUseCase.getNames(gender, NamesCallBack())
    }

    fun manageBabyNameClick(babyName: BabyName) {
        babyName.liked = !babyName.liked
    }

    fun onNamesLoaded(nameList: List<BabyName>) {
        view?.setData(nameList)
        view?.showContent()
    }

    inner class NamesCallBack : GetNameList.CallBacks.NamesCallback {
        override fun namesLoaded(nameList: List<BabyName>) {
            onNamesLoaded(nameList)
        }

    }

}