package com.kamisoft.babynames.presentation.choose_name

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceFragment
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.hide
import com.kamisoft.babynames.commons.show
import com.kamisoft.babynames.data.datasource.FavoritesDataFactory
import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.datasource.NamesDataSource
import com.kamisoft.babynames.data.repository.FavoritesDataRepository
import com.kamisoft.babynames.data.repository.NamesDataRepository
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.domain.usecase.GetFavoriteList
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.domain.usecase.SaveFavoriteName
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.choose_name.adapter.NameItemAnimator
import com.kamisoft.babynames.presentation.choose_name.adapter.NamesAdapter
import kotlinx.android.synthetic.main.fragment_choose_name.*

class ChooseNameFragment : MvpLceFragment<SwipeRefreshLayout, List<BabyName>, ChooseNameView,
        ChooseNamePresenter>(), ChooseNameView {
    private var selectedGender: NamesDataSource.Gender = NamesDataSource.Gender.MALE
    private var parent: String = Parent.DAD.toString()

    private val namesAdapter: NamesAdapter = NamesAdapter({
        Logger.debug("${it.name} Clicked")
        presenter.manageBabyNameClick(parent, selectedGender, it)
    })

    lateinit var callBack: (babyNamesLiked: List<BabyName>) -> Unit

    companion object {
        const val ARG_GENDER = "gender"
        const val ARG_PARENT = "parent"
        fun createInstance(parent: String, gender: NamesDataSource.Gender): ChooseNameFragment {
            val fragment = ChooseNameFragment()
            val bundle = Bundle()
            bundle.putString(ARG_GENDER, gender.toString())
            bundle.putString(ARG_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_name, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        selectedGender = NamesDataSource.Gender.valueOf(arguments.getString(ARG_GENDER).toUpperCase())
        parent = arguments.getString(ARG_PARENT)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO errorView is pending
        rvList.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        rvList.adapter = namesAdapter
        rvList.itemAnimator = NameItemAnimator()
        btnOk.setOnClickListener {
            callBack.invoke(presenter.getLikedBabyNames(namesAdapter.getBabyNameList()))
        }
        showLoading(false)
    }

    override fun onStart() {
        super.onStart()
        presenter.loadNames(selectedGender)
    }

    override fun createPresenter() = ChooseNamePresenter(
            GetNameList(NamesDataRepository(NamesDataFactory())),
            GetFavoriteList(FavoritesDataRepository(FavoritesDataFactory())),
            SaveFavoriteName(FavoritesDataRepository(FavoritesDataFactory())))

    override fun setData(nameList: List<BabyName>) {
        showLoading(false)
        namesAdapter.setBabyNameList(nameList)
        presenter.loadFavorites(parent, selectedGender)
    }

    override fun setFavoriteList(favorites: List<String>) {
        namesAdapter.updateFavorites(favorites)
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

    fun findNameInList(text: String) {
        (rvList.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(namesAdapter.getFirstItemPositionStartingWith(text), 20)
    }
}