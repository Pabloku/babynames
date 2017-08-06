package com.kamisoft.babynames.presentation.choose_name

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.FrameLayout
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceFragment
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.extensions.gone
import com.kamisoft.babynames.commons.extensions.visible
import com.kamisoft.babynames.data.datasource.FavoritesDataFactory
import com.kamisoft.babynames.data.repository.FavoritesDataRepository
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.model.Parent
import com.kamisoft.babynames.domain.usecase.GetFavoriteList
import com.kamisoft.babynames.domain.usecase.SaveFavoriteName
import com.kamisoft.babynames.logger.Logger
import com.kamisoft.babynames.presentation.choose_name.adapter.NameItemAnimator
import com.kamisoft.babynames.presentation.choose_name.adapter.NamesAdapter
import com.kamisoft.babynames.presentation.model.BabyNameLikable
import com.rahulrav.futures.Future
import kotlinx.android.synthetic.main.fragment_choose_name.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onUiThread

/*/ TODO This is a horrible copied class from ChooseName[FirstParent]Fragment.
I think there is a bug managing fragments (or surely I am doing something wrong) when 2 consecutive
 fragments are instances of the same class. After doing a replace, and coming back to previous
 fragment of the same instance, this fragment acts as it was the second instance instead the first.
So, that's why I created this copied class, to avoid having two consecutive fragments instances of the
same class.
 */
class ChooseNameSecondParentFragment : MvpLceFragment<FrameLayout, List<BabyNameLikable>, ChooseNameView,
        ChooseNamePresenter>(), ChooseNameView {
    private var selectedGender: Gender = Gender.MALE
    private var parent: String = Parent.DAD.toString()

    private val namesAdapter: NamesAdapter = NamesAdapter({
        Logger.debug("${it.name} Clicked")
        presenter.manageBabyNameClick(parent, selectedGender, it)
    })

    companion object {
        const val ARG_GENDER = "gender"
        const val ARG_PARENT = "parent"
        lateinit var listFuture: Future<List<BabyName>>
        lateinit var searchCallback: () -> Unit
        lateinit var favoriteCallback: (favoriteCount: Int) -> Unit
        lateinit var namesListCallBack: (babyNamesLiked: List<BabyNameLikable>) -> Unit
        fun createInstance(parent: String, gender: Gender,
                           namesFuture: Future<List<BabyName>>,
                           searchCallback: () -> Unit,
                           favoriteCallback: (favoriteCount: Int) -> Unit,
                           namesListCallBack: (babyNamesLiked: List<BabyNameLikable>) -> Unit): ChooseNameSecondParentFragment {
            val fragment = ChooseNameSecondParentFragment()
            val bundle = Bundle()
            bundle.putString(ARG_GENDER, gender.toString())
            bundle.putString(ARG_PARENT, parent)
            fragment.arguments = bundle
            this.listFuture = namesFuture
            this.searchCallback = searchCallback
            this.favoriteCallback = favoriteCallback
            this.namesListCallBack = namesListCallBack
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_name, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        selectedGender = Gender.valueOf(arguments.getString(ARG_GENDER).toUpperCase())
        parent = arguments.getString(ARG_PARENT)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSearch -> {
                searchCallback.invoke()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.start(listFuture)
    }

    override fun initViews() {
        txtChooseNames.text = getString(R.string.name_list_title, parent)
        //TODO errorView is pending
        rvList.layoutManager = LinearLayoutManager(activity)
        rvList.adapter = namesAdapter
        rvList.itemAnimator = NameItemAnimator()
        btnOk.setOnClickListener { presenter.onOkClicked() }
    }

    override fun createPresenter() = ChooseNamePresenter(
            GetFavoriteList(FavoritesDataRepository(FavoritesDataFactory())),
            SaveFavoriteName(FavoritesDataRepository(FavoritesDataFactory())))

    override fun setData(nameList: List<BabyNameLikable>) {
        onUiThread {
            showLoading(false)
            namesAdapter.setBabyNameList(nameList)
            loadData(pullToRefresh = false)
        }
    }

    override fun setFavoriteList(favorites: List<String>) {
        namesAdapter.updateFavorites(favorites)
    }

    override fun loadData(pullToRefresh: Boolean) {
        presenter.loadFavorites(parent, selectedGender)
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showContent() {
        onUiThread {
            super.showContent()
        }
    }

    override fun updateFavoriteCounter(favoriteCount: Int) {
        favoriteCallback.invoke(favoriteCount)
    }

    fun findNameInList(text: String) {
        (rvList.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(namesAdapter.getFirstItemPositionStartingWith(text), 20)
    }

    override fun showNoFavoritesMessage() {
        snackbar(contentView, R.string.name_list_no_favorites)
    }

    override fun getLikedBabyNames(): List<BabyNameLikable> = namesAdapter.getLikedBabyNames()

    override fun onLikedNamesChosen() {
        namesListCallBack.invoke(getLikedBabyNames())
    }
}