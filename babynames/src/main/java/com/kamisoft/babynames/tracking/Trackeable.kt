package com.kamisoft.babynames.tracking


interface Trackeable {

    fun sendScreen(section: String, screenName: String)

    fun sendEvent(event: TrackerEvent)

    fun sendItemEvent(item: TrackerItem)

    fun sendShareEvent(type: String, name: String)
}
