package com.yugentech.kaizen.viewmodels

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugentech.kaizen.data.utils.getDummyHabits
import com.yugentech.kaizen.data.model.Habit
import com.yugentech.kaizen.repositories.habitsRepository.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "kaizen_prefs")

class HabitsViewModel(
    private val repository: HabitsRepository,
    private val appContext: Context
) : ViewModel() {

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    var selectedDate: String = ""

    companion object {
        private val INIT_KEY = booleanPreferencesKey("is_initialized")
    }

    init {
        initializeHabitsOnce()
        getHabits()
    }

    private fun initializeHabitsOnce() {
        viewModelScope.launch {
            val prefs = appContext.dataStore.data.first()
            val alreadyInitialized = prefs[INIT_KEY] == true
            if (!alreadyInitialized) {
                val dummyHabits = getDummyHabits()
                dummyHabits.forEach { repository.addHabit(it) }

                appContext.dataStore.edit { settings ->
                    settings[INIT_KEY] = true
                }
            }
        }
    }

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.addHabit(habit)
                getHabits()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to add habit: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getHabits() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val habitsList = repository.getHabits().map { habit ->
                    habit.copy(completionStatus = habit.completionStatus)
                }
                _habits.value = habitsList
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load habits: ${e.message}"
                _habits.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateHabit(habitId: String, date: String, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateHabit(habitId, date, isCompleted)
            getHabits()
        }
    }

    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteHabit(habitId)
                getHabits()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to delete habit: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSelectedDate(date: String) {
        selectedDate = date
        getHabits()
    }
}