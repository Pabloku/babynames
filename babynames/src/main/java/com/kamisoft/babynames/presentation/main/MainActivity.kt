package com.kamisoft.babynames.presentation.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.presentation.choose_gender.ChooseGenderFragment
import com.kamisoft.babynames.presentation.choose_name.ChooseNameFragment
import com.kamisoft.babynames.presentation.who_choose.WhoChooseFragment
import kotlinx.android.synthetic.main.activity_main.*

//TODO Mvp makes sense here in this static activity?
class MainActivity : AppCompatActivity(),
        ChooseGenderFragment.ChooseGenderListener, WhoChooseFragment.WhoChooseListener {

    lateinit var gender: NamesDataSource.Gender

    override fun onGenderSelected(gender: NamesDataSource.Gender) {
        this.gender = gender
        showWhoChooseView()
    }

    override fun onWhoSelected(parent: Parent) {
        showChooseNameView(this.gender)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showChooseGenderView()
        stepperIndicator.stepCount = 4

    }

    fun showChooseGenderView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentView, ChooseGenderFragment.createInstance())
        transaction.commit()
    }

    fun showWhoChooseView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.contentView, WhoChooseFragment.createInstance())
        transaction.addToBackStack("whoChoose")
        transaction.commit()
        stepperIndicator.currentStep = 1
    }

    fun showChooseNameView(gender: NamesDataSource.Gender) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.contentView, ChooseNameFragment.createInstance(gender))
        transaction.addToBackStack("chooseName")
        transaction.commit()
        stepperIndicator.currentStep = 2
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (stepperIndicator.currentStep > 0) {
            stepperIndicator.currentStep -= 1
        }
    }

}
