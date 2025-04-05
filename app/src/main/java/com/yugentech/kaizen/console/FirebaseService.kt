@file:Suppress("DEPRECATION")

package com.yugentech.kaizen.console

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.yugentech.kaizen.data.auth.AuthResult
import kotlinx.coroutines.tasks.await

class FirebaseService(
    context: Context
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    suspend fun signUp(email: String, password: String): AuthResult<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.user!!)
        } catch (e: Exception) {
            AuthResult.Error("Sign up failed: ${e.message}")
        }
    }

    suspend fun signIn(email: String, password: String): AuthResult<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.user!!)
        } catch (e: Exception) {
            AuthResult.Error("Sign in failed: ${e.message}")
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        val user = auth.currentUser
        return user
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun getGoogleSignInIntent(webClientId: String): AuthResult<PendingIntent> {
        return try {
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(webClientId)
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(false)
                .build()

            val result = oneTapClient.beginSignIn(signInRequest).await()
            AuthResult.Success(result.pendingIntent)
        } catch (e: Exception) {
            AuthResult.Error("Google Sign-In failed: ${e.message}")
        }
    }

    suspend fun handleGoogleSignInResult(data: Intent?): AuthResult<FirebaseUser> {
        return try {
            if (data == null) {
                return AuthResult.Error("Google Sign-In failed: No data received")
            }
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken ?: throw Exception("Google ID token is null")
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(firebaseCredential).await()
            AuthResult.Success(result.user!!)
        } catch (e: Exception) {
            AuthResult.Error("Google Sign-In failed: ${e.message}")
        }
    }
}