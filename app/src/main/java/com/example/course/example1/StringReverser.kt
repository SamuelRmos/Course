package com.example.course.example1

import java.lang.StringBuilder

class StringReverser {
    @ExperimentalStdlibApi
    fun reverse(string: String): String {
        val sd = StringBuilder()
        for (x in string.length - 1 downTo 0) {
            sd.append(string.toCharArray()[x])
        }
        return sd.toString()
    }
}