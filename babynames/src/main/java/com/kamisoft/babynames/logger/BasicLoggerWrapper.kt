package com.kamisoft.babynames.logger

interface BasicLoggerWrapper {

    fun verbose(text: String, vararg args: Any)

    fun debug(text: String, vararg args: Any)

    fun info(text: String, vararg args: Any)

    fun warning(text: String, vararg args: Any)

    fun error(t: Throwable, text: String, vararg args: Any)
}