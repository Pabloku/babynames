package com.kamisoft.babynames.logger

import com.kamisoft.babynames.logger.BasicLoggerWrapper

internal interface LoggerWrapper: BasicLoggerWrapper {

    fun init()

    fun tag(tag: String) : BasicLoggerWrapper
}
