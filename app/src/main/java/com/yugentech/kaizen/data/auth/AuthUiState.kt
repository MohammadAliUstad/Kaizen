package com.yugentech.kaizen.data.auth

import android.app.PendingIntent

data class AuthUiState(
    val isLoading: Boolean = false,
    val userId: String? = null,
    val error: String? = null,
    val pendingIntent: PendingIntent? = null,
    val userData: UserData? = null,
    val isUserLoggedIn: Boolean = false
)