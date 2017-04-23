package com.kamisoft.babynames.presentation.chooseGenre

import com.kamisoft.babynames.presentation.base.BaseFragmentPresenter

class ChooseGenreFragmentPresenter : BaseFragmentPresenter<ChooseGenreFragmentPresenter.ViewModel>() {

    interface ViewModel {
        fun showToast(message: String)
    }

    override fun onStart() {
        // Use this callback to start some operation, like database a query
    }

    override fun onStop() {
        // Stop any running operation that might be busy in the background
    }

    fun buttonToastClicked(message: String) {
        /*toastInteractor.executeSomeComplexOperation(message) {
            viewModel?.showToast(it)
        }*/
    }

    fun buttonSnackbarClicked(message: String) {
        /*snackbarInteractor.executeSomeComplexOperation(message) {
            viewModel?.showSnackbar(it)
        }*/
    }
}
