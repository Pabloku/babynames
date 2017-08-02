package com.kamisoft.babynames.presentation.matches

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.choose_name.adapter.NameItemAnimator
import com.kamisoft.babynames.presentation.choose_name.adapter.NamesAdapter
import com.kamisoft.babynames.presentation.model.BabyNameLikable
import kotlinx.android.synthetic.main.fragment_choose_name.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MatchesFragment : Fragment() {

    companion object {
        const val ARG_MATCH_LIST = "matchList"
        fun createInstance(matches: ArrayList<String>): MatchesFragment {
            val fragment = MatchesFragment()
            val bundle = Bundle()
            bundle.putStringArrayList(ARG_MATCH_LIST, matches)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val nameMatchesList: ArrayList<String> by MatchesArgument(ARG_MATCH_LIST)

    private val namesAdapter: NamesAdapter = NamesAdapter {
        Logger.debug("${it.name} Clicked")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.layoutManager = LinearLayoutManager(activity)
        rvList.adapter = namesAdapter
        rvList.itemAnimator = NameItemAnimator()
        namesAdapter.setBabyNameList(nameMatchesList.map { BabyNameLikable(name = it, origin = "", meaning = "", liked = false) }) //TODO Maybe I should use a different model here
    }

    class MatchesArgument(private val arg: String) : ReadOnlyProperty<Fragment, ArrayList<String>> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): ArrayList<String> {
            return thisRef.arguments.getStringArrayList(arg)
        }
    }
}