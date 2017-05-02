package com.kamisoft.babynames.commons

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

fun View.getLayoutInflater() =
    LayoutInflater.from(context)

fun View.showToast(message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun View.isVisible() = visibility == View.VISIBLE

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

