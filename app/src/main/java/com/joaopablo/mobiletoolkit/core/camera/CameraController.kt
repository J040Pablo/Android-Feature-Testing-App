package com.joaopablo.mobiletoolkit.core.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "CameraController"

/**
 * Manages CameraX lifecycle, preview binding, and teardown.
 *
 * Thread-safety: [startCamera] is guarded by an [AtomicBoolean] so that
 * concurrent calls (e.g. from rapid recompositions) are silently ignored
 * until the in-flight initialisation completes.
 *
 * Usage:
 *   val controller = CameraController(context)
 *   controller.startCamera(lifecycleOwner, previewView)
 *   // Cleanup: call stopCamera() in DisposableEffect.onDispose
 */
class CameraController(private val context: Context) {

    private var cameraProvider: ProcessCameraProvider? = null

    /**
     * Guard that prevents multiple simultaneous calls to [ProcessCameraProvider.getInstance].
     * compareAndSet(false, true) succeeds only if the flag was false; duplicate calls return
     * immediately.  The flag is always reset in the `finally` block or in [stopCamera].
     */
    private val isStarting = AtomicBoolean(false)

    /**
     * Starts the camera preview and binds it to [lifecycleOwner].
     *
     * Duplicate / concurrent calls are ignored safely — [isStarting] acts as a
     * lightweight mutex without blocking threads.
     *
     * @param lifecycleOwner  The Compose / Fragment lifecycle owner.
     * @param previewView     The [PreviewView] that will display the camera feed.
     * @param lensFacing      [CameraSelector.LENS_FACING_BACK] (default) or FRONT.
     * @param onError         Called on the main thread with a descriptive message on failure.
     */
    fun startCamera(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        lensFacing: Int = CameraSelector.LENS_FACING_BACK,
        onError: (String) -> Unit = {}
    ) {
        // Reject duplicate in-flight requests
        if (!isStarting.compareAndSet(false, true)) {
            Log.w(TAG, "startCamera() ignorado — inicialização já em progresso.")
            return
        }

        val providerFuture = ProcessCameraProvider.getInstance(context)

        providerFuture.addListener({
            try {
                val provider = providerFuture.get()
                cameraProvider = provider

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build()

                // Release any existing use-cases before rebinding
                provider.unbindAll()

                // Guard: skip bind if stopCamera() was already called while the
                // Future was in-flight (race between onDispose and the async callback)
                if (cameraProvider == null) {
                    Log.w(TAG, "stopCamera() chamado antes do bind — abortando.")
                    return@addListener
                }

                provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )

                Log.d(TAG, "Câmera vinculada com sucesso (lens=$lensFacing).")
            } catch (e: Throwable) {
                // Catch Throwable (not just Exception) because CameraX native
                // internals can throw UnsatisfiedLinkError / ExceptionInInitializerError
                // which bypass Exception handlers and would otherwise crash the app.
                Log.e(TAG, "Falha ao vincular câmera: ${e.message}", e)
                onError("Erro ao iniciar câmera: ${e.message ?: "erro desconhecido"}")
            } finally {
                // Always release the guard — success or failure
                isStarting.set(false)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    /**
     * Releases all camera resources.
     * Call this inside [androidx.compose.runtime.DisposableEffect]'s `onDispose` block.
     */
    fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraProvider = null
        isStarting.set(false) // reset guard in case stop is called mid-init
        Log.d(TAG, "Câmera liberada.")
    }
}
