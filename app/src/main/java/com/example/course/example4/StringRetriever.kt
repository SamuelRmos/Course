package com.example.course.example4

import android.content.Context

class StringRetriever(private val mContext: Context) {

    fun getString(id: Int) = mContext.getString(id)
}
