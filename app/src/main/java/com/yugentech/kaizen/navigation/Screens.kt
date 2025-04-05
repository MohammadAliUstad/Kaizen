package com.yugentech.kaizen.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    data object Login : Screens("login", "Login", Icons.AutoMirrored.Filled.Login)
    data object Dashboard : Screens("dashboard", "Dashboard", Icons.Filled.Dashboard)
    data object About : Screens("about", "About", Icons.Filled.Info)
}