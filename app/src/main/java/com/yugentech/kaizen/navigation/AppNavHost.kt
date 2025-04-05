@file:Suppress("DEPRECATION")

package com.yugentech.kaizen.navigation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.yugentech.kaizen.ui.screens.AboutScreen
import com.yugentech.kaizen.ui.screens.DashboardScreen
import com.yugentech.kaizen.ui.screens.LoginScreen
import com.yugentech.kaizen.viewmodels.FirebaseViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = koinViewModel(),
    webClientId: String,
    startDestination: String
) {
    val state by firebaseViewModel.authState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                firebaseViewModel.handleGoogleSignInResult(result.data)
            }
        }
    )

    LaunchedEffect(key1 = state.pendingIntent) {
        state.pendingIntent?.let {
            launcher.launch(IntentSenderRequest.Builder(it).build())
        }
    }

    LaunchedEffect(key1 = state.userId) {
        if (state.userId != null) {
            navController.navigate(Screens.Dashboard.route) {
                popUpTo(Screens.Login.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screens.Login.route) {
                popUpTo(Screens.Dashboard.route) { inclusive = true }
            }
        }
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally { it } + fadeIn() },
        exitTransition = { slideOutHorizontally { -it } + fadeOut() },
        popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally { it } + fadeOut() }
    ) {
        composable(Screens.Login.route) {
            LoginScreen(
                state = state,
                onSignInClick = { email, password -> firebaseViewModel.signIn(email, password) },
                onSignUpClick = { email, password -> firebaseViewModel.signUp(email, password) },
                onGoogleSignInClick = { firebaseViewModel.getGoogleSignInIntent(webClientId) }
            )
        }
        composable(Screens.Dashboard.route) {
            var signOutRequested by remember { mutableStateOf(false) }

            if (signOutRequested) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screens.Login.route) {
                        popUpTo(Screens.Dashboard.route) { inclusive = true }
                    }
                    delay(300)
                    firebaseViewModel.signOut()
                }
            }

            DashboardScreen(
                state = state,
                navController = navController,
                onSignOut = { signOutRequested = true }
            )
        }
        composable(Screens.About.route) {
            AboutScreen(
                onNavigateBack = navController::popBackStack
            )
        }
    }
}