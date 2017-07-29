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

    companion object {
        lateinit var genderSelectCallBack: (NamesDataSource.Gender) -> Unit
        fun createInstance(genderSelectCallBack: (NamesDataSource.Gender) -> Unit): ChooseGenderFragment {
            this.genderSelectCallBack = genderSelectCallBack
            return ChooseGenderFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_gender, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBoy.setOnClickListener { genderSelectCallBack.invoke(NamesDataSource.Gender.MALE) }
        btnGirl.setOnClickListener { genderSelectCallBack.invoke(NamesDataSource.Gender.FEMALE) }
    }
}