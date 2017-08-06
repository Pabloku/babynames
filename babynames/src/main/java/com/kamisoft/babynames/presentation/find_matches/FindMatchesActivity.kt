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
import com.kamisoft.babynames.commons.OSVersionUtils
import com.kamisoft.babynames.commons.extensions.*
import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.repository.NamesDataRepository
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.presentation.choose_gender.ChooseGenderFragment
import com.kamisoft.babynames.presentation.choose_name.ChooseNameFirstParentFragment
import com.kamisoft.babynames.presentation.choose_name.ChooseNameSecondParentFragment
import com.kamisoft.babynames.presentation.choose_parent.ChooseParentFragment
import com.kamisoft.babynames.presentation.find_matches.BabyNamesSearchView.BabyNamesSearchView
import com.kamisoft.babynames.presentation.matches.MatchesFragment
import kotlinx.android.synthetic.main.activity_find_matches.*
import kotlinx.android.synthetic.main.toolbar.*

class FindMatchesActivity : MvpActivity<FindMatchesView, FindMatchesPresenter>(), FindMatchesView {

    private lateinit var searchMenu: Menu
    private lateinit var searchItem: MenuItem

    private val babyNamesSearchView by lazy { createBabyNamesSearchView() }

    enum class CurrentStep(val value: String) {
        CHOOSE_GENDER("ChooseGender"),
        WHO_CHOOSE("WhoChoose"),
        FIRST_PARENT_CHOOSE_NAME("FirstParentChooseName"),
        SECOND_PARENT_CHOOSE_NAME("SecondParentChooseName"),
        MATCHES("Matches");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_matches)
        presenter.start()
    }

    override fun initViews() {
        initToolbars()
        babyNamesSearchView.setQueryListenerToSearchView({ onSearchItem(it) })
        stepperIndicator.stepCount = CurrentStep.values().size - 1
        stepperIndicator.currentStep = CurrentStep.CHOOSE_GENDER.ordinal
    }

    override fun createPresenter(): FindMatchesPresenter = FindMatchesPresenter(GetNameList(NamesDataRepository(NamesDataFactory())))

    private fun createBabyNamesSearchView(): BabyNamesSearchView {
        return MenuItemCompat.getActionView(searchItem) as BabyNamesSearchView
    }

    override fun showChooseGenderView() {
        val chooseGenderFragment = ChooseGenderFragment.createInstance(genderSelectCallBack = { presenter.onGenderSelected(it) })
        showFragment(fragment = chooseGenderFragment, withRightLeftAnimation = false)
        manageCurrentStep()
    }

    override fun showWhoChooseFirstView() {
        val whoChooseFragment = ChooseParentFragment.createInstance(parentPosition = 1,
                parentSelectCallBack = { firstParentName: String, secondParentName: String -> presenter.onWhoChooseFirst(firstParentName, secondParentName) })
        showFragment(fragment = whoChooseFragment, backStackTag = CurrentStep.WHO_CHOOSE.value)
        stepperIndicator.currentStep = CurrentStep.WHO_CHOOSE.ordinal
        manageCurrentStep()
    }

    override fun showFirstChooseNameView() {
        val chooseNameFragment = ChooseNameFirstParentFragment.createInstance(presenter.getParent1Name(),
                presenter.getGender(), presenter.getNameListFuture(),
                searchCallback = { onSearchClicked() },
                favoriteCallback = { presenter.onFavoriteCountUpdated(it) },
                namesListCallBack = { presenter.onFirstParentChooseNames(it) })
        showFragment(fragment = chooseNameFragment, backStackTag = CurrentStep.FIRST_PARENT_CHOOSE_NAME.value)
        stepperIndicator.currentStep = CurrentStep.FIRST_PARENT_CHOOSE_NAME.ordinal
        manageCurrentStep()
    }

    override fun showSecondChooseNameView() {
        val chooseNameFragment = ChooseNameSecondParentFragment.createInstance(presenter.getParent2Name(),
                presenter.getGender(), presenter.getNameListFuture(),
                searchCallback = { onSearchClicked() },
                favoriteCallback = { presenter.onFavoriteCountUpdated(it) },
                namesListCallBack = { presenter.onSecondParentChooseNames(it) })
        showFragment(fragment = chooseNameFragment, backStackTag = CurrentStep.SECOND_PARENT_CHOOSE_NAME.value)
        stepperIndicator.currentStep = CurrentStep.SECOND_PARENT_CHOOSE_NAME.ordinal
        manageCurrentStep()
    }

    override fun showMatchesView() {
        val list = presenter.getParent1NamesChosen()?.filter {
            val name1 = it.name
            val babyName2 = presenter.getParent2NamesChosen()?.find { it.name == name1 }
            return@filter babyName2 != null
        }
        val matchesMap = HashMap<String, String>(list?.map { it.name to it.origin + "|" + it.meaning }?.toMap() ?: emptyMap<String, String>())
        val matchesFragment = MatchesFragment.createInstance(matchesMap)
        showFragment(fragment = matchesFragment, backStackTag = CurrentStep.MATCHES.value)
        stepperIndicator.currentStep = CurrentStep.MATCHES.ordinal
        manageCurrentStep()
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
        transaction.replace(R.id.contentView, fragment, backStackTag)
        backStackTag?.let { transaction.addToBackStack(backStackTag) }
        transaction.commit()
    }

    private fun initToolbars() {
        initActionToolbar()
        initSearchToolbar()
    }

    private fun onSearchClicked() {
        showSearchToolbar()
        searchItem.expandActionView()
    }

    private fun initActionToolbar() {
        val toolbar = toolbar as Toolbar?
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
            manageCurrentStep()
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

    private fun onSearchItem(item: String) {
        when (stepperIndicator.currentStep) {
            CurrentStep.FIRST_PARENT_CHOOSE_NAME.ordinal -> {
                val chooseNameFragment = supportFragmentManager.findFragmentByTag(CurrentStep.FIRST_PARENT_CHOOSE_NAME.value) as ChooseNameFirstParentFragment?
                chooseNameFragment?.findNameInList(item)
            }
            CurrentStep.SECOND_PARENT_CHOOSE_NAME.ordinal -> {
                val chooseNameFragment = supportFragmentManager.findFragmentByTag(CurrentStep.SECOND_PARENT_CHOOSE_NAME.value) as ChooseNameSecondParentFragment?
                chooseNameFragment?.findNameInList(item)
            }
            else -> {
            }
        }

    }

    private fun manageCurrentStep() {
        when (stepperIndicator.currentStep) {
            CurrentStep.FIRST_PARENT_CHOOSE_NAME.ordinal -> showFavoriteCounter()
            CurrentStep.SECOND_PARENT_CHOOSE_NAME.ordinal -> showFavoriteCounter()
            else -> hideFavoriteCounter()
        }
    }
}
