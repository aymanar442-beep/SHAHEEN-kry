package com.example

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.data.AlertNotificationManager
import com.example.ui.ShaheenDashboardScreen
import com.example.ui.ShaheenViewModel
import com.example.ui.platform.ShaheenPlatformShowcaseScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val shaheenViewModel: ShaheenViewModel by viewModels()

  private val requestNotificationPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
      // Handled automatically by ViewModel & Notification Manager
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE)
    enableEdgeToEdge()

    // Initialize notification channel for real-time market price fluctuation alerts
    AlertNotificationManager.ensureNotificationChannel(this)

    // Enforce Sovereign Single-User Physical Hardware Lock
    com.example.util.DeviceHardwareLockManager.bindCurrentDeviceAsExclusive(this, "SHAHEEN-LICENSED-OPERATOR-1")

    // Start Sovereign Ghost Mode Foreground Service
    val serviceIntent = android.content.Intent(this, com.example.service.ShaheenForegroundService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(serviceIntent)
    } else {
        startService(serviceIntent)
    }

    // Request notification permission on Android 13+ (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(
          this,
          android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
      }
    }

    setContent {
      MyApplicationTheme {
        ShaheenDashboardScreen(viewModel = shaheenViewModel)
      }
    }
  }
}

