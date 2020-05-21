package com.example.course.example1

import com.example.course.example1.StringReverser
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StringReverserTest{
    private lateinit var SUT : StringReverser

    @Before
    fun setup(){
        SUT = StringReverser()
    }

    @Test
    @ExperimentalStdlibApi
    fun reverse_emptyString_emptyReturned(){
        val result = SUT.reverse("")
        assertThat(result, CoreMatchers.`is`(""))
    }

    @Test
    @ExperimentalStdlibApi
    fun reverse_singleCharacter_sameStringReturned(){
        val result = SUT.reverse("a")
        assertThat(result, CoreMatchers.`is`("a"))
    }

    @Test
    @ExperimentalStdlibApi
    fun reverse_longString_reversedStringReturned(){
        val result = SUT.reverse("Samuel Ramos")
        assertThat(result, CoreMatchers.`is`("somaR leumaS"))
    }
}