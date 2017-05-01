package com.kamisoft.babynames.presentation.choose_name

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceFragment
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.hide
import com.kamisoft.babynames.commons.show
import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.data.repository.NamesDataRepository
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.choose_name.adapter.NameItemAnimator
import com.kamisoft.babynames.presentation.choose_name.adapter.NamesAdapter
import kotlinx.android.synthetic.main.fragment_choose_name.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ChooseNameFragment : MvpLceFragment<SwipeRefreshLayout, List<BabyName>, ChooseNameView,
        ChooseNamePresenter>(), ChooseNameView {

    private val selectedGender: NamesDataSource.Gender by GenderArgument(ARG_GENDER)

    private val namesAdapter: NamesAdapter = NamesAdapter {
        Logger.debug("${it.name} Clicked")
        presenter.manageBabyNameClick(it)
    }

    companion object {
        const val ARG_GENDER = "gender"
        fun createInstance(gender: NamesDataSource.Gender): ChooseNameFragment {
            val fragment = ChooseNameFragment()
            val bundle = Bundle()
            bundle.putString(ARG_GENDER, gender.toString())
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_name, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO errorView is pending
        rvList.layoutManager = LinearLayoutManager(activity)
        rvList.adapter = namesAdapter
        rvList.itemAnimator = NameItemAnimator()
        showLoading(false)
    }

    override fun onStart() {
        super.onStart()
        presenter.loadNames(selectedGender)
    }

    override fun createPresenter() = ChooseNamePresenter(GetNameList(NamesDataRepository(NamesDataFactory())))

    override fun setData(nameList: List<BabyName>) {
        namesAdapter.setBabyNameNameList(nameList)
    }

    override fun loadData(pullToRefresh: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading(pullToRefresh: Boolean) {
        super.showLoading(pullToRefresh)
        loadingView.show()
        contentView.hide()
    }

    override fun showContent() {
        super.showContent()
        loadingView.hide()
        contentView.show()
    }

    class GenderArgument(private val name: String) : ReadOnlyProperty<Fragment, NamesDataSource.Gender> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): NamesDataSource.Gender {
            return NamesDataSource.Gender.valueOf(thisRef.arguments.getString(name).toUpperCase())
        }
    }

}