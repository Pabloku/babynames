package com.kamisoft.babynames.presentation.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.delegate.ViewGroupDelegateCallback
import com.hannesdorfmann.mosby3.mvp.delegate.ViewGroupMvpDelegate
import com.hannesdorfmann.mosby3.mvp.delegate.ViewGroupMvpDelegateImpl

abstract class MvpSearchView<V : MvpView, P : MvpPresenter<V>> : SearchView, ViewGroupDelegateCallback<V, P>, MvpView {

    protected lateinit var searchViewPresenter: P
    protected val mvpDelegate: ViewGroupMvpDelegate<V, P>? by lazy { createMvpDelegate() }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun createMvpDelegate(): ViewGroupMvpDelegate<V, P> {
        return ViewGroupMvpDelegateImpl(this, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mvpDelegate?.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mvpDelegate?.onDetachedFromWindow()
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(): Parcelable? {
        return mvpDelegate?.onSaveInstanceState()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRestoreInstanceState(state: Parcelable) {
        mvpDelegate?.onRestoreInstanceState(state)
    }

    abstract override fun createPresenter(): P

    override fun getMvpView(): V {
        return this as V
    }

    override fun getPresenter(): P = searchViewPresenter

    override fun setPresenter(presenter: P) {
        this.searchViewPresenter = presenter
    }

    override fun superOnSaveInstanceState(): Parcelable {
        return super.onSaveInstanceState()
    }

    override fun superOnRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
    }
}
