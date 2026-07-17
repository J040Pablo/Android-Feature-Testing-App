package com.joaopablo.mobiletoolkit.features.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joaopablo.mobiletoolkit.components.FloatingBottomBar
import com.joaopablo.mobiletoolkit.components.NotificationButton
import com.joaopablo.mobiletoolkit.components.ToolkitCard
import com.joaopablo.mobiletoolkit.navigation.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val hasNotification by viewModel.hasNotification.collectAsState()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            FloatingBottomBar(
                currentTabRoute = currentTab,
                onTabSelected = { tab ->
                    viewModel.updateSelectedTab(tab)
                }
            )
        },
        containerColor = Color(0xFFF7F5FC) // Soft modern lavender/gray background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                "home_tab" -> {
                    HomeDashboardContent(
                        navController = navController,
                        hasNotification = hasNotification,
                        onNotificationClick = {
                            viewModel.clearNotificationBadge()
                            Toast.makeText(context, "Notificações lidas", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                "upload_tab" -> {
                    UploadTabContent()
                }
                "library_tab" -> {
                    LibraryTabContent()
                }
                "profile_tab" -> {
                    ProfileTabContent()
                }
            }
        }
    }
}

@Composable
fun HomeDashboardContent(
    navController: NavController,
    hasNotification: Boolean,
    onNotificationClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Mobile Toolkit",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2C243B)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Test Android Features",
                    fontSize = 14.sp,
                    color = Color(0xFF8B8496)
                )
            }
            
            NotificationButton(
                hasNotification = hasNotification,
                onClick = onNotificationClick
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Center Content - Tool Cards
        ToolkitCard(
            emoji = "🔐",
            title = "Biometria",
            description = "Teste autenticação por impressão digital ou rosto",
            actionLabel = "Testar",
            onActionClick = {
                navController.navigate(Routes.BIOMETRIC)
            }
        )

        ToolkitCard(
            emoji = "📷",
            title = "CameraX",
            description = "Captura de imagens usando câmera nativa",
            actionLabel = "Abrir",
            onActionClick = {
                navController.navigate(Routes.CAMERA)
            }
        )

        ToolkitCard(
            emoji = "🙂",
            title = "Face Detection",
            description = "Reconhecimento facial usando ML Kit",
            actionLabel = "Detectar",
            onActionClick = {
                Toast.makeText(context, "Face Detection: Recurso em desenvolvimento", Toast.LENGTH_SHORT).show()
            }
        )

        ToolkitCard(
            emoji = "🔔",
            title = "Notificações",
            description = "Teste notificações locais e Firebase",
            actionLabel = "Enviar",
            onActionClick = {
                navController.navigate(Routes.NOTIFICATION)
            }
        )

        ToolkitCard(
            emoji = "🧩",
            title = "Widget",
            description = "Testar widgets Android",
            actionLabel = "Atualizar",
            onActionClick = {
                Toast.makeText(context, "Atualizando Widgets...", Toast.LENGTH_SHORT).show()
            }
        )
        
        Spacer(modifier = Modifier.height(80.dp)) // Padding for floating nav bar
    }
}

@Composable
fun UploadTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF4F2F9),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Text("⬆", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Upload Logs & Info",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C243B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Envie os logs e relatórios dos seus testes nativos diretamente para o dashboard do desenvolvedor.",
                    fontSize = 14.sp,
                    color = Color(0xFF706484),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { /* Action */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF825DFF))
                ) {
                    Text("Selecionar Arquivos", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun LibraryTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Feature Library",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2C243B)
        )
        Text(
            text = "Componentes modulares de teste para Android",
            fontSize = 14.sp,
            color = Color(0xFF8B8496)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recentes",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C243B)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal items (matching the top row in the image mockup)
        val recents = listOf(
            Pair("Biometrics Helper", "🔐"),
            Pair("Camera preview", "📷"),
            Pair("Secure storage", "💾")
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recents) { item ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(180.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F2F9))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFE5DFFF),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(item.second, fontSize = 28.sp)
                            }
                        }
                        Column {
                            Text(
                                text = item.first,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C243B)
                            )
                            Text(
                                text = "Kotlin source",
                                fontSize = 11.sp,
                                color = Color(0xFF8B8496)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Todas Categorias",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C243B)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Two vertical items in a row (matching the bottom image mockup layout)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F2F9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🔑", fontSize = 28.sp)
                    Column {
                        Text("Security", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C243B))
                        Text("Cryptography", fontSize = 12.sp, color = Color(0xFF8B8496))
                    }
                }
            }
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F2F9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🛰️", fontSize = 28.sp)
                    Column {
                        Text("Sensors", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C243B))
                        Text("Hardware API", fontSize = 12.sp, color = Color(0xFF8B8496))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ProfileTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(Color(0xFFE5DFFF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("👤", fontSize = 44.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "João Pablo",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C243B)
        )
        Text(
            text = "joaopablo@mobiletoolkit.com",
            fontSize = 14.sp,
            color = Color(0xFF8B8496)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Profile options (using rounded items/cards)
        val options = listOf(
            Pair("Configurações do App", "⚙️"),
            Pair("Salvar Relatório de Testes", "📁"),
            Pair("Sobre o Mobile Toolkit", "ℹ️")
        )
        
        options.forEach { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F2F9))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.second, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item.first,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2C243B)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(80.dp))
    }
}
