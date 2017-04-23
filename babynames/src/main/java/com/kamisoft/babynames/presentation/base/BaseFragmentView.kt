package com.kamisoft.babynames.presentation.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babynames.presentation.base.BaseFragmentPresenter

abstract class BaseFragmentView<VM, P : BaseFragmentPresenter<VM>> {

    protected var rootView: View? = null
    protected var presenter: P? = null

    open fun inflate(inflater: LayoutInflater, container: ViewGroup, @SuppressWarnings("unused") savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(getViewId(), container, false)
        return rootView!!
    }

    fun androidViewReady() {
        if (rootView != null) {
            prepare(rootView!!)
        }
    }

    fun deflate() {
        stop()
        rootView = null
    }

    fun start(presenter: P) {
        this.presenter = presenter

        val viewModel = getViewModel()
        presenter.start(viewModel)
    }

    fun stop() {
        presenter?.stop()
        presenter = null
    }

    /**
     * Return the view id to be inflated
     */
    @LayoutRes
    protected abstract fun getViewId(): Int

    /**
     * Create and return the implementation of the ViewModel
     */
    protected abstract fun getViewModel(): VM

    /**
     * Convenience method so that the implementation knows when UI widget can be obtained and prepared.
     */
    protected abstract fun prepare(rootView: View);
}
