package com.example.course.example3.network

open class CartItemScheme(
    private val offedId: String,
    private val amount: Int
) {
    fun getOfferId() = offedId
    fun getAmount() = amount
}