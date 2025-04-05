package com.yugentech.kaizen.data.model

data class Habit(
    val id: String = "",
    val title: String = "",
    val completionStatus: MutableMap<String, Boolean> = mutableMapOf()
)