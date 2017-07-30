package com.kamisoft.babynames.presentation.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.kamisoft.babyname.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }


    private fun initViews() {
        initToolbar()
        initNavDrawer()
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
            /*when (id) {
                R.id.drawerContact -> presenter.onDrawerItemContactClicked()
                R.id.drawerAboutUs -> presenter.onDrawerItemAboutUsClicked()
                R.id.drawerTerms -> presenter.onDrawerItemTermsClicked()
                R.id.drawerWeplan -> presenter.onDrawerItemWeplanClicked()
            }*/
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun openContactActivity() {
        //openActivity(ContactActivity::class.java)
    }
}
