package com.zurmati.newsapp.activities

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.view.View
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.core.app.ActivityCompat
import com.zurmati.newsapp.R
import com.zurmati.newsapp.databinding.ActivityBiometricBinding

class BiometricActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBiometricBinding.inflate(layoutInflater)
    }

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication Success!")
                    MainActivity.launch(this@BiometricActivity)
                }
            }

    private val biometricManager by lazy {
        BiometricManager.from(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || !hasUserConfiguredBiometric()) {
            MainActivity.launch(this@BiometricActivity)
        } else if (isBiometricSupported()) {
            startAuthenticationFlow()
        } else
            MainActivity.launch(this@BiometricActivity)

        binding.authenticate.setOnClickListener {
            startAuthenticationFlow()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun startAuthenticationFlow() {
        val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(this)
            .setTitle(getString(R.string.app_name))
            .setSubtitle("Authentication is required to continue using the app")
            .setNegativeButton(
                "Cancel",
                this.mainExecutor
            ) { _, _ ->
                binding.txt.text = getString(R.string.sorry_you_wont_continue)
                binding.authenticate.visibility = View.VISIBLE
            }.build()

        biometricPrompt.authenticate(
            getCancellationSignal(),
            mainExecutor,
            authenticationCallback
        )
    }

    private fun getCancellationSignal(): CancellationSignal {
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal
    }


    private fun queryBiometricStatusFromDevice(): Int =
        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)

    private fun hasUserConfiguredBiometric(): Boolean {

        return when (queryBiometricStatusFromDevice()) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> true
        }
    }


    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isBiometricSupported(): Boolean {
        val keyguardManager: KeyguardManager =
            getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            Log.i("MyInfo", "checkBiometricSupport: First")

            return false
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            return false
        } else {
            Log.i("MyInfo", "checkBiometricSupport: Granted")

        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            Log.i("MyInfo", "checkBiometricSupport: has feature")

            true
        } else {
            Log.i("MyInfo", "checkBiometricSupport: no feature")
            true
        }
    }
}