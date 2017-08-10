package com.kamisoft.babynames.tracking

import android.content.Context
import android.text.TextUtils
import com.kamisoft.babynames.tracking.FirebaseAnalyticsTracker
import com.kamisoft.babynames.tracking.TrackerConstants
import com.kamisoft.babynames.tracking.TrackerEvent
import com.kamisoft.babynames.tracking.TrackerItem

class TrackerManager(context: Context) {

    private val firebaseAnalyticsTracker = FirebaseAnalyticsTracker(context)

    fun sendScreen(section: String, screen: String) {
        if (!TextUtils.isEmpty(section) && !TextUtils.isEmpty(screen)) {
            firebaseAnalyticsTracker.sendScreen(section, screen)
        }
    }

    fun sendEvent(category: String, action: String = TrackerConstants.Action.CLICK.value, label: String) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(action) && !TextUtils.isEmpty(label)) {
            val event = TrackerEvent(label = label, action = action, category = category)
            firebaseAnalyticsTracker.sendEvent(event)
        }
    }

    fun sendItemEvent(itemId: String, itemCategory: String, itemName: String) {
        if (!TextUtils.isEmpty(itemId) && !TextUtils.isEmpty(itemCategory) && !TextUtils.isEmpty(
                itemName)) {
            val item = TrackerItem(itemId, itemName, itemCategory)
            firebaseAnalyticsTracker.sendItemEvent(item)
        }
    }

    fun sendShareEvent(type: String, name: String) {
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(name)) {
            firebaseAnalyticsTracker.sendShareEvent(type, name)
        }
    }

}
