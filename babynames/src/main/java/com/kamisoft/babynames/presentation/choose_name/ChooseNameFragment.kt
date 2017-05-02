package com.kamisoft.babynames.presentation.choose_name

import android.content.Context
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
import com.kamisoft.babynames.presentation.who_choose.WhoChooseFragment
import kotlinx.android.synthetic.main.fragment_choose_name.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ChooseNameFragment : MvpLceFragment<SwipeRefreshLayout, List<BabyName>, ChooseNameView,
        ChooseNamePresenter>(), ChooseNameView {

    private val selectedGender: NamesDataSource.Gender by GenderArgument(ARG_GENDER)
    private val parentPosition: Int by ParentArgument(WhoChooseFragment.ARG_PARENT_POSITION)

    private val namesAdapter: NamesAdapter = NamesAdapter {
        Logger.debug("${it.name} Clicked")
        presenter.manageBabyNameClick(it)
    }

    interface ChooseNamesListener {
        fun onNamesSelected(babyNamesLiked: List<BabyName>, parentPosition: Int)
    }

    var callBack: ChooseNamesListener? = null

    companion object {
        const val ARG_GENDER = "gender"
        const val ARG_PARENT_POSITION = "parentPosition"
        fun createInstance(gender: NamesDataSource.Gender, parentPosition: Int): ChooseNameFragment {
            val fragment = ChooseNameFragment()
            val bundle = Bundle()
            bundle.putString(ARG_GENDER, gender.toString())
            bundle.putInt(ARG_PARENT_POSITION, parentPosition)
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
        btnOk.setOnClickListener {
            callBack?.onNamesSelected(presenter.getLikedBabyNames(namesAdapter.getBabyNameList()), parentPosition)
        }
        showLoading(false)
    }

    override fun onStart() {
        super.onStart()
        presenter.loadNames(selectedGender)
    }

    override fun createPresenter() = ChooseNamePresenter(GetNameList(NamesDataRepository(NamesDataFactory())))

    override fun setData(nameList: List<BabyName>) {
        namesAdapter.setBabyNameList(nameList)
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context !is ChooseNamesListener) {
            throw IllegalStateException("The attaching activity has to implement ${ChooseNamesListener::class.java.canonicalName}")
        }
        callBack = context
    }

    class GenderArgument(private val arg: String) : ReadOnlyProperty<Fragment, NamesDataSource.Gender> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): NamesDataSource.Gender {
            return NamesDataSource.Gender.valueOf(thisRef.arguments.getString(arg).toUpperCase())
        }
    }

    class ParentArgument(private val arg: String) : ReadOnlyProperty<Fragment, Int> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): Int {
            return thisRef.arguments.getInt(arg)
        }
    }

}