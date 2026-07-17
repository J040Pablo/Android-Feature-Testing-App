package com.joaopablo.mobiletoolkit.core.camera

import android.content.Context
import androidx.lifecycle.LifecycleOwner

/**
 * Controller class to manage CameraX lifecycle, preview, and image capture.
 * To be implemented in detail during CameraX integration phase.
 */
class CameraController(private val context: Context) {
    fun startCamera(lifecycleOwner: LifecycleOwner) {
        // Placeholder implementation
    }

    fun takePicture(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        // Placeholder implementation
    }

    fun stopCamera() {
        // Placeholder implementation
    }
}
