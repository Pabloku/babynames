package com.kamisoft.babynames.commons

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

fun View.getLayoutInflater(): LayoutInflater {
    return LayoutInflater.from(context)
}

fun View.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

/*
fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}*/
