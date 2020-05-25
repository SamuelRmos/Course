package com.example.course.example4

import android.content.Context
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StringRetrieverTest {

    //region constants
    companion object {
        const val ID = 10
        const val STRING = "String"
    }
    //end region constants

    //region helper fields
    @Mock
    lateinit var mContextMock: Context
    // end region helper fields

    lateinit var sut: StringRetriever

    @Before
    fun setup() {
        sut = StringRetriever(mContextMock)
    }

    @Test
    fun getString_correctResultReturned() {
        Mockito.`when`(mContextMock.getString(ID)).thenReturn(STRING)
        val result = sut.getString(ID)
        assertThat(result, CoreMatchers.`is`(STRING))
    }

    // region helper methods

    // end region helper methods

    // region helper class

    // end region helper class
}