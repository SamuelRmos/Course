package com.example.course.example2.network

interface LoginHttpEndpointSync {

    @Throws(NetworkErrorException::class)
    fun loginSync(username: String, password: String): EndpointResult

    enum class EndpointResultStatus {
        SUCCESS,
        AUTH_ERROR,
        SERVER_ERROR,
        GENERAL_ERROR
    }

    class EndpointResult(
        private val mStatus: EndpointResultStatus,
        private val mAuthToken: String
    ) {
        fun getStatus(): EndpointResultStatus = mStatus
        fun getAuthToken(): String = mAuthToken
    }
}