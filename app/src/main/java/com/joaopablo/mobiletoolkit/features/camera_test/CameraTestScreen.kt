package com.joaopablo.mobiletoolkit.features.camera_test

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.joaopablo.mobiletoolkit.components.ToolkitTopBar
import com.joaopablo.mobiletoolkit.features.camera_test.components.CameraPreview

// ── State machine ──────────────────────────────────────────────────────────────
private sealed class CameraUiState {
    data object Idle : CameraUiState()
    data object Active : CameraUiState()
    data object PermissionDenied : CameraUiState()
    data class Error(val message: String) : CameraUiState()
}

// ── Screen ─────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraTestScreen(navController: NavController) {
    val context = LocalContext.current

    var cameraUiState by remember { mutableStateOf<CameraUiState>(CameraUiState.Idle) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    // ── Permission launcher ───────────────────────────────────────────────────
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraUiState = if (granted) CameraUiState.Active else CameraUiState.PermissionDenied
    }

    fun requestCameraOrOpen() {
        try {
            val alreadyGranted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

            if (alreadyGranted) {
                cameraUiState = CameraUiState.Active
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        } catch (e: Throwable) {
            cameraUiState = CameraUiState.Error("Erro ao verificar permissão: ${e.message}")
        }
    }

    // ── Scaffold — mesmo padrão de todas as telas ─────────────────────────────
    Scaffold(
        topBar = {
            ToolkitTopBar(
                title = "Teste CameraX 📷",
                onBack = { navController.popBackStack() }
            )
        },
        containerColor = Color(0xFFF7F5FC)  // mesmo background de todas as telas
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Ícone (mesmo padrão da Biometria e Notificações) ──────────────
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE5DFFF), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📷", fontSize = 44.sp)
            }

            // ── Título + descrição ────────────────────────────────────────────
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Camera Preview",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C243B)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Visualize o feed ao vivo da câmera nativa usando CameraX. " +
                            "Alterne entre câmera frontal e traseira.",
                    fontSize = 14.sp,
                    color = Color(0xFF706484),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            // ── Área do preview (Card escuro para contraste do feed) ──────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1A1025)),
                contentAlignment = Alignment.Center
            ) {
                when (val state = cameraUiState) {

                    is CameraUiState.Idle -> CameraIdlePlaceholder()

                    is CameraUiState.Active -> {
                        CameraPreview(
                            modifier = Modifier.fillMaxSize(),
                            lensFacing = lensFacing,
                            onError = { msg -> cameraUiState = CameraUiState.Error(msg) }
                        )
                        // Overlay: alternar lente
                        IconButton(
                            onClick = {
                                lensFacing =
                                    if (lensFacing == CameraSelector.LENS_FACING_BACK)
                                        CameraSelector.LENS_FACING_FRONT
                                    else CameraSelector.LENS_FACING_BACK
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                                .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Cameraswitch,
                                contentDescription = "Alternar câmera",
                                tint = Color.White
                            )
                        }
                    }

                    is CameraUiState.PermissionDenied -> CameraPermissionDenied()

                    is CameraUiState.Error -> CameraErrorMessage(state.message)
                }
            }

            // ── Status badge (mesma animação já existente) ────────────────────
            AnimatedVisibility(
                visible = cameraUiState != CameraUiState.Idle,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CameraStatusBadge(cameraUiState)
            }

            // ── Botão principal (mesmo estilo das demais telas) ───────────────
            val isOpen = cameraUiState is CameraUiState.Active

            Button(
                onClick = {
                    if (isOpen) cameraUiState = CameraUiState.Idle else requestCameraOrOpen()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOpen) Color(0xFFE53935) else Color(0xFF825DFF)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = if (isOpen) "Fechar Câmera" else "Abrir Câmera",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Helper composables ─────────────────────────────────────────────────────────

@Composable
private fun CameraIdlePlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(text = "📷", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Toque em Abrir Câmera para iniciar o preview ao vivo.",
            color = Color(0xFF706484),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun CameraPermissionDenied() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Text(text = "❌", fontSize = 36.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Permissão negada",
            color = Color(0xFFE53935),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Vá em Configurações › Aplicativos › MobileToolkit e conceda a permissão de câmera.",
            color = Color(0xFF8B8496),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun CameraErrorMessage(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Text(text = "⚠️", fontSize = 36.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Erro ao iniciar câmera",
            color = Color(0xFFFFA000),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            color = Color(0xFF8B8496),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun CameraStatusBadge(state: CameraUiState) {
    val (icon, label, color) = when (state) {
        is CameraUiState.Active           -> Triple("✅", "Câmera ativa",     Color(0xFF4CAF50))
        is CameraUiState.PermissionDenied -> Triple("❌", "Permissão negada", Color(0xFFE53935))
        is CameraUiState.Error            -> Triple("⚠️", "Erro ao iniciar",  Color(0xFFFFA000))
        else                              -> Triple("",   "",                  Color.Transparent)
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.15f),
        modifier = Modifier.wrapContentWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = icon, fontSize = 15.sp)
            Text(
                text = label,
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
