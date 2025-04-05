package com.yugentech.kaizen.repositories.firebaseRepository

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.yugentech.kaizen.data.auth.AuthResult
import com.yugentech.kaizen.console.FirebaseService

class FirebaseRepositoryImpl(
    private val firebaseService: FirebaseService
) : FirebaseRepository {

    override suspend fun signUp(email: String, password: String): AuthResult<FirebaseUser> {
        return firebaseService.signUp(email, password)
    }

    override suspend fun signIn(email: String, password: String): AuthResult<FirebaseUser> {
        return firebaseService.signIn(email, password)
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseService.getCurrentUser()
    }

    override fun signOut() {
        firebaseService.signOut()
    }

    override suspend fun getGoogleSignInIntent(webClientId: String): AuthResult<PendingIntent> {
        return firebaseService.getGoogleSignInIntent(webClientId)
    }

    override suspend fun handleGoogleSignInResult(data: Intent?): AuthResult<FirebaseUser> {
        return firebaseService.handleGoogleSignInResult(data)
    }
}