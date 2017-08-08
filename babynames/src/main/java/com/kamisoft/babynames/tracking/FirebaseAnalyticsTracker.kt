package com.kamisoft.babynames.tracking

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.kamisoft.babynames.logger.Logger

class FirebaseAnalyticsTracker(context: Context) : Trackeable {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun sendScreen(section: String, screenName: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, section)
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, screenName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        Logger.debug("ANALYTICS: FIREBASE - sendScreen - CONTENT_TYPE: %s ITEM_ID: %s EVENT: select_content",
                section, screenName)
    }

    override fun sendEvent(event: TrackerEvent) {
        val params = Bundle()
        params.putString(TrackerConstants.Param.SCREEN.value, event.category)
        params.putString(TrackerConstants.Param.ACTION.value, event.action)
        event.value?.let { params.putDouble(FirebaseAnalytics.Param.VALUE, it) }
        firebaseAnalytics.logEvent(event.toString(), params)
        Logger.debug("ANALYTICS: FIREBASE - sendEvent - SCREEN: %s ACTION: %s EVENT: %s",
                event.category, event.action, event.toString())
    }

    override fun sendItemEvent(item: TrackerItem) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_ID, item.id)
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, item.category)
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, item.name)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params)
        Logger.debug("ANALYTICS: FIREBASE - sendItemEvent - ITEM_ID: %s ITEM_CATEGORY: %s ITEM_NAME: %s EVENT: view_item",
                item.id, item.category, item.name)

    }

    override fun sendShareEvent(type: String, name: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type)
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, name)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle)
        Logger.debug("ANALYTICS: FIREBASE - sendShareEvent - CONTENT_TYPE: %s ITEM_ID: %s EVENT: share",
                type, name)

    }

}
