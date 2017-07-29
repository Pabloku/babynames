package com.kamisoft.babynames.commons.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import com.kamisoft.babyname.R

fun View.getLayoutInflater() = LayoutInflater.from(context)

fun View.showToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun View.isVisible() = visibility == View.VISIBLE

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun View.circleReveal(posFromRight: Int, containsOverflow: Boolean, isShow: Boolean) {
    var width = width

    if (posFromRight > 0)
        width -= posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2
    if (containsOverflow)
        width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)

    val cx = width
    val cy = height / 2

    val anim: Animator
    if (isShow)
        anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, width.toFloat())
    else
        anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, width.toFloat(), 0f)

    anim.duration = 220.toLong()

    // make the view invisible when the animation is done
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            if (!isShow) {
                super.onAnimationEnd(animation)
                visibility = View.INVISIBLE
            }
        }
    })

    // make the view visible and start the animation
    if (isShow)
        visible()

    // start the animation
    anim.start()
}

