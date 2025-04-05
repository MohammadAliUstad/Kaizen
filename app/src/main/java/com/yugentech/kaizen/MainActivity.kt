package com.yugentech.kaizen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.yugentech.kaizen.navigation.AppNavHost
import com.yugentech.kaizen.navigation.Screens
import com.yugentech.kaizen.ui.theme.KaizenTheme
import com.yugentech.kaizen.viewmodels.FirebaseViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            KaizenTheme {
                val navController = rememberNavController()
                val webClientId = getString(R.string.web_client_id)

                AppNavHost(
                    navController = navController,
                    webClientId = webClientId,
                    startDestination = getStartDestination(firebaseViewModel = koinViewModel())
                )
            }
        }
    }

    private fun getStartDestination(firebaseViewModel: FirebaseViewModel): String {
        return if (firebaseViewModel.isUserLoggedIn()) {
            Screens.Dashboard.route
        } else {
            Screens.Login.route
        }
    }
}