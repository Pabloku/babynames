package com.kamisoft.babynames.presentation.main

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.data.repository.NamesDataRepository
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.SummaryResult
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.logger.Logger
import com.rahulrav.futures.Future
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class MainPresenter(private val getNamesUseCase: GetNameList) : MvpBasePresenter<MainView>() {

    private var summaryResult = SummaryResult()
    val namesListFuture: Future<List<BabyName>> by lazy { createNameListFuture() }

    fun start() {
        view?.let {
            it.initViews()
            it.showChooseGenderView()
        }
    }

    fun onGenderSelected(gender: NamesDataSource.Gender) {
        Logger.debug("Gender selected: $gender")

        summaryResult = summaryResult.copy(gender = gender)
        loadNameList()
        view?.showWhoChooseFirstView()
    }

    fun onWhoChooseFirst(firstParentName: String, secondParentName: String) {
        Logger.debug("Choosing first: $firstParentName; Choosing second: $secondParentName")
        summaryResult = summaryResult.copy(parent1Name = firstParentName,
                parent2Name = secondParentName)
        view?.showFirstChooseNameView()
    }

    fun onFirstParentChooseNames(babyNamesLiked: List<BabyName>) {
        Logger.debug("First parent chose ${babyNamesLiked.size} names")
        summaryResult = summaryResult.copy(parent1NamesChosen = babyNamesLiked)
        view?.showSecondChooseNameView()
    }

    fun onSecondParentChooseNames(babyNamesLiked: List<BabyName>) {
        Logger.debug("Second parent chose ${babyNamesLiked.size} names")
        summaryResult = summaryResult.copy(parent2NamesChosen = babyNamesLiked)
        view?.showMatchesView()
    }

    fun getGender() = summaryResult.gender
    fun getParent1Name() = summaryResult.parent1Name
    fun getParent2Name() = summaryResult.parent2Name
    fun getParent1NamesChosen() = summaryResult.parent1NamesChosen
    fun getParent2NamesChosen() = summaryResult.parent2NamesChosen

    private fun loadNameList() {
        launch(CommonPool) {
            Logger.debug("Future: launch")
            namesListFuture.await(30000)
        }
    }

    private fun createNameListFuture() = Future.submit {
        Logger.debug("Future: createNameListFuture")
        val getNamesUseCase = GetNameList(NamesDataRepository(NamesDataFactory()))
        val names = getNamesUseCase.getNames(summaryResult.gender)
        Logger.debug("Future: createNameListFuture: names ${names.size}")
        names
    }
}
