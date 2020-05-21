package com.example.course.example2

import com.example.course.example2.authtoken.AuthTokenCache
import com.example.course.example2.eventbus.EventBusPoster
import com.example.course.example2.eventbus.LoggedInEvent
import com.example.course.example2.network.LoginHttpEndpointSync
import com.example.course.example2.network.NetworkErrorException

class LoginUseCaseSync(
    private val mLoginHttpEndpointSync: LoginHttpEndpointSync,
    private val mAuthTokenCache: AuthTokenCache,
    private val mEventBusPoster: EventBusPoster
) {
    enum class UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    fun loginSync(username: String, password: String): UseCaseResult {
        val endpointEndPointResult: LoginHttpEndpointSync.EndpointResult
        try {
            endpointEndPointResult = mLoginHttpEndpointSync.loginSync(username, password)
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        return when {
            isSuccessfulEndpointResult(endpointEndPointResult) -> {
                mAuthTokenCache.cacheAuthToken(endpointEndPointResult.getAuthToken())
                mEventBusPoster.postEvent(LoggedInEvent())
                UseCaseResult.SUCCESS
            }
            else -> UseCaseResult.FAILURE
        }
    }

    private fun isSuccessfulEndpointResult(endpointResult: LoginHttpEndpointSync.EndpointResult) =
        endpointResult.getStatus() == LoginHttpEndpointSync.EndpointResultStatus.SUCCESS
}