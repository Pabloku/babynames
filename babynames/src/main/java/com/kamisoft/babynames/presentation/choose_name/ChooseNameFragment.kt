package com.kamisoft.babynames.presentation.choose_name

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
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
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ChooseNameFragment : MvpLceFragment<SwipeRefreshLayout, List<BabyName>, ChooseNameView,
        ChooseNamePresenter>(), ChooseNameView {
    private var selectedGender: NamesDataSource.Gender = NamesDataSource.Gender.MALE
    private var parent: String = Parent.DAD.toString()
    private val parentPosition: Int by ParentPositionArgument(ARG_PARENT_POSITION)

    private val namesAdapter: NamesAdapter = NamesAdapter({
        Logger.debug("${it.name} Clicked")
        presenter.manageBabyNameClick(parent + parentPosition, selectedGender, it)
    })

    interface ChooseNamesListener {
        fun onNamesSelected(babyNamesLiked: List<BabyName>, parentPosition: Int)
    }

    var callBack: ChooseNamesListener? = null

    companion object {
        const val ARG_GENDER = "gender"
        const val ARG_PARENT = "parent"
        const val ARG_PARENT_POSITION = "parentPosition"
        fun createInstance(parent: String, gender: NamesDataSource.Gender, parentPosition: Int): ChooseNameFragment {
            val fragment = ChooseNameFragment()
            val bundle = Bundle()
            bundle.putString(ARG_GENDER, gender.toString())
            bundle.putString(ARG_PARENT, parent)
            bundle.putInt(ARG_PARENT_POSITION, parentPosition)
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
            callBack?.onNamesSelected(presenter.getLikedBabyNames(namesAdapter.getBabyNameList()), parentPosition)
        }
        showLoading(false)
    }

    override fun onStart() {
        super.onStart()
        presenter.loadNames(selectedGender)
    }

    //TODO Clean that parent + parentPosition
    override fun createPresenter() = ChooseNamePresenter(
            GetNameList(NamesDataRepository(NamesDataFactory())),
            GetFavoriteList(FavoritesDataRepository(FavoritesDataFactory())),
            SaveFavoriteName(FavoritesDataRepository(FavoritesDataFactory())))

    override fun setData(nameList: List<BabyName>) {
        showLoading(false)
        namesAdapter.setBabyNameList(nameList)
        presenter.loadFavorites(parent + parentPosition, selectedGender)
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

    fun updateListByGender(gender: NamesDataSource.Gender) {
        showLoading(false)
        this.selectedGender = gender
        presenter.loadNames(selectedGender)
    }

    fun updateParent(parent: String) {
        this.parent = parent
        presenter.loadFavorites(parent + parentPosition, selectedGender)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context !is ChooseNamesListener) {
            throw IllegalStateException("The attaching activity has to implement ${ChooseNamesListener::class.java.canonicalName}")
        }
        callBack = context
    }

    class ParentPositionArgument(private val arg: String) : ReadOnlyProperty<Fragment, Int> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): Int {
            return thisRef.arguments.getInt(arg)
        }
    }

}