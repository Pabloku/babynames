package com.kamisoft.babynames.presentation.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kamisoft.babyname.R
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

    fun showCoincidencesView(matches: ArrayList<String>) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.contentView, MatchesFragment.createInstance(matches))
        transaction.addToBackStack("coincidences")
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (stepperIndicator.currentStep > 0) {
            stepperIndicator.currentStep -= 1
        }
    }

}
