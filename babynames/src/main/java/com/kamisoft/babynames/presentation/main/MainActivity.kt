package com.kamisoft.babynames.presentation.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.circleReveal
import com.kamisoft.babynames.commons.hide
import com.kamisoft.babynames.commons.isVisible
import com.kamisoft.babynames.commons.show
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.choose_gender.ChooseGenderFragment
import com.kamisoft.babynames.presentation.choose_name.ChooseNameFragment
import com.kamisoft.babynames.presentation.matches.MatchesFragment
import com.kamisoft.babynames.presentation.who_choose.WhoChooseFragment
import kotlinx.android.synthetic.main.activity_main.*

//TODO MVP
class MainActivity : AppCompatActivity(),
        ChooseGenderFragment.ChooseGenderListener,
        WhoChooseFragment.WhoChooseListener,
        ChooseNameFragment.ChooseNamesListener,
        MatchesFragment.MatchesListener {

    private val chooseGenderFragment by lazy { ChooseGenderFragment.createInstance() }
    private val whoChooseFirstFragment by lazy { WhoChooseFragment.createInstance(parentPosition = 1) }
    private val chooseNameFirstFragment by lazy { ChooseNameFragment.createInstance(gender, parentPosition = 1) }
    private val whoChooseSecondFragment by lazy { WhoChooseFragment.createInstance(parentPosition = 2) }
    private val chooseNameSecondFragment by lazy { ChooseNameFragment.createInstance(gender, parentPosition = 2) }
    private val matchesFragment by lazy { MatchesFragment.createInstance() }

    private lateinit var searchMenu: Menu
    private lateinit var searchItem: MenuItem

    private var gender: NamesDataSource.Gender = NamesDataSource.Gender.MALE

    private lateinit var babyNamesFirstParent: List<BabyName>
    private lateinit var babyNamesSecondParent: List<BabyName>

    override fun onGenderSelected(gender: NamesDataSource.Gender) {
        if (gender != this.gender) {
            this.gender = gender
            chooseNameFirstFragment.updateListByGender(gender)
        }
        contentPager.currentItem = contentPager.currentItem + 1
    }

    override fun onWhoSelected(parent: Parent, position: Int) {
        contentPager.currentItem = contentPager.currentItem + 1
    }

    override fun onNamesSelected(babyNamesLiked: List<BabyName>, parentPosition: Int) {
        hideSearchToolbar()
        clearSearchText()
        contentPager.currentItem = contentPager.currentItem + 1
        //TODO save babyNamesLiked in memory
        if (parentPosition == 1) {
            babyNamesFirstParent = babyNamesLiked
        } else {
            babyNamesSecondParent = babyNamesLiked
            val list = babyNamesFirstParent.filter {
                val name1 = it.name
                val babyName2 = babyNamesSecondParent.find { it.name == name1 }
                return@filter babyName2 != null
            }
            matchesFragment.setMatchedItems(ArrayList(list.map { it.name }))
        }
    }


    fun getCurrentFragment(): Fragment {
        val pagerAdapter = contentPager.adapter as ScreenSlidePagerAdapter
        return pagerAdapter.instantiateItem(contentPager, contentPager.currentItem) as Fragment
    }


    override fun onAccept() {
        Logger.debug("Matches acepted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPager()
        setupToolbars()
        stepperIndicator.setViewPager(contentPager)
    }

    fun setupToolbars() {
        setSupportActionBar(toolbar as Toolbar?)
        setSearchToolbar()
    }

    fun setSearchToolbar() {
        val searchToolbar = (searchToolbar as Toolbar)
        searchToolbar.inflateMenu(R.menu.menu_search)
        searchMenu = searchToolbar.menu

        searchToolbar.setNavigationOnClickListener({
            hideSearchToolbar()
            clearSearchText()
        })

        searchItem = searchMenu.findItem(R.id.action_search)

        MenuItemCompat.setOnActionExpandListener(searchItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Do something when collapsed
                hideSearchToolbar()
                clearSearchText()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when expanded
                return true
            }
        })

        initSearchView()
    }

    fun initSearchView() {
        val searchView = searchMenu.findItem(R.id.action_search).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE

        // Enable/Disable Submit button in the keyboard
        searchView.isSubmitButtonEnabled = false
        searchView.setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha)

        // Change search close button image
        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setImageResource(R.drawable.ic_close)

        // set hint and the text colors
        val txtSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        txtSearch.setHint(R.string.search_hint)
        txtSearch.setHintTextColor(Color.LTGRAY)
        txtSearch.setTextColor(Color.WHITE)

        // set the cursor
        val searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView

        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor) //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
            Logger.error(e, "initSearchView error")
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                Logger.info("query $query")
                (getCurrentFragment() as ChooseNameFragment).findNameInList(query)
            }

        })

    }

    override fun onBackPressed() {
        if (searchToolbar.isVisible()) {
            hideSearchToolbar()
            clearSearchText()
        } else {
            if (contentPager.currentItem > 0) {
                contentPager.currentItem -= 1
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_search -> {
                showSearchToolbar()
                searchItem.expandActionView()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showSearchToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            searchToolbar.circleReveal(posFromRight = 1, containsOverflow = true, isShow = true)
        else
            searchToolbar.show()
    }

    private fun hideSearchToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            searchToolbar.circleReveal(posFromRight = 1, containsOverflow = true, isShow = false)
        else
            searchToolbar.hide()
    }

    private fun clearSearchText() {
        val searchView = searchMenu.findItem(R.id.action_search).actionView as SearchView
        val searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView
        searchTextView.setText("")
    }

    object Page {
        val FIRST = 0
        val SECOND = 1
        val THIRD = 2
        val FOURTH = 3
        val FIFTH = 4
        val SIXTH = 5
    }

    private fun initPager() {
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        contentPager.offscreenPageLimit = 5
        contentPager.adapter = pagerAdapter
    }

    val NUMBER_OF_PAGES = 6

    private inner class ScreenSlidePagerAdapter constructor(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                Page.FIRST -> chooseGenderFragment
                Page.SECOND -> whoChooseFirstFragment
                Page.THIRD -> chooseNameFirstFragment
                Page.FOURTH -> whoChooseSecondFragment
                Page.FIFTH -> chooseNameSecondFragment
                Page.SIXTH -> matchesFragment
                else -> {
                    chooseGenderFragment
                }
            }
        }

        override fun getCount(): Int {
            return NUMBER_OF_PAGES
        }
    }
}
