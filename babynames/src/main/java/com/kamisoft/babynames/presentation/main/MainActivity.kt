package com.kamisoft.babynames.presentation.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.kamisoft.babynames.R
import com.kamisoft.babynames.commons.Constants
import com.kamisoft.babynames.commons.extensions.openActivity
import com.kamisoft.babynames.commons.extensions.openActivityForResult
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

    companion object Request {
        const val PARENTS_SCREEN = 1001
    }

    override fun createPresenter() = MainPresenter(AndroidPrefsManager(this),
            TrackerManager(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.start()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            animate()
            super.onWindowFocusChanged(hasFocus)
        } else {
            hideToolbar()
        }
    }

    private fun animate() {
        animateToolbar()
        animateLogo()
        animateTitle()
        animateSubtitle()
        animateButtonGo()
    }

    private fun hideToolbar() {
        val toolbar = toolbar as Toolbar
        toolbar.animate()
                .translationY(-toolbar.height.toFloat())
                .setDuration(Constants.Animations.DURATION_SHORT).start()
    }

    private fun animateToolbar(): ViewPropertyAnimator {
        val toolbar = toolbar as Toolbar
        toolbar.translationY = -toolbar.height.toFloat()
        val toolbarTitle = toolbar.getChildAt(0) as AppCompatImageView
        toolbarTitle.translationY = -toolbar.height.toFloat()

        toolbar.animate()
                .translationY(0f)
                .setDuration(Constants.Animations.DURATION_SHORT)
                .setStartDelay(Constants.Animations.ITEM_DELAY).start()

        val animator = toolbarTitle.animate()
                .translationY(0f)
                .setDuration(Constants.Animations.DURATION_SHORT)
                .setStartDelay(Constants.Animations.ITEM_DELAY)
        animator.start()
        return animator
    }

    private fun animateLogo() {
        ViewCompat.animate(imgLogo)
                .translationY(-250f)
                .setStartDelay(Constants.Animations.STARTUP_DELAY)
                .setDuration(Constants.Animations.DURATION_SHORT).setInterpolator(
                DecelerateInterpolator(1.2f)).start()
    }

    private fun animateTitle() {
        ViewCompat.animate(txtTitle)
                .translationY(-250f).alpha(1f)
                .setStartDelay(Constants.Animations.ITEM_DELAY * 1 + 200)
                .setDuration(Constants.Animations.DURATION_SHORT).setInterpolator(DecelerateInterpolator()).start()
    }

    private fun animateSubtitle() {
        ViewCompat.animate(txtSubTitle)
                .translationY(-250f).alpha(1f)
                .setStartDelay(Constants.Animations.ITEM_DELAY * 2 + 200)
                .setDuration(Constants.Animations.DURATION_SHORT).setInterpolator(DecelerateInterpolator()).start()
    }

    private fun animateButtonGo() {
        ViewCompat.animate(btnGo)
                .translationY(-250f)
                .scaleY(1f).scaleX(1f)
                .setStartDelay(Constants.Animations.ITEM_DELAY * 3 + 200)
                .setDuration(Constants.Animations.DURATION_MEDIUM).setInterpolator(DecelerateInterpolator()).start()
    }

    private fun animateAdBanner() {
        ViewCompat.animate(adView).alpha(1f)
                .setStartDelay(Constants.Animations.ITEM_DELAY * 1 + 200)
                .setDuration(Constants.Animations.DURATION_LONG).setInterpolator(DecelerateInterpolator()).start()
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

    override fun openParentNamesActivity(requestForResult: Boolean) {
        if (requestForResult) {
            openActivityForResult(ParentNamesActivity::class.java, Request.PARENTS_SCREEN)
        } else {
            openActivity(ParentNamesActivity::class.java)
        }
    }

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
                    presenter.onNewAppVersionAvailableDialogOkClicked()
                })
                .dismissListener({ presenter.onNewAppVersionAvailableDialogDismissed() })
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
                    presenter.onRequiredAppVersionDialogOkClicked()
                })
                .dismissListener({ presenter.onRequiredAppVersionDialogDismissed() })
                .show()
    }

    override fun openPlayStoreLink() {
        val googlePlayWeplanUrl = getString(R.string.app_url_play_store)
        val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(googlePlayWeplanUrl))
        startActivity(intentWeb)
    }

    override fun loadAds() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                animateAdBanner()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Request.PARENTS_SCREEN && resultCode == Activity.RESULT_CANCELED) {
            close()
        }
    }
}
