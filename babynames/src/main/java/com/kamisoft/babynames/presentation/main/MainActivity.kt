package com.kamisoft.babynames.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.extensions.openActivity
import com.kamisoft.babynames.commons.shared_preferences.AndroidPrefsManager
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.presentation.contact.ContactActivity
import com.kamisoft.babynames.presentation.find_matches.FindMatchesActivity
import com.kamisoft.babynames.presentation.names_list.NamesListActivity
import com.kamisoft.babynames.presentation.parent_names.ParentNamesActivity
import com.kamisoft.babynames.tracking.TrackerManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*


class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView {

    override fun createPresenter() = MainPresenter(AndroidPrefsManager(this),
            TrackerManager(this))

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
        val toolbar = toolbar as Toolbar?
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
                R.id.drawerDadAndMom -> presenter.onDrawerItemDadMomClicked()
                R.id.drawerBoyNames -> presenter.onDrawerItemBoyNamesListClicked()
                R.id.drawerGirlNames -> presenter.onDrawerItemGirlNameListClicked()
                R.id.drawerContact -> presenter.onDrawerItemContactClicked()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun openFinMatchesActivity() = openActivity(FindMatchesActivity::class.java)

    override fun openParentNamesActivity() = openActivity(ParentNamesActivity::class.java)

    override fun openNamesListActivity(gender: Gender) {
        val params = Bundle()
        params.putString(NamesListActivity.ARG_GENDER, gender.value)
        openActivity(NamesListActivity::class.java, params)
    }

    override fun openContactActivity() = openActivity(ContactActivity::class.java)

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

    override fun showNewVersionAvailable() {
        MaterialDialog.Builder(this).cancelable(true)
                .title(R.string.new_app_version_title)
                .content(getString(R.string.new_app_version_message, getString(R.string.app_name)))
                .positiveText(R.string.new_app_version_ok)
                .negativeText(R.string.new_app_version_cancel)
                .autoDismiss(true)
                .onPositive({ _, _ ->
                    val googlePlayWeplanUrl = getString(R.string.app_url_play_store)
                    val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(googlePlayWeplanUrl))
                    startActivity(intentWeb)
                })
                .show()
    }

    override fun showNewRequiredVersionAvailable() {
        MaterialDialog.Builder(this).cancelable(false)
                .title(getString(R.string.required_app_version_title, getString(R.string.app_name)))
                .content(getString(R.string.required_app_version_message, getString(R.string.app_name)))
                .positiveText(R.string.required_app_version_ok)
                .negativeText(R.string.required_app_version_cancel)
                .autoDismiss(true)
                .onPositive({ _, _ ->
                    val googlePlayWeplanUrl = getString(R.string.app_url_play_store)
                    val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(googlePlayWeplanUrl))
                    startActivity(intentWeb)
                })
                .dismissListener({ finish() })
                .show()
    }
}
