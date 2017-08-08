package com.kamisoft.babynames.presentation.choose_gender

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.tracking.TrackerConstants
import com.kamisoft.babynames.tracking.TrackerManager
import kotlinx.android.synthetic.main.fragment_choose_gender.*

class ChooseGenderFragment : Fragment() {

    private val trackerManager by lazy { TrackerManager(activity) }

    companion object {
        lateinit var genderSelectCallBack: (Gender) -> Unit
        fun createInstance(genderSelectCallBack: (Gender) -> Unit): ChooseGenderFragment {
            this.genderSelectCallBack = genderSelectCallBack
            return ChooseGenderFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_gender, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackPage()
        btnBoy.setOnClickListener {
            trackEvent(TrackerConstants.Label.ChooseGenderScreen.BOY)
            genderSelectCallBack.invoke(Gender.MALE)
        }
        btnGirl.setOnClickListener {
            trackEvent(TrackerConstants.Label.ChooseGenderScreen.GIRL)
            genderSelectCallBack.invoke(Gender.FEMALE)
        }
    }

    private fun trackPage() {
        trackerManager.sendScreen(TrackerConstants.Section.FIND_MATCHES.value,
                TrackerConstants.Section.FindMatches.CHOOSE_GENDER.value)
    }

    private fun trackEvent(label: TrackerConstants.Label.ChooseGenderScreen) {
        trackerManager.sendEvent(
                category = TrackerConstants.Section.ParentsSetup.MAIN.value,
                action = TrackerConstants.Action.CLICK.value,
                label = label.value)
    }
}