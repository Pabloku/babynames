package com.kamisoft.babynames.tracking

import com.kamisoft.babynames.tracking.TrackerConstants

data class TrackerEvent(val category: String,
                        val action: String = TrackerConstants.Action.CLICK.value,
                        val label: String,
                        val value: Double? = null) {

    override fun toString(): String = label
}
