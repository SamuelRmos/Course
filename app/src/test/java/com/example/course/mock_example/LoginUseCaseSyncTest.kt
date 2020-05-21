package com.example.course.mock_example

import com.example.course.example2.LoginUseCaseSync
import com.example.course.example2.authtoken.AuthTokenCache
import com.example.course.example2.eventbus.EventBusPoster
import com.example.course.example2.eventbus.LoggedInEvent
import com.example.course.example2.network.LoginHttpEndpointSync
import com.example.course.example2.network.NetworkErrorException
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*
import java.lang.Exception

class LoginUseCaseSyncTest {
    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val AUTH_TOKEN = "authToken"
    }

    lateinit var mLoginHttpEndpointSyncMock: LoginHttpEndpointSync

    lateinit var mAuthTokenCacheMock: AuthTokenCache
    lateinit var mEventBusPosterMock: EventBusPoster
    private lateinit var SUT: LoginUseCaseSync

    @Before
    @Throws(Exception::class)
    fun setup() {
        mLoginHttpEndpointSyncMock = mock(LoginHttpEndpointSync::class.java)
        mAuthTokenCacheMock = mock(AuthTokenCache::class.java)
        mEventBusPosterMock = mock(EventBusPoster::class.java)
        SUT = LoginUseCaseSync(mLoginHttpEndpointSyncMock, mAuthTokenCacheMock, mEventBusPosterMock)
        success()
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_usernameAndPasswordPassedToEndpoint() {
        val argCaptor = argumentCaptor<String>()
        SUT.loginSync(USERNAME, PASSWORD)

        verify(mLoginHttpEndpointSyncMock, times(1))
            .loginSync(argCaptor.capture(), argCaptor.capture())
        val captures = argCaptor.allValues

        assertThat(captures[0], CoreMatchers.`is`(USERNAME))
        assertThat(captures[1], CoreMatchers.`is`(PASSWORD))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_authTokenCached() {
        val argCaptor = argumentCaptor<String>()
        SUT.loginSync(USERNAME, PASSWORD)
        verify(mAuthTokenCacheMock).cacheAuthToken(argCaptor.capture())
        assertThat(argCaptor.firstValue, CoreMatchers.`is`(AUTH_TOKEN))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_authTokenNotCached() {
        generalError()
        SUT.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(mAuthTokenCacheMock)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_authTokenNotCached() {
        serverError()
        SUT.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(mAuthTokenCacheMock)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_loggedInEventPosted() {
        val argCaptor = argumentCaptor<LoggedInEvent>()
        SUT.loginSync(USERNAME, PASSWORD)
        verify(mEventBusPosterMock).postEvent(argCaptor.capture())
        assertThat(argCaptor.lastValue, CoreMatchers.instanceOf(LoggedInEvent::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_noInteractionWithEventBusPoster() {
        generalError()
        SUT.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(mEventBusPosterMock)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_noInteractionWithEventBusPoster() {
        authError()
        SUT.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(mEventBusPosterMock)
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
        serverError()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_failureReturned() {
        authError()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_failureReturned() {
        generalError()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_networkError_networkErrorReturned() {
        networkError()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(result, CoreMatchers.`is`(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR))
    }

    private fun success() {
        `when`(
            mLoginHttpEndpointSyncMock.loginSync(
                anyString(),
                anyString()
            )
        ).thenReturn(
            LoginHttpEndpointSync.EndpointResult(
                LoginHttpEndpointSync.EndpointResultStatus.SUCCESS,
                AUTH_TOKEN
            )
        )
    }

    private fun generalError() {
        `when`(mLoginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
            .thenReturn(
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                    ""
                )
            )
    }

    private fun serverError() {
        `when`(mLoginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
            .thenReturn(
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                    ""
                )
            )
    }

    private fun authError() {
        `when`(mLoginHttpEndpointSyncMock.loginSync(anyString(), anyString()))
            .thenReturn(
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                    ""
                )
            )
    }

    private fun networkError() {
        doThrow(NetworkErrorException())
            .`when`(mLoginHttpEndpointSyncMock).loginSync(anyString(), anyString())
    }


}