package com.yugentech.kaizen.repositories.habitsRepository

import com.yugentech.kaizen.data.model.Habit

interface HabitsRepository {
    suspend fun addHabit(habit: Habit)
    suspend fun getHabits(): List<Habit>
    suspend fun updateHabit(habitId: String, date: String, isCompleted: Boolean)
    suspend fun deleteHabit(habitId: String)
}