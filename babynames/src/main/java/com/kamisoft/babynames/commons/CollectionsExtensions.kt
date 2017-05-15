package com.kamisoft.babynames.commons

import java.util.*

fun <K, V : Any> Map<K, V?>.toVarargArray(): Array<out Pair<K, V>> =
        map({ Pair(it.key, it.value!!) }).toTypedArray()

inline fun <T, R : Any> Iterable<T>.firstResult(predicate: (T) -> R?): R {
    this
            .mapNotNull { predicate(it) }
            .forEach { return it }
    throw NoSuchElementException("No element matching predicate was found.")
}