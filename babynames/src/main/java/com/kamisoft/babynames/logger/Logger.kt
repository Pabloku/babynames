package com.kamisoft.babynames.logger

import com.kamisoft.babynames.logger.BasicLoggerWrapper
import com.kamisoft.babynames.logger.TimberLogger

internal class Logger {

    companion object Log {

        private val timberLogger: TimberLogger = TimberLogger()

        fun start() {
            timberLogger.init()
        }

        fun verbose(text: String, vararg args: Any) {
            timberLogger.verbose(text, *args)
        }

        fun debug(text: String, vararg args: Any) {
            timberLogger.debug(text, *args)
        }

        fun info(text: String, vararg args: Any) {
            timberLogger.info(text, *args)
        }

        fun warning(text: String, vararg args: Any) {
            timberLogger.warning(text, *args)
        }

        fun error(t: Throwable, text: String, vararg args: Any) {
            timberLogger.error(t, text, *args)
        }

        fun tag(tag: String): BasicLoggerWrapper {
            return timberLogger.tag(tag)
        }
    }
}
