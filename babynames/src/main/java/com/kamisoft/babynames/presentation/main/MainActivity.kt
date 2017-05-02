package com.kamisoft.babynames.presentation.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.kamisoft.babyname.R
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


class MainActivity : AppCompatActivity(),
        ChooseGenderFragment.ChooseGenderListener,
        WhoChooseFragment.WhoChooseListener,
        ChooseNameFragment.ChooseNamesListener,
        MatchesFragment.MatchesListener {

    private lateinit var searchMenu: Menu
    private lateinit var searchItem: MenuItem

    private lateinit var gender: NamesDataSource.Gender

    private lateinit var babyNamesFirstParent: List<BabyName>
    private lateinit var babyNamesSecondParent: List<BabyName>

    override fun onGenderSelected(gender: NamesDataSource.Gender) {
        this.gender = gender
        showWhoChooseView(1)
    }

    override fun onWhoSelected(parent: Parent, position: Int) {
        showChooseNameView(this.gender, position)
    }

    override fun onNamesSelected(babyNamesLiked: List<BabyName>, parentPosition: Int) {
        //TODO save babyNamesLiked in memory
        if (parentPosition == 1) {
            babyNamesFirstParent = babyNamesLiked
            showWhoChooseView(2)
        } else {
            //TODO show coincidences fragment!
            babyNamesSecondParent = babyNamesLiked
            val list = babyNamesFirstParent.filter {
                val name1 = it.name
                val babyName2 = babyNamesSecondParent.find { it.name == name1 }
                return@filter babyName2 != null
            }
            showCoincidencesView(ArrayList(list.map { it.name }))
        }
    }

    override fun onAccept() {
        Logger.debug("Matches acepted")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbars()
        showChooseGenderView()
        stepperIndicator.stepCount = 5
    }

    fun setupToolbars() {
        setSupportActionBar(toolbar as Toolbar?);
        setSearchToolbar();
    }

    fun setSearchToolbar() {
        val searchToolbar = (searchToolbar as Toolbar)
        searchToolbar.inflateMenu(R.menu.menu_search)
        searchMenu = searchToolbar.menu

        searchToolbar.setNavigationOnClickListener({
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(searchToolbar, 1, true, false)
            else
                searchToolbar.hide()
        })

        searchItem = searchMenu.findItem(R.id.action_search)

        MenuItemCompat.setOnActionExpandListener(searchItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Do something when collapsed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    circleReveal(searchToolbar, 1, true, false)
                } else
                    searchToolbar.hide()
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
        searchView.maxWidth = Integer.MAX_VALUE;

        // Enable/Disable Submit button in the keyboard

        searchView.isSubmitButtonEnabled = false
        searchView.setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha)

        // Change search close button image

        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setImageResource(R.drawable.ic_close)

        // set hint and the text colors

        val txtSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        txtSearch.hint = "Search..."
        txtSearch.setHintTextColor(Color.LTGRAY)
        txtSearch.setTextColor(Color.WHITE)

        // set the cursor

        val searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor) //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
            e.printStackTrace()
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

            }

        })

    }

    fun showChooseGenderView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentView, ChooseGenderFragment.createInstance())
        transaction.commit()
    }

    fun showWhoChooseView(parentPosition: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.contentView, WhoChooseFragment.createInstance(parentPosition))
        if (parentPosition == 1) { //TODO isFirstParent
            stepperIndicator.currentStep = 1
            transaction.addToBackStack("whoChooseFirstParent")
        } else {
            stepperIndicator.currentStep = 3
            transaction.addToBackStack("whoChooseSecondParent")
        }
        transaction.commit()
    }

    fun showChooseNameView(gender: NamesDataSource.Gender, parentPosition: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.contentView, ChooseNameFragment.createInstance(gender, parentPosition))
        if (parentPosition == 1) {
            stepperIndicator.currentStep = 2
            transaction.addToBackStack("chooseNameFirstParent")
        } else {
            stepperIndicator.currentStep = 4
            transaction.addToBackStack("chooseNameSecondParent")
        }
        transaction.commit()
    }

    fun showCoincidencesView(matches: ArrayList<String>) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.contentView, MatchesFragment.createInstance(matches))
        transaction.addToBackStack("coincidences")
        transaction.commit()
    }

    override fun onBackPressed() {
        if (searchToolbar.isVisible()) {
            circleReveal(searchToolbar, 1, true, false)
        }else {
            super.onBackPressed()
            if (stepperIndicator.currentStep > 0) {
                stepperIndicator.currentStep -= 1
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_search -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(searchToolbar, 1, true, true)
                else
                    searchToolbar.show()

                searchItem.expandActionView()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun circleReveal(myView: View, posFromRight: Int, containsOverflow: Boolean, isShow: Boolean) {
        var width = myView.width

        if (posFromRight > 0)
            width -= posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2
        if (containsOverflow)
            width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)

        val cx = width
        val cy = myView.height / 2

        val anim: Animator
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, width.toFloat())
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, width.toFloat(), 0f)

        anim.duration = 220.toLong()

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    myView.visibility = View.INVISIBLE
                }
            }
        })

        // make the view visible and start the animation
        if (isShow)
            myView.show()

        // start the animation
        anim.start()
    }
}
