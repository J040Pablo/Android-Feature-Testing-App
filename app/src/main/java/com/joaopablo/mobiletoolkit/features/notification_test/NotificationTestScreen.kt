package com.joaopablo.mobiletoolkit.features.notification_test

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTestScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teste Notificações 🔔", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFFF7F5FC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notification Tester",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C243B)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Este módulo permitirá disparar notificações locais imediatas e agendadas, bem como testar notificações via FCM (Firebase Cloud Messaging).",
                fontSize = 14.sp,
                color = Color(0xFF706484),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(30.dp))
            
            Button(
                onClick = { /* Will trigger test notification */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF825DFF))
            ) {
                Text("Disparar Notificação Local", color = Color.White)
            }
        }
    }
}
