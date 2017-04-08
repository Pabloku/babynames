package com.kamisoft.babynames.logger

import timber.log.Timber
import java.util.regex.Pattern

internal class TimberLogger : LoggerWrapper {

    override fun init() {
        Timber.plant(Timber.DebugTree())
    }

    override fun verbose(text: String, vararg args: Any) {
        Timber.tag(getLogTag()).v(text, *args)
    }

    override fun debug(text: String, vararg args: Any) {
        Timber.tag(getLogTag()).d(text, *args)
    }

    override fun info(text: String, vararg args: Any) {
        Timber.tag(getLogTag()).i(text, *args)
    }

    override fun warning(text: String, vararg args: Any) {
        Timber.tag(getLogTag()).w(text, *args)
    }

    override fun error(t: Throwable, text: String, vararg args: Any) {
        Timber.tag(getLogTag()).e(t, text, *args)
    }

    override fun tag(tag: String): BasicLoggerWrapper {
        return TagTimberLogger(tag)
    }

    private fun getLogTag(): String {
        var tag = "NoTag"
        val CALL_STACK_INDEX = 3
        val stackTrace = java.lang.Throwable().stackTrace
        if (stackTrace.size > CALL_STACK_INDEX) {
            tag = createStackElementTag(stackTrace[CALL_STACK_INDEX])
        }
        return tag
    }

    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className
        val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
        val MAX_TAG_LENGTH = 23
        val matcher = ANONYMOUS_CLASS.matcher(tag)
        if (matcher.find()) {
            tag = matcher.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        tag += ":" + element.lineNumber
        return if (tag.length > MAX_TAG_LENGTH) tag.substring(0, MAX_TAG_LENGTH) else tag
    }

    inner class TagTimberLogger(private val tag: String) : BasicLoggerWrapper {

        override fun verbose(text: String, vararg args: Any) {
            Timber.tag(tag).v(text, *args)
        }

        override fun debug(text: String, vararg args: Any) {
            Timber.tag(tag).d(text, *args)
        }

        override fun info(text: String, vararg args: Any) {
            Timber.tag(tag).i(text, *args)
        }

        override fun warning(text: String, vararg args: Any) {
            Timber.tag(tag).w(text, *args)
        }

        override fun error(t: Throwable, text: String, vararg args: Any) {
            Timber.tag(tag).e(t, text, *args)
        }

    }
}
