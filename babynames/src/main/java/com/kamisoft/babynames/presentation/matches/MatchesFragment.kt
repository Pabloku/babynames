package com.kamisoft.babynames.presentation.matches

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.presentation.choose_name.adapter.NameItemAnimator
import com.kamisoft.babynames.presentation.matches.adapter.MatchedNamesAdapter
import kotlinx.android.synthetic.main.fragment_choose_name.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MatchesFragment : Fragment() {

    companion object {
        const val ARG_MATCH_LIST = "matchList"
        fun createInstance(matches: HashMap<String, String>): MatchesFragment {
            val fragment = MatchesFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_MATCH_LIST, matches)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val nameMatchesList: HashMap<String, String> by MatchesArgument(ARG_MATCH_LIST)

    private val namesAdapter by lazy { MatchedNamesAdapter(nameMatchesList.map { BabyName(name = it.key, origin = it.value.split("|").first(), meaning = it.value.split("|")[1]) }) } //TODO Maybe I should use a different model here

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.layoutManager = LinearLayoutManager(activity)
        rvList.adapter = namesAdapter
        rvList.itemAnimator = NameItemAnimator()
    }

    class MatchesArgument(private val arg: String) : ReadOnlyProperty<Fragment, HashMap<String, String>> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): HashMap<String, String> {
            return thisRef.arguments.getSerializable(arg) as HashMap<String, String>
        }
    }
}