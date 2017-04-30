package com.kamisoft.babynames.presentation.chooseName.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.kamisoft.babyname.R
import com.kamisoft.babynames.commons.getScreenHeight
import kotlinx.android.synthetic.main.row_name.view.*
import java.util.*

class NameItemAnimator : DefaultItemAnimator() {

    internal var likeAnimationsMap: Map<RecyclerView.ViewHolder, AnimatorSet> = HashMap()
    internal var heartAnimationsMap: MutableMap<RecyclerView.ViewHolder, AnimatorSet> = HashMap()

    private var lastAddAnimatedItem = -2

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun recordPreLayoutInformation(state: RecyclerView.State,
                                            viewHolder: RecyclerView.ViewHolder,
                                            changeFlags: Int, payloads: List<Any>): ItemHolderInfo {
        if (changeFlags == RecyclerView.ItemAnimator.FLAG_CHANGED) {
            payloads
                    .filterIsInstance<String>()
                    .forEach { return FeedItemHolderInfo(it) }
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
    }

    override fun animateAdd(viewHolder: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.layoutPosition > lastAddAnimatedItem) {
            lastAddAnimatedItem++
            runEnterAnimation(viewHolder as NamesAdapter.ViewHolder)
            return false
        }

        dispatchAddFinished(viewHolder)
        return false
    }

    private fun runEnterAnimation(holder: NamesAdapter.ViewHolder) {
        val screenHeight = holder.itemView.context.getScreenHeight()
        holder.itemView.translationY = screenHeight.toFloat()
        holder.itemView.animate()
                .translationY(0f)
                .setInterpolator(DecelerateInterpolator(3f))
                .setDuration(700)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        dispatchAddFinished(holder)
                    }
                })
                .start()
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder,
                               newHolder: RecyclerView.ViewHolder,
                               preInfo: ItemHolderInfo,
                               postInfo: ItemHolderInfo): Boolean {
        cancelCurrentAnimationIfExists(newHolder)

        if (preInfo is FeedItemHolderInfo) {
            val holder = newHolder as NamesAdapter.ViewHolder
            animateHeartButton(holder)
        }

        return false
    }

    private fun cancelCurrentAnimationIfExists(item: RecyclerView.ViewHolder) {
        if (likeAnimationsMap.containsKey(item)) {
            likeAnimationsMap[item]?.cancel()
        }
        if (heartAnimationsMap.containsKey(item)) {
            heartAnimationsMap[item]?.cancel()
        }
    }

    private fun animateHeartButton(holder: NamesAdapter.ViewHolder) {
        val animatorSet = AnimatorSet()

        val rotationAnim = ObjectAnimator.ofFloat(holder.itemView.btnLike, "rotation", 0f, 360f)
        rotationAnim.duration = 300
        rotationAnim.interpolator = ACCELERATE_INTERPOLATOR

        val bounceAnimX = ObjectAnimator.ofFloat(holder.itemView.btnLike, "scaleX", 0.2f, 1f)
        bounceAnimX.duration = 300
        bounceAnimX.interpolator = OVERSHOOT_INTERPOLATOR

        val bounceAnimY = ObjectAnimator.ofFloat(holder.itemView.btnLike, "scaleY", 0.2f, 1f)
        bounceAnimY.duration = 300
        bounceAnimY.interpolator = OVERSHOOT_INTERPOLATOR
        bounceAnimY.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                holder.itemView.btnLike.setImageResource(R.drawable.ic_heart_red)
            }

            override fun onAnimationEnd(animation: Animator) {
                heartAnimationsMap.remove(holder)
                dispatchChangeFinishedIfAllAnimationsEnded(holder)
            }
        })

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim)
        animatorSet.start()

        heartAnimationsMap.put(holder, animatorSet)
    }

    private fun dispatchChangeFinishedIfAllAnimationsEnded(holder: NamesAdapter.ViewHolder) {
        if (likeAnimationsMap.containsKey(holder) || heartAnimationsMap.containsKey(holder)) {
            return
        }

        dispatchAnimationFinished(holder)
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)
        cancelCurrentAnimationIfExists(item)
    }

    override fun endAnimations() {
        super.endAnimations()
        for (animatorSet in likeAnimationsMap.values) {
            animatorSet.cancel()
        }
    }

    class FeedItemHolderInfo(var updateAction: String) : ItemHolderInfo()

    companion object {
        private val DECELERATE_INTERPOLATOR = DecelerateInterpolator()
        private val ACCELERATE_INTERPOLATOR = AccelerateInterpolator()
        private val OVERSHOOT_INTERPOLATOR = OvershootInterpolator(4f)
    }
}
