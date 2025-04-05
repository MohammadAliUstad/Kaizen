package com.yugentech.kaizen.repositories.habitsRepository

import com.yugentech.kaizen.console.FirestoreService
import com.yugentech.kaizen.data.model.Habit

class HabitsRepositoryImpl(
    private val firestoreService: FirestoreService
) : HabitsRepository {

    override suspend fun addHabit(habit: Habit) {
        firestoreService.addHabit(habit)
    }

    override suspend fun getHabits(): List<Habit> {
        return firestoreService.getHabits()
    }

    override suspend fun updateHabit(habitId: String, date: String, isCompleted: Boolean) {
        firestoreService.updateHabit(habitId, date, isCompleted)
    }

    override suspend fun deleteHabit(habitId: String) {
        firestoreService.deleteHabit(habitId)
    }
}