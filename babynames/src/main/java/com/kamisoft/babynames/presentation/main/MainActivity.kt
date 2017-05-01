package com.kamisoft.babynames.presentation.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.presentation.choose_gender.ChooseGenderFragment
import com.kamisoft.babynames.presentation.choose_name.ChooseNameFragment
import com.kamisoft.babynames.presentation.who_choose.WhoChooseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        ChooseGenderFragment.ChooseGenderListener,
        WhoChooseFragment.WhoChooseListener,
        ChooseNameFragment.ChooseNamesListener {
    lateinit var gender: NamesDataSource.Gender

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
            showWhoChooseView(2)
        } else {
            //TODO show coincidences fragment!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showChooseGenderView()
        stepperIndicator.stepCount = 5
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (stepperIndicator.currentStep > 0) {
            stepperIndicator.currentStep -= 1
        }
    }

}
