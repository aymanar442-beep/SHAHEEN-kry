package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

object HapticFeedbackHelper {

  fun performClickHaptic(haptic: HapticFeedback?, context: Context? = null) {
    try {
      haptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    } catch (_: Exception) {}
  }

  fun performSuccessHaptic(context: Context) {
    try {
      val vibrator = getVibrator(context)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        vibrator?.vibrate(
          VibrationEffect.createWaveform(
            longArrayOf(0, 40, 50, 70),
            intArrayOf(0, 160, 0, 255),
            -1
          )
        )
      } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(longArrayOf(0, 50, 40, 70), -1)
      }
    } catch (_: Exception) {}
  }

  fun performAlertHaptic(context: Context) {
    try {
      val vibrator = getVibrator(context)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
      } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(120)
      }
    } catch (_: Exception) {}
  }

  fun performHeavyActionHaptic(context: Context) {
    try {
      val vibrator = getVibrator(context)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
      } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(80)
      }
    } catch (_: Exception) {}
  }

  private fun getVibrator(context: Context): Vibrator? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
      vibratorManager?.defaultVibrator
    } else {
      @Suppress("DEPRECATION")
      context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
  }
}
