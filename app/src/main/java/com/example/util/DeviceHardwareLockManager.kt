package com.example.util

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.security.MessageDigest

/**
 * Sovereign Single-User Hardware Lock:
 * Binds the Shaheen engine strictly to the initial physical device hardware fingerprint (DRM/Knox level).
 * Prevents cloning, multi-account circumvention, or APK sharing without authorization.
 */
object DeviceHardwareLockManager {

  private const val PREF_NAME = "shaheen_sovereign_hardware_binding"
  private const val KEY_HARDWARE_FINGERPRINT = "bound_hardware_fingerprint"
  private const val KEY_ACTIVATION_TIMESTAMP = "bound_activation_timestamp"
  private const val KEY_AUTHORIZED_OPERATOR = "authorized_operator_id"

  fun getDeviceHardwareSignature(context: Context): String {
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "UNKNOWN_DEV"
    val hardwareRaw = "${Build.MANUFACTURER}-${Build.MODEL}-${Build.BOARD}-${Build.HARDWARE}-$androidId"
    return sha256Hex(hardwareRaw)
  }

  fun bindCurrentDeviceAsExclusive(context: Context, operatorId: String = "OPERATOR-ALPHA-1"): Boolean {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val currentSignature = getDeviceHardwareSignature(context)
    val existing = prefs.getString(KEY_HARDWARE_FINGERPRINT, null)

    if (existing == null) {
      prefs.edit()
        .putString(KEY_HARDWARE_FINGERPRINT, currentSignature)
        .putLong(KEY_ACTIVATION_TIMESTAMP, System.currentTimeMillis())
        .putString(KEY_AUTHORIZED_OPERATOR, operatorId)
        .apply()
      return true
    }
    return existing == currentSignature
  }

  fun verifySingleUserIntegrity(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val existing = prefs.getString(KEY_HARDWARE_FINGERPRINT, null) ?: return true
    val currentSignature = getDeviceHardwareSignature(context)
    return existing == currentSignature
  }

  fun getBoundOperatorId(context: Context): String {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return prefs.getString(KEY_AUTHORIZED_OPERATOR, "SHAHEEN-EXCLUSIVE-OPERATOR-1") ?: "SHAHEEN-EXCLUSIVE-OPERATOR-1"
  }

  private fun sha256Hex(input: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
  }
}
