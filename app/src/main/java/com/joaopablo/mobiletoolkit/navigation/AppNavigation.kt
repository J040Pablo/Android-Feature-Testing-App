package com.joaopablo.mobiletoolkit.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joaopablo.mobiletoolkit.features.biometric_test.BiometricTestScreen
import com.joaopablo.mobiletoolkit.features.camera_test.CameraTestScreen
import com.joaopablo.mobiletoolkit.features.home.HomeScreen
import com.joaopablo.mobiletoolkit.features.home.HomeViewModel
import com.joaopablo.mobiletoolkit.features.notification_test.NotificationTestScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    onBackToHome: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }
        composable(Routes.BIOMETRIC) {
            BiometricTestScreen(navController = navController)
        }
        composable(Routes.CAMERA) {
            CameraTestScreen(navController = navController)
        }
        composable(Routes.NOTIFICATION) {
            NotificationTestScreen(navController = navController)
        }
    }
}
