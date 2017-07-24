package com.kamisoft.babynames.presentation.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.extensions.circleReveal
import com.kamisoft.babynames.commons.extensions.gone
import com.kamisoft.babynames.commons.extensions.isVisible
import com.kamisoft.babynames.commons.extensions.visible
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.SummaryResult
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.choose_gender.ChooseGenderFragment
import com.kamisoft.babynames.presentation.choose_name.ChooseNameFragment
import com.kamisoft.babynames.presentation.matches.MatchesFragment
import com.kamisoft.babynames.presentation.choose_parent.ChooseParentFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView {

    private lateinit var searchMenu: Menu
    private lateinit var searchItem: MenuItem

    private var summaryResult = SummaryResult()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbars()
        showChooseGenderView()
        stepperIndicator.stepCount = 4
    }

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun showChooseGenderView() {
        val transaction = supportFragmentManager.beginTransaction()
        val chooseGenderFragment = ChooseGenderFragment.createInstance()
        chooseGenderFragment.callBack = { onGenderSelected(it) }
        transaction.replace(R.id.contentView, chooseGenderFragment)
        transaction.commit()
    }

    private fun onGenderSelected(gender: NamesDataSource.Gender) {
        Logger.debug("Gender selected: $gender")
        summaryResult = summaryResult.copy(gender = gender)
        showWhoChooseFirstView()
    }

    override fun showWhoChooseFirstView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
        val whoChooseFragment = ChooseParentFragment.createInstance(parentPosition = 1)
        whoChooseFragment.callBack = { firstParentName: String, secondParentName: String -> onWhoChooseFirst(firstParentName, secondParentName) }
        transaction.replace(R.id.contentView, whoChooseFragment)
        stepperIndicator.currentStep = 1
        transaction.addToBackStack("whoChooseFirstParent")
        transaction.commit()
    }

    private fun onWhoChooseFirst(firstParentName: String, secondParentName: String) {
        Logger.debug("Choosing first: $firstParentName; Choosing second: $secondParentName")
        summaryResult = summaryResult.copy(parent1Name = firstParentName,
                parent2Name = secondParentName)
        showFirstChooseNameView()
    }

    override fun showFirstChooseNameView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
        val chooseNameFragment = ChooseNameFragment.createInstance(summaryResult.parent1Name, summaryResult.gender)
        chooseNameFragment.callBack = { onFirstParentChooseNames(it) }
        transaction.replace(R.id.contentView, chooseNameFragment)
        stepperIndicator.currentStep = 2
        transaction.addToBackStack("chooseNameFirstParent")
        transaction.commit()
    }

    private fun onFirstParentChooseNames(babyNamesLiked: List<BabyName>) {
        Logger.debug("First parent chose ${babyNamesLiked.size} names")
        summaryResult = summaryResult.copy(parent1NamesChosen = babyNamesLiked)
        showSecondChooseNameView()
    }

    override fun showSecondChooseNameView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
        val chooseNameFragment = ChooseNameFragment.createInstance(summaryResult.parent2Name, summaryResult.gender)
        chooseNameFragment.callBack = { onSecondParentChooseNames(it) }
        transaction.replace(R.id.contentView, chooseNameFragment)
        stepperIndicator.currentStep = 3
        transaction.addToBackStack("chooseNameSecondParent")
        transaction.commit()
    }

    private fun onSecondParentChooseNames(babyNamesLiked: List<BabyName>) {
        Logger.debug("Second parent chose ${babyNamesLiked.size} names")
        summaryResult = summaryResult.copy(parent2NamesChosen = babyNamesLiked)
        showMatchesView()
    }

    override fun showMatchesView() {
        val list = summaryResult.parent1NamesChosen?.filter {
            val name1 = it.name
            val babyName2 = summaryResult.parent2NamesChosen?.find { it.name == name1 }
            return@filter babyName2 != null
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
        val matchesFragment = MatchesFragment.createInstance(ArrayList(list?.map { it.name }))
        transaction.replace(R.id.contentView, matchesFragment)
        stepperIndicator.currentStep = 4
        transaction.addToBackStack("matchesFragment")
        transaction.commit()
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
                //TODO chooseNameFragment.findNameInList(query)
            }

        })

    }

    override fun onBackPressed() {
        if (searchToolbar.isVisible()) {
            hideSearchToolbar()
            clearSearchText()
        } else {
            super.onBackPressed()
            if (stepperIndicator.currentStep > 0) {
                stepperIndicator.currentStep -= 1
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
            searchToolbar.visible()
    }

    private fun hideSearchToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            searchToolbar.circleReveal(posFromRight = 1, containsOverflow = true, isShow = false)
        else
            searchToolbar.gone()
    }

    private fun clearSearchText() {
        val searchView = searchMenu.findItem(R.id.action_search).actionView as SearchView
        val searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView
        searchTextView.setText("")
    }
}
