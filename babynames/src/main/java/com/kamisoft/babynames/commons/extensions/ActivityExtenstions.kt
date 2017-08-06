package com.kamisoft.babynames.commons.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntegerRes
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast


fun Activity.openActivity(javaClass: Class<*>) = startActivity(Intent(this, javaClass))

fun Activity.openActivity(javaClass: Class<*>, params: Bundle) =
        startActivity(Intent(this, javaClass).putExtras(params))

fun Activity.openActivityForResult(javaClass: Class<*>, params: Bundle, requestCode: Int) =
        startActivityForResult(Intent(this, javaClass).putExtras(params), requestCode)

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Activity.toast(@IntegerRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(messageRes), duration)
}

fun Activity.snackbar(view: View, @IntegerRes messageRes: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, messageRes, duration).show()
}