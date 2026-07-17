package com.joaopablo.mobiletoolkit.features.biometric_test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
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
import com.joaopablo.mobiletoolkit.core.biometric.BiometricAuthenticator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiometricTestScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val authenticator = remember { BiometricAuthenticator(context) }
    
    var authStatusMessage by remember { mutableStateOf<String?>(null) }
    var authStatusType by remember { mutableStateOf<AuthStatusType>(AuthStatusType.IDLE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teste Biometria 🔐", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rounded Icon Container for biometric
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE5DFFF), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Fingerprint Preview",
                    tint = Color(0xFF825DFF),
                    modifier = Modifier.size(56.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Biometric Authenticator",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C243B)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Teste a integração nativa com o Android BiometricPrompt usando biometria forte ou fraca (impressão digital e reconhecimento facial).",
                    fontSize = 14.sp,
                    color = Color(0xFF706484),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            // Status Indicator UI Block
            StatusCard(type = authStatusType, message = authStatusMessage)

            Spacer(modifier = Modifier.height(8.dp))

            // Action Button
            Button(
                onClick = {
                    authStatusType = AuthStatusType.AUTHENTICATING
                    authStatusMessage = "Autenticando..."
                    
                    authenticator.authenticate(
                        onSuccess = {
                            authStatusType = AuthStatusType.SUCCESS
                            authStatusMessage = "Autenticado com sucesso!"
                        },
                        onError = { error ->
                            when (error) {
                                "Usuário cancelou" -> {
                                    authStatusType = AuthStatusType.CANCELLED
                                    authStatusMessage = "Autenticação cancelada pelo usuário."
                                }
                                "Biometria não cadastrada" -> {
                                    authStatusType = AuthStatusType.NOT_ENROLLED
                                    authStatusMessage = "Nenhuma biometria cadastrada neste dispositivo."
                                }
                                "Dispositivo sem suporte" -> {
                                    authStatusType = AuthStatusType.NOT_SUPPORTED
                                    authStatusMessage = "Este dispositivo não possui suporte a biometria."
                                }
                                else -> {
                                    authStatusType = AuthStatusType.FAILED
                                    authStatusMessage = error
                                }
                            }
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF825DFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Autenticar Agora",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

enum class AuthStatusType {
    IDLE,
    AUTHENTICATING,
    SUCCESS,
    CANCELLED,
    NOT_ENROLLED,
    NOT_SUPPORTED,
    FAILED
}

@Composable
fun StatusCard(type: AuthStatusType, message: String?) {
    val (backgroundColor, contentColor, icon, statusTitle) = when (type) {
        AuthStatusType.IDLE -> Quadruple(
            Color(0xFFF4F2F9),
            Color(0xFF706484),
            Icons.Default.Info,
            "Não Testado"
        )
        AuthStatusType.AUTHENTICATING -> Quadruple(
            Color(0xFFE5DFFF),
            Color(0xFF825DFF),
            Icons.Default.Fingerprint,
            "Autenticando"
        )
        AuthStatusType.SUCCESS -> Quadruple(
            Color(0xFFE8F5E9),
            Color(0xFF2E7D32),
            Icons.Default.CheckCircle,
            "Sucesso"
        )
        AuthStatusType.CANCELLED -> Quadruple(
            Color(0xFFFFF3E0),
            Color(0xFFEF6C00),
            Icons.Default.Info,
            "Cancelado"
        )
        AuthStatusType.NOT_ENROLLED -> Quadruple(
            Color(0xFFFFEBEE),
            Color(0xFFC62828),
            Icons.Default.Error,
            "Não Cadastrado"
        )
        AuthStatusType.NOT_SUPPORTED -> Quadruple(
            Color(0xFFFFEBEE),
            Color(0xFFC62828),
            Icons.Default.Cancel,
            "Sem Suporte"
        )
        AuthStatusType.FAILED -> Quadruple(
            Color(0xFFFFEBEE),
            Color(0xFFC62828),
            Icons.Default.Error,
            "Falhou"
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, contentColor.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = statusTitle,
                tint = contentColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = statusTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = message ?: "Toque no botão abaixo para testar.",
                    fontSize = 13.sp,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
