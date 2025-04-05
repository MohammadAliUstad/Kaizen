package com.yugentech.kaizen.console

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yugentech.kaizen.data.model.Habit
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
    }

    private fun habitsCollection() =
        db.collection("users").document(getUserId()).collection("habits")

    suspend fun addHabit(habit: Habit) {
        val newHabitRef = habitsCollection().document()
        val newHabit = habit.copy(id = newHabitRef.id)
        newHabitRef.set(newHabit).await()  // ✅ Ensures ID is correctly stored
    }

    suspend fun getHabits(): List<Habit> {
        return habitsCollection().get().await().toObjects(Habit::class.java)
    }

    suspend fun updateHabit(habitId: String, date: String, isCompleted: Boolean) {
        val habitRef = habitsCollection().document(habitId)
        habitRef.update("completionStatus.$date", isCompleted).await()
    }

    suspend fun deleteHabit(habitId: String) {
        habitsCollection().document(habitId).delete().await()
    }
}