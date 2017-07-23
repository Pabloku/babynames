package com.kamisoft.babynames.presentation.choose_gender

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.data.datasource.NamesDataSource
import kotlinx.android.synthetic.main.fragment_choose_gender.*

class ChooseGenderFragment : Fragment() {

    lateinit var callBack: (NamesDataSource.Gender) -> Unit

    companion object {
        fun createInstance() = ChooseGenderFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_gender, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBoy.setOnClickListener { callBack.invoke(NamesDataSource.Gender.MALE) }
        btnGirl.setOnClickListener { callBack.invoke(NamesDataSource.Gender.FEMALE) }
    }
}