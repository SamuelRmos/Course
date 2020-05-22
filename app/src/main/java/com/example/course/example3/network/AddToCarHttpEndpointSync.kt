package com.example.course.example3.network

import android.accounts.NetworkErrorException

interface AddToCarHttpEndpointSync {

    @Throws(NetworkErrorException::class)
    fun addToCartSync(cartItemScheme: CartItemScheme): EndpointResult

    enum class EndpointResult {
        SUCCESS,
        AUTH_ERROR,
        GENERAL_ERROR
    }


}