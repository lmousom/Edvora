package com.mousom.edvora.utils

object ListToStringUtil {

    fun listToString(list: List<Int>): String {
        val stringList = list.map { it.toString() }.toTypedArray()
        return stringList.joinToString(
            prefix = "[",
            separator = ", ",
            postfix = "]"
        )
    }
}