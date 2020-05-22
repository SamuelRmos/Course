package com.example.course.example3

import android.accounts.NetworkErrorException
import com.example.course.example3.network.AddToCarHttpEndpointSync
import com.example.course.example3.network.CartItemScheme
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddCartUseCaseSyncTest {

    companion object {
        const val OFFER_ID = "offerId"
        const val AMOUNT = 4
    }
    //region  constants

    //end region constants

    //region helper fields

    @Mock
    lateinit var mAddToCartHttpEndpointSyncMock: AddToCarHttpEndpointSync

    // end region helper fields

    private lateinit var sut: AddCartUseCaseSync

    @Before
    fun setup() {
        sut = AddCartUseCaseSync(mAddToCartHttpEndpointSyncMock)
        success()
    }

    // correct parameters passed to the endpoint

    @Test
    fun addToCartSync_correctParametersPassedToEndpoint() {
        //Arrange
        val argCaptor = argumentCaptor<CartItemScheme>()
        //Act
        sut.addToCartSync(OFFER_ID, AMOUNT)
        //Assert
        verify(mAddToCartHttpEndpointSyncMock).addToCartSync(argCaptor.capture())
        assertThat(argCaptor.lastValue.getOfferId(), CoreMatchers.`is`(OFFER_ID))
        assertThat(argCaptor.firstValue.getAmount(), CoreMatchers.`is`(AMOUNT))
    }

    // endpoint success - success returned

    @Test
    fun addToCartSync_success_successReturned() {
        //Arrange

        //Act
        val result = sut.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        assertThat(result, CoreMatchers.`is`(AddCartUseCaseSync.UseCaseResult.SUCCESS))
    }

    // endpoint auth error - failure returned

    @Test
    fun addToCartSync_authError_failureReturned() {
        //Arrange
        authError()
        //Act
        val result = sut.addToCartSync(OFFER_ID, AMOUNT)
        //Assert
        assertThat(result, CoreMatchers.`is`(AddCartUseCaseSync.UseCaseResult.FAILURE))
    }

    // endpoint general error - failure returned

    @Test
    fun addToCartSync_generalError_failureReturned() {
        //Arrange
        generalError()
        //Act
        val result = sut.addToCartSync(OFFER_ID, AMOUNT)
        //Assert
        assertThat(result, CoreMatchers.`is`(AddCartUseCaseSync.UseCaseResult.FAILURE))
    }

    // network exception - network error returned

    @Test
    fun addCartSync_networkError_networkErrorReturned() {
        //Arrange
        networkError()
        //Act
        val result = sut.addToCartSync(OFFER_ID, AMOUNT)
        //Assert
        assertThat(result, CoreMatchers.`is`(AddCartUseCaseSync.UseCaseResult.NETWORK_ERROR))
    }

    // region helper methods

    @Throws(NetworkErrorException::class)
    private fun authError() {
        `when`(mAddToCartHttpEndpointSyncMock.addToCartSync(any()))
            .thenReturn(AddToCarHttpEndpointSync.EndpointResult.AUTH_ERROR)
    }

    @Throws(NetworkErrorException::class)
    private fun generalError() {
        `when`(mAddToCartHttpEndpointSyncMock.addToCartSync(any()))
            .thenReturn(AddToCarHttpEndpointSync.EndpointResult.GENERAL_ERROR)
    }

    @Throws(NetworkErrorException::class)
    private fun networkError() {
        `when`(mAddToCartHttpEndpointSyncMock.addToCartSync(any()))
            .thenThrow(NetworkErrorException())
    }

    private fun success() {
        `when`(mAddToCartHttpEndpointSyncMock.addToCartSync(any()))
            .thenReturn(AddToCarHttpEndpointSync.EndpointResult.SUCCESS)
    }

    // end region helper methods

    // region helper class

    // end region helper class
}