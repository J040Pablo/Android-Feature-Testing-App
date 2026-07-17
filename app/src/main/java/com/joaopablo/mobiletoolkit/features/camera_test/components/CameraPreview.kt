package com.joaopablo.mobiletoolkit.features.camera_test.components

import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.joaopablo.mobiletoolkit.core.camera.CameraController

/**
 * Composable that renders a live CameraX preview.
 *
 * ## Architecture
 * - [AndroidView] **only** creates the [PreviewView] inside `factory`. The `update` callback
 *   is intentionally omitted to prevent CameraX from being re-initialised on every recomposition.
 * - Camera initialisation is driven exclusively by [DisposableEffect], which fires once per
 *   unique `(lifecycleOwner, lensFacing)` pair and cleans up via `onDispose` when either
 *   value changes or the composable leaves the composition.
 *
 * ## Lifecycle
 * - Camera starts → [CameraController.startCamera] (guarded by [AtomicBoolean] internally)
 * - Camera stops  → [CameraController.stopCamera]  (called in `onDispose`)
 *
 * @param modifier       Standard Compose modifier (size, shape, clip, etc.).
 * @param lensFacing     [CameraSelector.LENS_FACING_BACK] (default) or FRONT.
 *                       Changing this value triggers a camera rebind automatically.
 * @param onError        Called on the main thread with a human-readable message on failure.
 */
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    onError: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Controller is stable across recompositions — created once per composition
    val cameraController = remember { CameraController(context) }

    // PreviewView is created once and reused — no camera logic here
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    // ── Camera lifecycle ──────────────────────────────────────────────────────
    // DisposableEffect re-runs when lifecycleOwner or lensFacing changes.
    // This is the ONLY place where startCamera() / stopCamera() are called.
    DisposableEffect(lifecycleOwner, lensFacing) {
        cameraController.startCamera(
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            lensFacing = lensFacing,
            onError = onError
        )
        onDispose {
            cameraController.stopCamera()
        }
    }

    // ── View ─────────────────────────────────────────────────────────────────
    // factory only — update{} is omitted intentionally.
    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}
