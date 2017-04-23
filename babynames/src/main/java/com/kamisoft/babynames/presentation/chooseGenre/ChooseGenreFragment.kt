package com.kamisoft.babynames.presentation.chooseGenre

import com.kamisoft.babynames.presentation.base.BaseFragment

class ChooseGenreFragment : BaseFragment<ChooseGenreFragmentPresenter.ViewModel, ChooseGenreFragmentPresenter, ChooseGenreFragmentView>() {

    companion object {
        fun createInstance() = ChooseGenreFragment()
    }

    override fun createView(): ChooseGenreFragmentView {
        return ChooseGenreFragmentView()
    }

    override fun createPresenter(): ChooseGenreFragmentPresenter {
        return ChooseGenreFragmentPresenter()
    }
}