package com.yugentech.kaizen.data.utils

import com.yugentech.kaizen.data.model.Habit

fun getDummyHabits(): List<Habit> {
    return listOf(
        Habit(title = "Read for 30 Minutes"),
        Habit(title = "Drink 2 Liters of Water"),
        Habit(title = "Exercise or Stretch"),
        Habit(title = "Meditate for 10 Minutes"),
        Habit(title = "Journal Gratitude"),
        Habit(title = "Plan Tomorrow's Tasks"),
        Habit(title = "Limit Social Media to 1 Hour"),
        Habit(title = "Sleep by 11 PM"),
        Habit(title = "Practice Coding"),
        Habit(title = "Eat 3 Balanced Meals")
    )
}