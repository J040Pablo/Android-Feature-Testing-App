package com.joaopablo.mobiletoolkit.features.notification_test

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joaopablo.mobiletoolkit.components.ToolkitTopBar
import com.joaopablo.mobiletoolkit.core.notification.NotificationManager

// ─────────────────────────────────────────────────────────────
// State model
// ─────────────────────────────────────────────────────────────
enum class NotificationStatus { IDLE, SENT, DENIED, ERROR }

data class NotificationUIState(
    val status: NotificationStatus = NotificationStatus.IDLE,
    val message: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTestScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val notificationManager = remember { NotificationManager(context) }

    var uiState by remember { mutableStateOf(NotificationUIState()) }

    // Permission launcher for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val sent = notificationManager.sendLocalNotification(
                title = "Mobile Toolkit",
                message = "Notificação local disparada com sucesso 🚀"
            )
            uiState = if (sent) {
                NotificationUIState(NotificationStatus.SENT, "Notificação enviada com sucesso!")
            } else {
                NotificationUIState(NotificationStatus.ERROR, "Erro inesperado ao enviar.")
            }
        } else {
            uiState = NotificationUIState(
                NotificationStatus.DENIED,
                "Permissão de notificação negada pelo usuário."
            )
        }
    }

    fun triggerNotification() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ → request runtime permission first
                if (notificationManager.hasNotificationPermission()) {
                    val sent = notificationManager.sendLocalNotification(
                        "Mobile Toolkit",
                        "Notificação local disparada com sucesso 🚀"
                    )
                    uiState = if (sent) {
                        NotificationUIState(NotificationStatus.SENT, "Notificação enviada com sucesso!")
                    } else {
                        NotificationUIState(NotificationStatus.ERROR, "Erro inesperado ao enviar.")
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                // Android < 13 → works without runtime permission
                val sent = notificationManager.sendLocalNotification(
                    "Mobile Toolkit",
                    "Notificação local disparada com sucesso 🚀"
                )
                uiState = if (sent) {
                    NotificationUIState(NotificationStatus.SENT, "Notificação enviada com sucesso!")
                } else {
                    NotificationUIState(NotificationStatus.ERROR, "Erro inesperado ao enviar.")
                }
            }
        } catch (e: Exception) {
            uiState = NotificationUIState(
                NotificationStatus.ERROR,
                "Erro: ${e.localizedMessage}"
            )
        }
    }

    Scaffold(
        topBar = {
            ToolkitTopBar(
                title = "Teste Notificações 🔔",
                onBack = { navController.popBackStack() }
            )
        },
        containerColor = Color(0xFFF7F5FC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Icon ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE5DFFF), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificação",
                    tint = Color(0xFF825DFF),
                    modifier = Modifier.size(56.dp)
                )
            }

            // ── Title + description ───────────────────────────
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Notification Tester",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C243B)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Dispara uma notificação local imediata para a bandeja do sistema. " +
                            "No Android 13+ a permissão POST_NOTIFICATIONS é solicitada automaticamente.",
                    fontSize = 14.sp,
                    color = Color(0xFF706484),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            // ── Status Card ───────────────────────────────────
            NotificationStatusCard(state = uiState)

            // ── Action Button ─────────────────────────────────
            Button(
                onClick = { triggerNotification() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF825DFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Disparar Notificação Local",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Status Card
// ─────────────────────────────────────────────────────────────
@Composable
private fun NotificationStatusCard(state: NotificationUIState) {
    val (bg, fg, icon, label) = when (state.status) {
        NotificationStatus.IDLE -> Quad(
            Color(0xFFF4F2F9), Color(0xFF706484),
            Icons.Default.Notifications, "Aguardando"
        )
        NotificationStatus.SENT -> Quad(
            Color(0xFFE8F5E9), Color(0xFF2E7D32),
            Icons.Default.CheckCircle, "Enviada ✅"
        )
        NotificationStatus.DENIED -> Quad(
            Color(0xFFFFEBEE), Color(0xFFC62828),
            Icons.Default.Error, "Permissão Negada ❌"
        )
        NotificationStatus.ERROR -> Quad(
            Color(0xFFFFF8E1), Color(0xFFE65100),
            Icons.Default.Warning, "Erro ⚠️"
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, fg.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = fg, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = fg)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = state.message ?: "Toque no botão para disparar uma notificação.",
                    fontSize = 13.sp,
                    color = fg.copy(alpha = 0.8f)
                )
            }
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
