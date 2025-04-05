package com.yugentech.kaizen.repositories.firebaseRepository

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.yugentech.kaizen.data.auth.AuthResult

interface FirebaseRepository {
    suspend fun signUp(email: String, password: String): AuthResult<FirebaseUser>
    suspend fun signIn(email: String, password: String): AuthResult<FirebaseUser>
    fun getCurrentUser(): FirebaseUser?
    fun signOut()
    suspend fun getGoogleSignInIntent(webClientId: String): AuthResult<PendingIntent>
    suspend fun handleGoogleSignInResult(data: Intent?): AuthResult<FirebaseUser>
}