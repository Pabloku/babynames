package com.kamisoft.babynames.presentation.chooseName

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamisoft.babyname.R
import kotlinx.android.synthetic.main.row_name.view.*

class NamesAdapter(val listener: (String) -> Unit) : RecyclerView.Adapter<NamesAdapter.ViewHolder>() {

    val ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button"
    private var nameList: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_name, parent, false)
        val viewHolder = ViewHolder(view)
        setupClickableViews(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(nameList[position], listener)

    override fun getItemCount() = nameList.size

    fun setNameList(names: List<String>) {
        this.nameList = names
        notifyDataSetChanged()
    }

    private fun setupClickableViews(viewHolder: ViewHolder) {
        viewHolder.itemView.btnLike.setOnClickListener {
            notifyItemChanged(viewHolder.adapterPosition, ACTION_LIKE_BUTTON_CLICKED)
        }
        viewHolder.itemView.setOnClickListener {
            notifyItemChanged(viewHolder.adapterPosition, ACTION_LIKE_BUTTON_CLICKED) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: String, listener: (String) -> Unit) = with(itemView) {
            txtName.text = item
            btnLike.setImageResource(R.drawable.ic_heart_outline_grey)
            //setOnClickListener { listener(item) }
        }
    }
}