package com.example.course.example3

import android.accounts.NetworkErrorException
import com.example.course.example3.network.AddToCarHttpEndpointSync
import com.example.course.example3.network.CartItemScheme
import java.lang.RuntimeException

class AddCartUseCaseSync(private val mAddToCartHttpEndpointSync: AddToCarHttpEndpointSync) {

    enum class UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    fun addToCartSync(offerId: String, amount: Int): UseCaseResult {
        val result: AddToCarHttpEndpointSync.EndpointResult

        try {
            result = mAddToCartHttpEndpointSync.addToCartSync(CartItemScheme(offerId, amount))
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        return when (result) {
            AddToCarHttpEndpointSync.EndpointResult.SUCCESS ->
                UseCaseResult.SUCCESS
            AddToCarHttpEndpointSync.EndpointResult.AUTH_ERROR ->
                UseCaseResult.FAILURE
            AddToCarHttpEndpointSync.EndpointResult.GENERAL_ERROR ->
                UseCaseResult.FAILURE
        }
    }
}