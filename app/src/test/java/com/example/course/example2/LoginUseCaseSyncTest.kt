package com.example.course.example2

import com.example.course.example2.authtoken.AuthTokenCache
import com.example.course.example2.eventbus.EventBusPoster
import com.example.course.example2.eventbus.LoggedInEvent
import com.example.course.example2.network.LoginHttpEndpointSync
import com.example.course.example2.network.NetworkErrorException
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class LoginUseCaseSyncTest {
    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val AUTH_TOKEN = "authToken"
    }

    private lateinit var SUT: LoginUseCaseSync
    private lateinit var mLoginHttpEndpointSyncTd: LoginHttpEndpointSyncTd
    private lateinit var mAuthTokenCacheTd: AuthTokenCacheTd
    private lateinit var mEventBusPosterTd: EventBusPosterTd

    @Before
    @Throws(Exception::class)
    fun setup() {
        mLoginHttpEndpointSyncTd = LoginHttpEndpointSyncTd()
        mAuthTokenCacheTd = AuthTokenCacheTd()
        mEventBusPosterTd = EventBusPosterTd()
        SUT = LoginUseCaseSync(mLoginHttpEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_usernameAndPasswordPassedToEndPoint() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mLoginHttpEndpointSyncTd.mUsername, CoreMatchers.`is`(USERNAME))
        assertThat(mLoginHttpEndpointSyncTd.mPassword, CoreMatchers.`is`(PASSWORD))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_authTokenCached() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mAuthTokenCacheTd.getAuthToken(), CoreMatchers.`is`(AUTH_TOKEN))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mAuthTokenCacheTd.getAuthToken(), CoreMatchers.`is`(""))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mAuthError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mAuthTokenCacheTd.getAuthToken(), CoreMatchers.`is`(""))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsServerError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mAuthTokenCacheTd.getAuthToken(), CoreMatchers.`is`(""))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_loggedInEventPosted() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mEventBusPosterTd.mEvent, CoreMatchers.instanceOf(LoggedInEvent::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mEventBusPosterTd.mInteractionCount, CoreMatchers.`is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mAuthError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mEventBusPosterTd.mInteractionCount, CoreMatchers.`is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsServerError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(mEventBusPosterTd.mInteractionCount, CoreMatchers.`is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_successReturned() {
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.SUCCESS))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsServerError = true
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_failureReturned() {
        mLoginHttpEndpointSyncTd.mAuthError = true
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_networkError_networkErrorReturned() {
        mLoginHttpEndpointSyncTd.mIsNetworkError = true
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR))
    }

// --------------- Helper Class ---------------//

    private class LoginHttpEndpointSyncTd : LoginHttpEndpointSync {
        var mIsGeneralError: Boolean = false
        var mAuthError: Boolean = false
        var mIsServerError: Boolean = false
        var mIsNetworkError: Boolean = false
        var mUsername = ""
        var mPassword = ""

        @Throws(NetworkErrorException::class)
        override fun loginSync(
            username: String,
            password: String
        ): LoginHttpEndpointSync.EndpointResult {
            mUsername = username
            mPassword = password

            return when {
                mIsGeneralError ->
                    LoginHttpEndpointSync.EndpointResult(
                        LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                        ""
                    )
                mAuthError ->
                    LoginHttpEndpointSync.EndpointResult(
                        LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                        ""
                    )
                mIsServerError ->
                    LoginHttpEndpointSync.EndpointResult(
                        LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                        ""
                    )
                mIsNetworkError -> throw NetworkErrorException()
                else -> LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.SUCCESS,
                    AUTH_TOKEN
                )
            }
        }
    }

    private class AuthTokenCacheTd : AuthTokenCache {
        var mAuthToken = ""
        override fun cacheAuthToken(authToken: String) {
            mAuthToken = authToken
        }

        override fun getAuthToken(): String = mAuthToken
    }

    private class EventBusPosterTd : EventBusPoster {
        lateinit var mEvent: LoggedInEvent
        var mInteractionCount = 0

        override fun postEvent(event: LoggedInEvent) {
            mInteractionCount++
            mEvent = event
        }
    }
}