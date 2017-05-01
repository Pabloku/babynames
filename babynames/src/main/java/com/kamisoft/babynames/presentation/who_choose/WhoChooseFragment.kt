package com.kamisoft.babynames.presentation.who_choose

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.domain.model.Parent
import kotlinx.android.synthetic.main.fragment_who_choose.*


class WhoChooseFragment : Fragment() {

    interface WhoChooseListener {
        fun onWhoSelected(parent: Parent)
    }

    var callBack: WhoChooseListener? = null

    companion object {
        fun createInstance() = WhoChooseFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_who_choose, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnDad.setOnClickListener { callBack?.onWhoSelected(Parent.DAD) }
        btnMom.setOnClickListener { callBack?.onWhoSelected(Parent.MOM) }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context !is WhoChooseListener) {
            throw IllegalStateException("The attaching activity has to implement ${WhoChooseListener::class.java.canonicalName}")
        }
        callBack = context
    }
}
