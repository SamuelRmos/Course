package com.example.course.example2.authtoken

interface AuthTokenCache {
    fun cacheAuthToken(authToken: String)
    fun getAuthToken():String
}