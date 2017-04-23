package com.kamisoft.babynames.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment<VM, P : BaseFragmentPresenter<VM>, V : BaseFragmentView<VM, P>> : Fragment() {

    var view: V? = null
    var presenter: P? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.view = createView()
        return view?.inflate(inflater, container!!, savedInstanceState)
    }

    override fun onViewCreated(androidView: View?, savedInstanceState: Bundle?) {
        view?.androidViewReady()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.deflate()
        view = null
    }

    override fun onResume() {
        super.onResume()
        if (view != null) {
            presenter = createPresenter()
            view?.start(presenter!!)
        }
    }

    override fun onPause() {
        super.onPause()
        view?.stop()
        presenter = null
    }

    abstract fun createView(): V
    abstract fun createPresenter(): P
}
