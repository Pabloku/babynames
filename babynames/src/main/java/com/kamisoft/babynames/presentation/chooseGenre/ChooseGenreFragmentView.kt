package com.kamisoft.babynames.presentation.chooseGenre

import android.view.View
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.showToast
import com.kamisoft.babynames.presentation.base.BaseFragmentView
import kotlinx.android.synthetic.main.fragment_choose_genre.view.*

class ChooseGenreFragmentView : BaseFragmentView<ChooseGenreFragmentPresenter.ViewModel, ChooseGenreFragmentPresenter>() {

    override fun getViewId() = R.layout.fragment_choose_genre

    override fun prepare(rootView: View) {
        rootView.btnBoy.setOnClickListener {
            presenter?.buttonToastClicked("btnBoy")
        }

        rootView.btnGirl.setOnClickListener {
            presenter?.buttonSnackbarClicked("btnGirl")
        }
    }

    override fun getViewModel() = object : ChooseGenreFragmentPresenter.ViewModel {

        override fun showToast(message: String) {
            rootView?.showToast(message)
        }
    }
}
