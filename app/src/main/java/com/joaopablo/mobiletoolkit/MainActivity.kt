package com.joaopablo.mobiletoolkit

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.joaopablo.mobiletoolkit.navigation.AppNavigation
import com.joaopablo.mobiletoolkit.ui.theme.MobileToolkitTheme

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileToolkitTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    onBackToHome = {
                        if (!navController.popBackStack()) {
                            finish()
                        }
                    }
                )
            }
        }
    }
}