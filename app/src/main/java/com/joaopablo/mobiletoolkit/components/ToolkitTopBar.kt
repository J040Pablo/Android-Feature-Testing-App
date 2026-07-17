package com.joaopablo.mobiletoolkit.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Reusable top app bar that integrates with the Mobile Toolkit design system.
 *
 * - Uses the same lavender/gray background [Color(0xFFF7F5FC)] as the main Scaffold.
 * - Title and back-arrow are rendered in white, matching the premium look of the Home screen.
 * - Respects WindowInsets.statusBars automatically via TopAppBar.
 *
 * @param title       Text shown as the screen title.
 * @param onBack      Lambda called when the user taps the back arrow.
 * @param tintColor   Override the icon / title color if needed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolkitTopBar(
    title: String,
    onBack: () -> Unit,
    tintColor: Color = Color(0xFF2C243B)
) {
    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = tintColor
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = tintColor
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF7F5FC),
            scrolledContainerColor = Color(0xFFF7F5FC)
        )
    )
}
