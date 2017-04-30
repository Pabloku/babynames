package com.kamisoft.babynames.presentation.chooseName

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceFragment
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.hide
import com.kamisoft.babynames.commons.show
import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.data.repository.NamesDataRepository
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.logger.Logger
import kotlinx.android.synthetic.main.fragment_choose_name.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ChooseNameFragment : MvpLceFragment<RelativeLayout, List<String>, ChooseNameView,
        ChooseNamePresenter>(), ChooseNameView {

    private val selectedGender: NamesDataSource.Gender by GenderArgument(ARG_GENDER)

    private val namesAdapter: NamesAdapter = NamesAdapter {
        Logger.debug("$it Clicked")
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
        rvList.itemAnimator = FeedItemAnimator()
        showLoading(false)
    }

    override fun onStart() {
        super.onStart()
        presenter.loadNames(selectedGender)
    }

    override fun createPresenter() = ChooseNamePresenter(GetNameList(NamesDataRepository(NamesDataFactory())))

    override fun setData(nameList: List<String>) {
        namesAdapter.setNameList(nameList)
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