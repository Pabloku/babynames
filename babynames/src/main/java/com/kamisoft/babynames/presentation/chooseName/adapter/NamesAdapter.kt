package com.kamisoft.babynames.presentation.chooseName.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import com.kamisoft.babynames.domain.model.BabyName
import kotlinx.android.synthetic.main.row_name.view.*

class NamesAdapter(val listener: (BabyName) -> Unit) : RecyclerView.Adapter<NamesAdapter.ViewHolder>() {

    val ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button"
    private var nameList: List<BabyName> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_name, parent, false)
        val viewHolder = ViewHolder(view)
        setupClickableViews(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(nameList[position], listener)

    override fun getItemCount() = nameList.size

    fun setBabyNameNameList(names: List<BabyName>) {
        this.nameList = names
        notifyDataSetChanged()
    }

    private fun setupClickableViews(viewHolder: ViewHolder) {
        viewHolder.itemView.btnLike.setOnClickListener {
            notifyItemChanged(viewHolder.adapterPosition, ACTION_LIKE_BUTTON_CLICKED)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(babyName: BabyName, listener: (BabyName) -> Unit) = with(itemView) {
            txtName.text = babyName.name
            if (babyName.liked) {
                btnLike.setImageResource(R.drawable.ic_heart_red)
            } else {
                btnLike.setImageResource(R.drawable.ic_heart_outline_grey)
            }
            setOnClickListener {
                notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED)
                listener(babyName)
            }
        }
    }
}