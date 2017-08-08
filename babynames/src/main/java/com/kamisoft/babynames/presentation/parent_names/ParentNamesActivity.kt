package com.kamisoft.babynames.presentation.parent_names

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.extensions.isEmpty
import com.kamisoft.babynames.commons.extensions.openActivity
import com.kamisoft.babynames.commons.extensions.snackbar
import com.kamisoft.babynames.commons.shared_preferences.AndroidPrefsManager
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.presentation.find_matches.FindMatchesActivity
import com.kamisoft.babynames.tracking.TrackerConstants
import com.kamisoft.babynames.tracking.TrackerManager
import kotlinx.android.synthetic.main.activity_parent_names.*


class ParentNamesActivity : AppCompatActivity() {

    private val preferencesManager by lazy { AndroidPrefsManager(this) }
    private val trackerManager by lazy { TrackerManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_names)
        trackPage()
        initViews()
    }

    private fun initViews() {
        initToolbar()

        btnParent1Dad.setOnClickListener {
            trackEvent(TrackerConstants.Label.ParentsScreen.PARENT1_DAD)
            enableDadButton(btnDad = it as Button, btnMom = btnParent1Mom)
        }
        btnParent1Mom.setOnClickListener {
            trackEvent(TrackerConstants.Label.ParentsScreen.PARENT1_MOM)
            enableMomButton(btnDad = btnParent1Dad, btnMom = it as Button)
        }
        btnParent2Dad.setOnClickListener {
            trackEvent(TrackerConstants.Label.ParentsScreen.PARENT2_DAD)
            enableDadButton(btnDad = it as Button, btnMom = btnParent2Mom)
        }
        btnParent2Mom.setOnClickListener {
            trackEvent(TrackerConstants.Label.ParentsScreen.PARENT2_MOM)
            enableMomButton(btnDad = btnParent2Dad, btnMom = it as Button)
        }
        btnGo.setOnClickListener { go() }

        edtParent1Name.setText(preferencesManager.getParent1Name())
        edtParent2Name.setText(preferencesManager.getParent2Name())
        if (preferencesManager.getParent1() == Parent.MOM.value) {
            btnParent1Mom.performClick()
        } else {
            btnParent1Dad.performClick()
        }
        if (preferencesManager.getParent2() == Parent.DAD.value) {
            btnParent2Dad.performClick()
        } else {
            btnParent2Mom.performClick()
        }
    }

    private fun initToolbar() {
        val toolbar = toolbar as Toolbar?
        setSupportActionBar(toolbar)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar?.setNavigationIcon(R.drawable.ic_close)
    }

    private fun enableDadButton(btnDad: Button, btnMom: Button) {
        btnDad.setBackgroundColor(ContextCompat.getColor(this, R.color.dad_color))
        btnMom.setBackgroundColor(ContextCompat.getColor(this, R.color.disable))
    }

    private fun enableMomButton(btnDad: Button, btnMom: Button) {
        btnDad.setBackgroundColor(ContextCompat.getColor(this, R.color.disable))
        btnMom.setBackgroundColor(ContextCompat.getColor(this, R.color.mom_color))
    }

    private fun go() {
        if (areInputNamesOK()) {
            trackEvent(TrackerConstants.Label.ParentsScreen.GO_OK)
            preferencesManager.setParentNamesSetDatetime(System.currentTimeMillis())
            preferencesManager.setParent1(getParent1())
            preferencesManager.setParent1Name(edtParent1Name.text.toString())
            preferencesManager.setParent2(getParent2())
            preferencesManager.setParent2Name(edtParent2Name.text.toString())
            openMainActivity()
        } else {
            trackEvent(TrackerConstants.Label.ParentsScreen.GO_KO)
        }
    }

    private fun openMainActivity() {
        openActivity(FindMatchesActivity::class.java)
        finish()
    }

    private fun getParent1(): String {
        var parent = Parent.MOM
        if (isButtonEnabled(btnParent1Dad)) {
            parent = Parent.DAD
        }
        return parent.value
    }

    private fun getParent2(): String {
        var parent = Parent.MOM
        if (isButtonEnabled(btnParent2Dad)) {
            parent = Parent.DAD
        }
        return parent.value
    }

    private fun isButtonEnabled(button: Button): Boolean {
        val buttonColor = button.background as ColorDrawable
        return buttonColor.color != ContextCompat.getColor(this, R.color.disable)
    }

    private fun areInputNamesOK(): Boolean {
        var areOk = true
        if (edtParent1Name.isEmpty()) {
            areOk = false
            snackbar(coordinatorParentNames, R.string.parent_names_error_first_parent_name_empty)
        } else if (edtParent2Name.isEmpty()) {
            areOk = false
            snackbar(coordinatorParentNames, R.string.parent_names_error_second_parent_name_empty)
        } else if (edtParent1Name.text.toString() == edtParent2Name.text.toString()) {
            snackbar(coordinatorParentNames, R.string.parent_names_error_same_parent_names)
            areOk = false
        }
        return areOk
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

    private fun trackPage() {
        trackerManager.sendScreen(TrackerConstants.Section.PARENTS_SETUP.value,
                TrackerConstants.Section.ParentsSetup.MAIN.value)
    }

    private fun trackEvent(label: TrackerConstants.Label.ParentsScreen) {
        trackerManager.sendEvent(
                category = TrackerConstants.Section.ParentsSetup.MAIN.value,
                action = TrackerConstants.Action.CLICK.value,
                label = label.value)
    }
}
