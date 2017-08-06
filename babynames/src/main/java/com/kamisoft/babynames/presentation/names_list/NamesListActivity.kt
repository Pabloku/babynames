package com.kamisoft.babynames.presentation.names_list

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.data.datasource.NamesDataFactory
import com.kamisoft.babynames.data.repository.NamesDataRepository
import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.usecase.GetNameList
import com.kamisoft.babynames.presentation.names_list.adapter.NamesAdapter
import kotlinx.android.synthetic.main.activity_names_list.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class NamesListActivity : MvpLceActivity<RecyclerView, List<BabyName>, NamesListView, NamesListPresenter>(), NamesListView {

    companion object {
        const val ARG_GENDER = "gender"
    }

    private val gender: Gender by NamesListArgument(ARG_GENDER)
    private val namesAdapter: NamesAdapter = NamesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_names_list)
        presenter.start()
        presenter.loadData(gender)
    }

    override fun initViews() {
        initToolbar()
        initRecyclerView()
        when (gender) {
            Gender.FEMALE -> txtNamesLabel.text = getString(R.string.girl_names)
            Gender.MALE -> txtNamesLabel.text = getString(R.string.boy_names)
        }
    }

    override fun createPresenter() = NamesListPresenter(GetNameList(NamesDataRepository(NamesDataFactory())))

    private fun initToolbar() {
        val toolbar = toolbar as Toolbar?
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar?.setNavigationIcon(R.drawable.ic_close)
    }

    private fun initRecyclerView() {
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = namesAdapter
    }

    override fun getErrorMessage(e: Throwable?, pullToRefresh: Boolean): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(pullToRefresh: Boolean) {
        presenter.loadData(gender)
    }

    override fun setData(data: List<BabyName>?) {
        namesAdapter.setBabyNameList(data ?: emptyList())
    }

    class NamesListArgument(private val arg: String) : ReadOnlyProperty<Activity, Gender> {
        override fun getValue(thisRef: Activity, property: KProperty<*>): Gender {
            return Gender.valueOf(thisRef.intent.extras.getString(arg).toUpperCase())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
