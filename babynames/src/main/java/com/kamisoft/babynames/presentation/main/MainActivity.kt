package com.kamisoft.babynames.presentation.main

import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.presentation.chooseGender.ChooseGenderFragment
import com.kamisoft.babynames.presentation.chooseName.ChooseNameFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView,
        ChooseGenderFragment.ChooseGenderListener {

    override fun onGenderSelected(gender: NamesDataSource.Gender) {
        showChooseNameView(gender)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showChooseGenderView()
        stepperIndicator.stepCount = 4

    }

    override fun createPresenter() = MainPresenter()

    override fun showChooseGenderView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentView, ChooseGenderFragment.createInstance())
        transaction.commit()
    }

    override fun showChooseNameView(gender: NamesDataSource.Gender) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.contentView, ChooseNameFragment.createInstance(gender))
        transaction.addToBackStack("chooseName")
        transaction.commit()
        stepperIndicator.currentStep = 1
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (stepperIndicator.currentStep > 0) {
            stepperIndicator.currentStep -= 1
        }
    }

}
