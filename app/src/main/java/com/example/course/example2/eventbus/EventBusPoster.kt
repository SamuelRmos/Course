package com.example.course.example2.eventbus

interface EventBusPoster {
    fun postEvent(event: LoggedInEvent)
}