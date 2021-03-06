package com.kamisoft.babynames.presentation.matches.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babynames.R
import com.kamisoft.babynames.commons.extensions.invisible
import com.kamisoft.babynames.domain.model.BabyName
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import kotlinx.android.synthetic.main.row_name.view.*

class MatchedNamesAdapter(private val nameList: List<BabyName>) : RecyclerView.Adapter<MatchedNamesAdapter.ViewHolder>(),
        FastScrollRecyclerView.SectionedAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_name, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(nameList[position])

    override fun getItemCount() = nameList.size

    private fun getInitialLetter(name: String) = name[0].toString()

    override fun getSectionName(position: Int): String = getInitialLetter(nameList[position].name)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(babyName: BabyName) = with(itemView) {
            btnLike.invisible()
            txtName.text = babyName.name
            if (!TextUtils.isEmpty(babyName.origin)) {
                txtOrigin.text = txtOrigin.context.getString(R.string.origin, babyName.origin)
            } else {
                txtOrigin.text = ""
            }
            txtMeaning.text = babyName.meaning
        }
    }
}