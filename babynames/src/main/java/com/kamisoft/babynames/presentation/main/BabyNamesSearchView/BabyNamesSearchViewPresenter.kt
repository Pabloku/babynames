package com.kamisoft.babynames.presentation.main.BabyNamesSearchView

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

class BabyNamesSearchViewPresenter() : MvpBasePresenter<BabyNamesSearchViewView>() {

    fun start() = view?.initSearchView()

    fun onQueryTextSubmit(query: String, searchCallback: (String) -> Unit) {
        view?.doSearch(query, searchCallback)
        view?.clearFocus()
    }

    fun onQueryTextChange(query: String, searchCallback: (String) -> Unit) {
        view?.doSearch(query, searchCallback)
    }
}
