package com.kamisoft.babynames.presentation.find_matches

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.extensions.*
import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.repository.NamesDataRepository
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.presentation.choose_gender.ChooseGenderFragment
import com.kamisoft.babynames.presentation.choose_name.ChooseNameFragment
import com.kamisoft.babynames.presentation.choose_parent.ChooseParentFragment
import com.kamisoft.babynames.presentation.find_matches.BabyNamesSearchView.BabyNamesSearchView
import com.kamisoft.babynames.presentation.matches.MatchesFragment
import kotlinx.android.synthetic.main.activity_find_matches.*
import kotlinx.android.synthetic.main.toolbar.*

class FindMatchesActivity : MvpActivity<FindMatchesView, FindMatchesPresenter>(), FindMatchesView {

    private lateinit var searchMenu: Menu
    private lateinit var searchItem: MenuItem

    private val babyNamesSearchView by lazy { createBabyNamesSearchView() }

    fun createBabyNamesSearchView(): BabyNamesSearchView {
        return MenuItemCompat.getActionView(searchItem) as BabyNamesSearchView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_matches)
        presenter.start()
    }

    override fun initViews() {
        setupToolbars()
        stepperIndicator.stepCount = 4
    }

    override fun createPresenter(): FindMatchesPresenter = FindMatchesPresenter(GetNameList(NamesDataRepository(NamesDataFactory())))

    override fun showChooseGenderView() {
        val chooseGenderFragment = ChooseGenderFragment.createInstance(genderSelectCallBack = { presenter.onGenderSelected(it) })
        showFragment(fragment = chooseGenderFragment, withRightLeftAnimation = false)
        hideFavoriteCounter()
    }

    override fun showWhoChooseFirstView() {
        val whoChooseFragment = ChooseParentFragment.createInstance(parentPosition = 1, //TODO maybe not necessary
                parentSelectCallBack = { firstParentName: String, secondParentName: String -> presenter.onWhoChooseFirst(firstParentName, secondParentName) })
        showFragment(fragment = whoChooseFragment, backStackTag = "whoChooseFirstParent")
        stepperIndicator.currentStep = 1
        hideFavoriteCounter()
    }

    override fun showFirstChooseNameView() {
        val chooseNameFragment = ChooseNameFragment.createInstance(presenter.getParent1Name(),
                presenter.getGender(), presenter.getNameListFuture(),
                searchCallback = { onSearchClicked() },
                favoriteCallback = { presenter.onFavoriteCountUpdated(it) },
                namesListCallBack = { presenter.onFirstParentChooseNames(it) })
        showFragment(fragment = chooseNameFragment, backStackTag = "chooseNameFirstParent")
        babyNamesSearchView.setQueryListenerToSearchView({ chooseNameFragment.findNameInList(it) })
        stepperIndicator.currentStep = 2
        showFavoriteCounter()
    }

    override fun showSecondChooseNameView() {
        val chooseNameFragment = ChooseNameFragment.createInstance(presenter.getParent2Name(),
                presenter.getGender(), presenter.getNameListFuture(),
                searchCallback = { onSearchClicked() },
                favoriteCallback = { presenter.onFavoriteCountUpdated(it) },
                namesListCallBack = { presenter.onSecondParentChooseNames(it) })
        showFragment(fragment = chooseNameFragment, backStackTag = "chooseNameSecondParent")
        babyNamesSearchView.setQueryListenerToSearchView({ chooseNameFragment.findNameInList(it) })
        stepperIndicator.currentStep = 3
        showFavoriteCounter()
    }

    override fun showMatchesView() {
        val list = presenter.getParent1NamesChosen()?.filter {
            val name1 = it.name
            val babyName2 = presenter.getParent2NamesChosen()?.find { it.name == name1 }
            return@filter babyName2 != null
        }
        val matchesFragment = MatchesFragment.createInstance(ArrayList(list?.map { it.name } ?: emptyList()))
        showFragment(fragment = matchesFragment, backStackTag = "matchesFragment")
        stepperIndicator.currentStep = 4
        hideFavoriteCounter()
    }

    override fun updateFavoriteCounter(favoriteCount: Int) {
        tsLikesCounter.setCurrentText((favoriteCount - 1).toString())
        tsLikesCounter.setText(favoriteCount.toString())
    }

    private fun showFavoriteCounter() = layFavoriteCounter.visible()

    private fun hideFavoriteCounter() = layFavoriteCounter.gone()

    private fun showFragment(fragment: Fragment, backStackTag: String? = null, withRightLeftAnimation: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        if (withRightLeftAnimation) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right)
        }
        transaction.replace(R.id.contentView, fragment)
        backStackTag?.let { transaction.addToBackStack(backStackTag) }
        transaction.commit()
    }

    private fun setupToolbars() {
        initActionToolbar()
        initSearchToolbar()
    }

    private fun onSearchClicked() {
        showSearchToolbar()
        searchItem.expandActionView()
    }

    private fun initActionToolbar() {
        val toolbar = (toolbar as Toolbar?)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar?.setNavigationIcon(R.drawable.ic_close)
    }

    private fun initSearchToolbar() {
        val searchToolbar = (searchToolbar as Toolbar)
        searchToolbar.inflateMenu(R.menu.menu_search)
        searchMenu = searchToolbar.menu
        searchItem = searchMenu.findItem(R.id.actionSearch)

        searchToolbar.setNavigationOnClickListener({
            hideSearchToolbar()
            babyNamesSearchView.clearSearchText()
        })

        MenuItemCompat.setOnActionExpandListener(searchItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                hideSearchToolbar()
                babyNamesSearchView.clearSearchText()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }
        })
    }

    override fun onBackPressed() {
        if (searchToolbar.isVisible()) {
            hideSearchToolbar()
            babyNamesSearchView.clearSearchText()
        } else {
            super.onBackPressed()
            if (stepperIndicator.currentStep > 0) {
                stepperIndicator.currentStep -= 1
            }
        }
    }

    @SuppressLint("NewApi")
    private fun showSearchToolbar() {
        if (OSVersionUtils.isGreaterOrEqualThanLollipop)
            searchToolbar.circleReveal(posFromRight = 1, containsOverflow = true, isShow = true)
        else
            searchToolbar.visible()
    }

    @SuppressLint("NewApi")
    private fun hideSearchToolbar() {
        if (OSVersionUtils.isGreaterOrEqualThanLollipop)
            searchToolbar.circleReveal(posFromRight = 1, containsOverflow = true, isShow = false)
        else
            searchToolbar.gone()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
