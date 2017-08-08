package com.kamisoft.babynames.presentation.choose_name

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.kamisoft.babynames.commons.Constants
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.usecase.GetFavoriteList
import com.kamisoft.babynames.domain.usecase.SaveFavoriteName
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.model.BabyNameLikable
import com.kamisoft.babynames.tracking.TrackerConstants
import com.kamisoft.babynames.tracking.TrackerManager
import com.rahulrav.futures.Future
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class ChooseNamePresenter(
        private val getFavoritesUseCase: GetFavoriteList,
        private val saveFavoriteUseCase: SaveFavoriteName,
        private val trackerManager: TrackerManager) :
        MvpBasePresenter<ChooseNameView>() {

    private var favoriteCounter = 0
    private lateinit var screenToTrack: TrackerConstants.Section.FindMatches

    fun start(listFuture: Future<List<BabyName>>, parentPosition: Int) {
        if (parentPosition == 1) {
            screenToTrack = TrackerConstants.Section.FindMatches.FIRST_PARENT_CHOOSE_NAME
        } else {
            screenToTrack = TrackerConstants.Section.FindMatches.SECOND_PARENT_CHOOSE_NAME
        }
        trackPage(screenToTrack)
        val pullToRefresh = true
        view?.initViews()
        view?.showLoading(!pullToRefresh)
        loadData(listFuture)
    }

    private fun loadData(listFuture: Future<List<BabyName>>) {
        Logger.debug("Future: loadData")
        launch(CommonPool) {
            Logger.debug("Future: loadData launch")
            if (listFuture.ready) {
                Logger.debug("Future: is ready")
                val babyNames = listFuture.result?.toList() ?: emptyList()
                loadNames(babyNames)
            } else {
                Logger.debug("Future: not ready")
                val babyNames = listFuture.await(Constants.TIMEOUT_DEFAULT)?.toList() ?: emptyList()
                loadNames(babyNames)
            }
        }
    }

    private fun loadNames(babyNames: List<BabyName>) {
        if (babyNames.isNotEmpty()) {
            view?.setData(parseToBabyNamesLikable(babyNames))
            view?.showContent()
        } else {
            val pullToRefresh = true
            view?.showError(Exception(), !pullToRefresh)
        }
    }

    private fun parseToBabyNamesLikable(babyNames: List<BabyName>): List<BabyNameLikable> {
        return babyNames.map { BabyNameLikable(it.name, it.origin, it.meaning, liked = false) }
    }

    fun loadFavorites(parent: String?, gender: Gender) {
        parent?.let { getFavoritesUseCase.getFavorites(it, gender, { onFavoritesLoaded(it) }) }
    }

    fun manageBabyNameClick(parent: String, gender: Gender, babyName: BabyNameLikable) {
        saveFavoriteUseCase.saveFavoriteName(parent, gender, babyName.name)
        babyName.liked = !babyName.liked

        if (babyName.liked) {
            trackFavoriteEvent()
            favoriteCounter++
        } else {
            trackUnFavoriteEvent()
            favoriteCounter--
        }
        view?.updateFavoriteCounter(favoriteCounter)
    }

    fun onSearchClicked() {
        trackSearchEvent()
        view?.showSearchView()
    }

    fun onOkClicked() {
        val likedBabyNames = view?.getLikedBabyNames() ?: emptyList()
        if (likedBabyNames.isNotEmpty()) {
            view?.onLikedNamesChosen()
        } else {
            view?.showNoFavoritesMessage()
        }
    }

    fun onFavoritesLoaded(favoriteList: List<String>) {
        Logger.debug("onFavoritesLoaded: ${favoriteList.size} favorites")
        favoriteCounter = favoriteList.size
        view?.setFavoriteList(favoriteList)
        view?.updateFavoriteCounter(favoriteCounter)
    }

    private fun trackPage(screen: TrackerConstants.Section.FindMatches) {
        trackerManager.sendScreen(TrackerConstants.Section.FIND_MATCHES.value,
                screen.value)
    }

    private fun trackEvent(screen: TrackerConstants.Section.FindMatches,
                           label: String) {
        trackerManager.sendEvent(
                category = screen.value,
                action = TrackerConstants.Action.CLICK.value,
                label = label)
    }

    private fun trackFavoriteEvent() {
        if (screenToTrack == TrackerConstants.Section.FindMatches.FIRST_PARENT_CHOOSE_NAME) {
            trackEvent(screenToTrack, TrackerConstants.Label.FirstParentChooseScreen.FAVORITE_NAME.value)
        } else if (screenToTrack == TrackerConstants.Section.FindMatches.SECOND_PARENT_CHOOSE_NAME) {
            trackEvent(screenToTrack, TrackerConstants.Label.SecondParentChooseScreen.FAVORITE_NAME.value)
        }
    }

    private fun trackUnFavoriteEvent() {
        if (screenToTrack == TrackerConstants.Section.FindMatches.FIRST_PARENT_CHOOSE_NAME) {
            trackEvent(screenToTrack, TrackerConstants.Label.FirstParentChooseScreen.UNFAVORITE_NAME.value)
        } else if (screenToTrack == TrackerConstants.Section.FindMatches.SECOND_PARENT_CHOOSE_NAME) {
            trackEvent(screenToTrack, TrackerConstants.Label.SecondParentChooseScreen.UNFAVORITE_NAME.value)
        }
    }

    private fun trackSearchEvent() {
        if (screenToTrack == TrackerConstants.Section.FindMatches.FIRST_PARENT_CHOOSE_NAME) {
            trackEvent(screenToTrack, TrackerConstants.Label.FirstParentChooseScreen.SEARCH.value)
        } else if (screenToTrack == TrackerConstants.Section.FindMatches.SECOND_PARENT_CHOOSE_NAME) {
            trackEvent(screenToTrack, TrackerConstants.Label.SecondParentChooseScreen.SEARCH.value)
        }
    }

}