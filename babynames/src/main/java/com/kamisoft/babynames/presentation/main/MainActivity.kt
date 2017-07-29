package com.kamisoft.babynames.presentation.main

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
import com.kamisoft.babynames.presentation.main.BabyNamesSearchView.BabyNamesSearchView
import com.kamisoft.babynames.presentation.matches.MatchesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView {

    private lateinit var searchMenu: Menu
    private lateinit var searchItem: MenuItem

    private val babyNamesSearchView by lazy { createBabyNamesSearchView() }

    fun createBabyNamesSearchView(): BabyNamesSearchView {
        return MenuItemCompat.getActionView(searchItem) as BabyNamesSearchView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.start()
    }

    override fun initViews() {
        setupToolbars()
        stepperIndicator.stepCount = 4
    }

    override fun createPresenter(): MainPresenter = MainPresenter(GetNameList(NamesDataRepository(NamesDataFactory())))

    override fun showChooseGenderView() {
        val chooseGenderFragment = ChooseGenderFragment.createInstance(genderSelectCallBack = { presenter.onGenderSelected(it) })
        showFragment(fragment = chooseGenderFragment)
    }

    override fun showWhoChooseFirstView() {
        val whoChooseFragment = ChooseParentFragment.createInstance(parentPosition = 1,
                parentSelectCallBack = { firstParentName: String, secondParentName: String -> presenter.onWhoChooseFirst(firstParentName, secondParentName) })
        showFragment(fragment = whoChooseFragment, backStackTag = "whoChooseFirstParent")
        stepperIndicator.currentStep = 1
    }

    override fun showFirstChooseNameView() {
        val chooseNameFragment = ChooseNameFragment.createInstance(presenter.getParent1Name(),
                presenter.getGender(), presenter.getNameListFuture(),
                searchCallback = { onSearchClicked() },
                namesListCallBack = { presenter.onFirstParentChooseNames(it) })
        showFragment(fragment = chooseNameFragment, backStackTag = "chooseNameSecondParent")
        babyNamesSearchView.setQueryListenerToSearchView({ chooseNameFragment.findNameInList(it) })
        stepperIndicator.currentStep = 2
    }

    override fun showSecondChooseNameView() {
        val chooseNameFragment = ChooseNameFragment.createInstance(presenter.getParent2Name(),
                presenter.getGender(), presenter.getNameListFuture(),
                searchCallback = { onSearchClicked() },
                namesListCallBack = { presenter.onSecondParentChooseNames(it) })
        showFragment(fragment = chooseNameFragment, backStackTag = "chooseNameSecondParent")
        babyNamesSearchView.setQueryListenerToSearchView({ chooseNameFragment.findNameInList(it) })
        stepperIndicator.currentStep = 3
    }

    override fun showMatchesView() {
        val list = presenter.getParent1NamesChosen()?.filter {
            val name1 = it.name
            val babyName2 = presenter.getParent2NamesChosen()?.find { it.name == name1 }
            return@filter babyName2 != null
        }
        val matchesFragment = MatchesFragment.createInstance(ArrayList(list?.map { it.name }))
        showFragment(fragment = matchesFragment, backStackTag = "matchesFragment")
        stepperIndicator.currentStep = 4
    }

    private fun showFragment(fragment: Fragment, backStackTag: String? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
        transaction.replace(R.id.contentView, fragment)
        backStackTag?.let { transaction.addToBackStack(backStackTag) }
        transaction.commit()
    }

    private fun setupToolbars() {
        setSupportActionBar(toolbar as Toolbar?)
        setSearchToolbar()
    }

    private fun onSearchClicked() {
        showSearchToolbar()
        searchItem.expandActionView()
    }

    private fun setSearchToolbar() {
        val searchToolbar = (searchToolbar as Toolbar)
        searchToolbar.inflateMenu(R.menu.menu_search)
        searchMenu = searchToolbar.menu

        searchToolbar.setNavigationOnClickListener({
            hideSearchToolbar()
            babyNamesSearchView.clearSearchText()
        })

        searchItem = searchMenu.findItem(R.id.actionSearch)

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
}
