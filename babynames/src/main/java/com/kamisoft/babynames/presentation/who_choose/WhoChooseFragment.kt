package com.kamisoft.babynames.presentation.who_choose

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.domain.model.Parent
import kotlinx.android.synthetic.main.fragment_who_choose.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class WhoChooseFragment : Fragment() {

    private val parentPosition: Int by ParentArgument(ARG_PARENT_POSITION)

    lateinit var callBack: (Parent) -> Unit

    companion object {
        const val ARG_PARENT_POSITION = "parentPosition"
        fun createInstance(parentPosition: Int): WhoChooseFragment {
            val fragment = WhoChooseFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_PARENT_POSITION, parentPosition)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_who_choose, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnDad.setOnClickListener { callBack.invoke(Parent.DAD) }
        btnMom.setOnClickListener { callBack.invoke(Parent.MOM) }

        if (parentPosition == 1) {
            txtWhoChoose.setText(R.string.who_choose_first)
        } else {
            txtWhoChoose.setText(R.string.who_choose_second)
        }
    }

    class ParentArgument(private val arg: String) : ReadOnlyProperty<Fragment, Int> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): Int {
            return thisRef.arguments.getInt(arg)
        }
    }
}
