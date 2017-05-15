package com.kamisoft.babynames.presentation.choose_name.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.domain.model.BabyName
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import kotlinx.android.synthetic.main.row_name.view.*

class NamesAdapter(val listener: (BabyName) -> Unit) : RecyclerView.Adapter<NamesAdapter.ViewHolder>(),
        FastScrollRecyclerView.SectionedAdapter {

    companion object {
        val ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button"
        val ACTION_UNLIKE_BUTTON_CLICKED = "action_unlike_button_button"
    }

    private var nameList: List<BabyName> = emptyList()
    private var favoriteList: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_name, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(nameList[position], listener)

    override fun getItemCount() = nameList.size

    fun setBabyNameList(names: List<BabyName>) {
        this.nameList = names
        notifyDataSetChanged()
    }

    fun updateFavorites(favorites: List<String>) {
        this.favoriteList = favorites
        this.favoriteList.forEach {
            val favorite = it
            val babyName = nameList.find { it.name == favorite }
            babyName?.liked = true
            notifyItemChanged(nameList.indexOf(babyName))
        }
    }

    fun getBabyNameList() = nameList

    private fun getInitialLetter(name: String) = name[0].toString()

    override fun getSectionName(position: Int): String = getInitialLetter(nameList[position].name)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(babyName: BabyName, listener: (BabyName) -> Unit) = with(itemView) {
            txtName.text = babyName.name
            if (!TextUtils.isEmpty(babyName.origin)) {
                txtOrigin.text = txtOrigin.context.getString(R.string.origin, babyName.origin)
            } else {
                txtOrigin.text = ""
            }
            txtMeaning.text = babyName.meaning

            if (babyName.liked) {
                btnLike.setImageResource(R.drawable.ic_heart_red)
            } else {
                btnLike.setImageResource(R.drawable.ic_heart_outline_grey)
            }
            setOnClickListener {
                if (!babyName.liked) {
                    notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED)
                } else {
                    notifyItemChanged(adapterPosition, ACTION_UNLIKE_BUTTON_CLICKED)
                }
                listener(babyName)
            }
        }
    }

    fun getFirstItemPositionStartingWith(text: String) = nameList.indexOfFirst {
        it.name.toLowerCase().startsWith(text.toLowerCase())
    }
}