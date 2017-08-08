package com.kamisoft.babynames.presentation.choose_parent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.extensions.gone
import com.kamisoft.babynames.commons.extensions.visible
import com.kamisoft.babynames.commons.shared_preferences.AndroidPrefsManager
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.tracking.TrackerConstants
import com.kamisoft.babynames.tracking.TrackerManager
import kotlinx.android.synthetic.main.fragment_who_choose.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ChooseParentFragment : Fragment() {

    private val preferencesManager by lazy { AndroidPrefsManager(activity) }
    private val trackerManager by lazy { TrackerManager(activity) }

    private val parentPosition: Int by ParentArgument(ARG_PARENT_POSITION)

    companion object {
        const val ARG_PARENT_POSITION = "parentPosition"

        lateinit var parentSelectCallBack: (String, String) -> Unit
        fun createInstance(parentPosition: Int,
                           parentSelectCallBack: (String, String) -> Unit): ChooseParentFragment {
            val fragment = ChooseParentFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_PARENT_POSITION, parentPosition)
            fragment.arguments = bundle
            this.parentSelectCallBack = parentSelectCallBack
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_who_choose, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackPage()

        if (parentPosition == 1) {
            txtWhoChoose.setText(R.string.who_choose_first)
        } else {
            txtWhoChoose.setText(R.string.who_choose_second)
        }

        initViews()
    }

    private fun initViews() {
        initParent1Buttons()
        initParent2Buttons()
    }

    private fun initParent1Buttons() {
        val parent1 = preferencesManager.getParent1()
        val parent1Name = preferencesManager.getParent1Name()
        val parent2Name = preferencesManager.getParent2Name()

        if (Parent.valueOf(parent1.toUpperCase()) == Parent.DAD) {
            btnParent1Dad.visible()
            btnParent1Mom.gone()
            btnParent1Dad.text = getString(R.string.dad_name, parent1Name)
        } else {
            btnParent1Mom.visible()
            btnParent1Dad.gone()
            btnParent1Mom.text = getString(R.string.mom_name, parent1Name)
        }

        val firstParentToChoose = parent1Name
        val secondParentToChoose = parent2Name
        btnParent1Dad.setOnClickListener {
            trackEvent(TrackerConstants.Label.WhoChooseScreen.PARENT1_DAD)
            parentSelectCallBack.invoke(firstParentToChoose, secondParentToChoose)
        }
        btnParent1Mom.setOnClickListener {
            trackEvent(TrackerConstants.Label.WhoChooseScreen.PARENT1_MOM)
            parentSelectCallBack.invoke(firstParentToChoose, secondParentToChoose)
        }
    }

    private fun initParent2Buttons() {
        val parent2 = preferencesManager.getParent2()
        val parent1Name = preferencesManager.getParent1Name()
        val parent2Name = preferencesManager.getParent2Name()

        if (Parent.valueOf(parent2.toUpperCase()) == Parent.DAD) {
            btnParent2Dad.visible()
            btnParent2Mom.gone()
            btnParent2Dad.text = getString(R.string.dad_name, parent2Name)
        } else {
            btnParent2Mom.visible()
            btnParent2Dad.gone()
            btnParent2Mom.text = getString(R.string.mom_name, parent2Name)
        }
        val firstParentToChoose = parent2Name
        val secondParentToChoose = parent1Name
        btnParent2Dad.setOnClickListener {
            trackEvent(TrackerConstants.Label.WhoChooseScreen.PARENT2_DAD)
            parentSelectCallBack.invoke(firstParentToChoose, secondParentToChoose)
        }
        btnParent2Mom.setOnClickListener {
            trackEvent(TrackerConstants.Label.WhoChooseScreen.PARENT2_MOM)
            parentSelectCallBack.invoke(firstParentToChoose, secondParentToChoose)
        }
    }

    class ParentArgument(private val arg: String) : ReadOnlyProperty<Fragment, Int> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): Int {
            return thisRef.arguments.getInt(arg)
        }
    }

    private fun trackPage() {
        trackerManager.sendScreen(TrackerConstants.Section.FIND_MATCHES.value,
                TrackerConstants.Section.FindMatches.WHO_CHOOSE.value)
    }

    private fun trackEvent(label: TrackerConstants.Label.WhoChooseScreen) {
        trackerManager.sendEvent(
                category = TrackerConstants.Section.ParentsSetup.MAIN.value,
                action = TrackerConstants.Action.CLICK.value,
                label = label.value)
    }
}
