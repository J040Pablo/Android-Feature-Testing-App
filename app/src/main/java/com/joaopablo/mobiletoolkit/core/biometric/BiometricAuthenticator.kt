package com.joaopablo.mobiletoolkit.core.biometric

import android.content.Context
import android.content.ContextWrapper
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Helper class to manage Biometric Authentication workflows.
 */
class BiometricAuthenticator(private val context: Context) {

    private fun findActivity(context: Context): FragmentActivity? {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is FragmentActivity) {
                return currentContext
            }
            currentContext = currentContext.baseContext
        }
        return null
    }

    fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK
        return biometricManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val activity = findActivity(context)
        if (activity == null) {
            onError("Context is not a FragmentActivity")
            return
        }

        val biometricManager = BiometricManager.from(context)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK

        when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onError("Dispositivo sem suporte")
                return
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onError("Dispositivo sem suporte") // Or "Biometria indisponível" - matches "dispositivo sem suporte"
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onError("Biometria não cadastrada")
                return
            }
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Success, continue
            }
            else -> {
                onError("Dispositivo sem suporte")
                return
            }
        }

        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    when (errorCode) {
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                            onError("Usuário cancelou")
                        }
                        BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                            onError("Biometria não cadastrada")
                        }
                        BiometricPrompt.ERROR_HW_NOT_PRESENT,
                        BiometricPrompt.ERROR_HW_UNAVAILABLE -> {
                            onError("Dispositivo sem suporte")
                        }
                        else -> {
                            onError(errString.toString())
                        }
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onError("Autenticação falhou")
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação Biométrica")
            .setSubtitle("Use sua digital ou reconhecimento facial")
            .setAllowedAuthenticators(authenticators)
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
