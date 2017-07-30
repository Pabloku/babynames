package com.kamisoft.babynames.presentation.find_matches.BabyNamesSearchView

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.kamisoft.babyname.R
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.custom_view.MvpSearchView

class BabyNamesSearchView :
        MvpSearchView<BabyNamesSearchViewView, BabyNamesSearchViewPresenter>,
        BabyNamesSearchViewView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun createPresenter(): BabyNamesSearchViewPresenter {
        return BabyNamesSearchViewPresenter()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.start()
    }

    override fun initSearchView() {
        setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha)
        maxWidth = Integer.MAX_VALUE
        isSubmitButtonEnabled = false

        setSearchViewCloseButton()
        setSearchViewTextProperties()
        setSearchViewCursor()
    }

    private fun setSearchViewCloseButton() {
        val closeButton = findViewById(R.id.search_close_btn) as ImageView
        closeButton.setImageResource(R.drawable.ic_close)
    }

    private fun setSearchViewTextProperties() {
        val txtSearch = findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        txtSearch.setHint(R.string.search_hint)
        txtSearch.setHintTextColor(Color.LTGRAY)
        txtSearch.setTextColor(Color.WHITE)
    }

    private fun setSearchViewCursor() {
        val searchTextView = findViewById(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView

        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor) //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
            Logger.error(e, "setSearchViewCursor error")
        }
    }

    fun setQueryListenerToSearchView(searchEvent: (String) -> Unit) {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.onQueryTextSubmit(query, searchEvent)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.onQueryTextChange(newText, searchEvent)
                return true
            }
        })
    }

    override fun doSearch(query: String, searchCallback: (String) -> Unit) {
        Logger.info("query $query")
        if (!query.isEmpty()) searchCallback.invoke(query)
    }

    override fun clearSearchText() {
        val searchTextView = findViewById(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView
        searchTextView.setText("")
    }
}