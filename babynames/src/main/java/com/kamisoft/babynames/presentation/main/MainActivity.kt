﻿package com.kamisoft.babynames.presentation.main

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.extensions.openActivity
import com.kamisoft.babynames.commons.shared_preferences.AndroidPrefsManager
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.presentation.find_matches.FindMatchesActivity
import com.kamisoft.babynames.presentation.parent_names.ParentNamesActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*


class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView {

    override fun createPresenter() = MainPresenter(AndroidPrefsManager(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.start()
    }

    override fun initViews() {
        initToolbar()
        initNavDrawer()
        btnGo.setOnClickListener { presenter.onGoClicked() }
    }

    private fun initToolbar() {
        val toolbar = (toolbar as Toolbar?)
        setSupportActionBar(toolbar)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar?.setNavigationIcon(R.drawable.ic_menu_white_24dp)
    }

    private fun initNavDrawer() {
        navDrawer.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            when (id) {
                R.id.drawerDadAndMom -> presenter.onDrawerItemDadManClicked()
                R.id.drawerFavoritesDad -> presenter.onDrawerItemFavoritesDadClicked()
                R.id.drawerFavoritesMom -> presenter.onDrawerItemFavoritesMomClicked()
                R.id.drawerOurMatches -> presenter.onDrawerItemMatchesClicked()
                R.id.drawerContact -> presenter.onDrawerItemContactClicked()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun openFinMatchesActivity() = openActivity(FindMatchesActivity::class.java)

    override fun openParentNamesActivity() = openActivity(ParentNamesActivity::class.java)

    override fun openFavoritesActivity(parent: Parent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openMatchesActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openContactActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackPressed() {
        if (isNavigationDrawerOpen()) {
            closeNavigationDrawerIfOpen()
        } else {
            super.onBackPressed()
        }
    }

    private fun isNavigationDrawerOpen(): Boolean {
        return drawerLayout.isDrawerOpen(GravityCompat.START)
    }

    private fun closeNavigationDrawerIfOpen() {
        if (isNavigationDrawerOpen()) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun close() = finish()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
