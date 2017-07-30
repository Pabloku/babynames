package com.kamisoft.babynames.presentation.parent_names

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.extensions.isEmpty
import com.kamisoft.babynames.commons.extensions.openActivity
import com.kamisoft.babynames.commons.extensions.snackbar
import com.kamisoft.babynames.commons.shared_preferences.AndroidPrefsManager
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.presentation.find_matches.FindMatchesActivity
import kotlinx.android.synthetic.main.activity_parent_names.*


class ParentNamesActivity : AppCompatActivity() {

    private val preferencesManager by lazy { AndroidPrefsManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_names)
        if (!isFirstUse()) {
            openMainActivity()
        } else {
            initViews()
        }
    }

    private fun isFirstUse() = preferencesManager.getFirstUseInMillis() == 0L

    private fun initViews() {
        btnParent1Dad.setOnClickListener { enableDadButton(btnDad = it as Button, btnMom = btnParent1Mom) }
        btnParent1Mom.setOnClickListener { enableMomButton(btnDad = btnParent1Dad, btnMom = it as Button) }
        btnParent2Dad.setOnClickListener { enableDadButton(btnDad = it as Button, btnMom = btnParent2Mom) }
        btnParent2Mom.setOnClickListener { enableMomButton(btnDad = btnParent2Dad, btnMom = it as Button) }
        btnGo.setOnClickListener { go() }
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
            preferencesManager.saveFirstUse(System.currentTimeMillis())
            preferencesManager.setParent1(getParent1())
            preferencesManager.setParent1Name(edtParent1Name.text.toString())
            preferencesManager.setParent2(getParent2())
            preferencesManager.setParent2Name(edtParent2Name.text.toString())
            openMainActivity()
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
}
