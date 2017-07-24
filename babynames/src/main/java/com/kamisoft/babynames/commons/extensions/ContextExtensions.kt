package com.kamisoft.babynames.commons.extensions

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

private var screenWidth = 0
private var screenHeight = 0

fun Context.getScreenHeight(): Int {
    if (screenHeight == 0) {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenHeight = size.y
    }
    return screenHeight
}

fun Context.getScreenWidth(): Int {
    if (screenWidth == 0) {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
    }

    return screenWidth
}