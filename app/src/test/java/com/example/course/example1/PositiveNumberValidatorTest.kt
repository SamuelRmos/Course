package com.example.course.example1

import com.example.course.example1.PositiveNumberValidator
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PositiveNumberValidatorTest{
    private lateinit var SUT: PositiveNumberValidator

    @Before
    fun setup(){
        SUT = PositiveNumberValidator()
    }

    @Test
    fun test1(){
        val result = SUT.isPositive(-1)
        assert(!result)
    }

    @Test
    fun test2(){
        val result = SUT.isPositive(0)
        assert(!result)
    }

    @Test
    fun test3(){
        val result = SUT.isPositive(1)
        assertThat(result, CoreMatchers.`is`(true))
    }

}