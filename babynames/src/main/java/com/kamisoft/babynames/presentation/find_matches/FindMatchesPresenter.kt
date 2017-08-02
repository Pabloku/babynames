package com.kamisoft.babynames.presentation.find_matches

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.SummaryResult
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.logger.Logger
import com.rahulrav.futures.Future
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class FindMatchesPresenter(private val getNamesUseCase: GetNameList) : MvpBasePresenter<FindMatchesView>() {

    private var summaryResult = SummaryResult()
    val boyNameListFuture: Future<List<BabyName>> by lazy { createBoyNameListFuture() }
    val girlNameListFuture: Future<List<BabyName>> by lazy { createGirlNameListFuture() }

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
        when (summaryResult.gender) {
            NamesDataSource.Gender.MALE -> loadBoyNameList()
            NamesDataSource.Gender.FEMALE -> loadGirlNameList()
        }
    }

    private fun loadBoyNameList() {
        launch(CommonPool) {
            Logger.debug("boyNameListFuture: launch")
            boyNameListFuture.await(30000)//TODO Ese 30000...
        }
    }

    private fun loadGirlNameList() {
        launch(CommonPool) {
            Logger.debug("girlNameListFuture: launch")
            girlNameListFuture.await(30000)//TODO Ese 30000...
        }
    }

    private fun createBoyNameListFuture() = Future.submit {
        Logger.debug("Future: createBoyNameListFuture")
        val names = getNamesUseCase.getNames(NamesDataSource.Gender.MALE)
        Logger.debug("Future: createBoyNameListFuture: names ${names.size}")
        names
    }

    private fun createGirlNameListFuture() = Future.submit {
        Logger.debug("Future: createGirlNameListFuture")
        val names = getNamesUseCase.getNames(NamesDataSource.Gender.FEMALE)
        Logger.debug("Future: createGirlNameListFuture: names ${names.size}")
        names
    }

    fun getNameListFuture(): Future<List<BabyName>> {
        return when (summaryResult.gender) {
            NamesDataSource.Gender.MALE -> boyNameListFuture
            NamesDataSource.Gender.FEMALE -> girlNameListFuture
        }
    }

    fun onFavoriteCountUpdated(favoriteCount: Int) {
        view?.updateFavoriteCounter(favoriteCount)
    }
}
