package com.yugentech.kaizen.data.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object DataStoreUtils {
    private const val PREF_NAME = "kaizen_prefs"
    private val Context.dataStore by preferencesDataStore(name = PREF_NAME)

    val HAS_INITIALIZED = booleanPreferencesKey("has_initialized_dummy_habits")

    suspend fun hasInitialized(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[HAS_INITIALIZED] == true
    }

    suspend fun markInitialized(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[HAS_INITIALIZED] = true
        }
    }
}