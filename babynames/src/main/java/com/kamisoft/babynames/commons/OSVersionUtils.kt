package com.kamisoft.babynames.commons

import android.os.Build

object OSVersionUtils {
    val isGreaterOrEqualThanLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}
