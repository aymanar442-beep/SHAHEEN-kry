package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.FluctuationDirection
import com.example.model.ShaheenConfig

class ShaheenPreferences(context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

  companion object {
    private const val PREFS_NAME = "shaheen_secure_settings"
    private const val KEY_USERNAME = "username"
    private const val KEY_LICENSE_KEY = "license_key"
    private const val KEY_API_KEY = "api_key"
    private const val KEY_DISCLAIMER_ACCEPTED = "disclaimer_accepted"
    private const val KEY_EXTRA_USERS = "extra_users_count"
    private const val KEY_PRICE_ALERTS_ENABLED = "price_alerts_enabled"
    private const val KEY_UPPER_THRESHOLD = "upper_price_threshold"
    private const val KEY_LOWER_THRESHOLD = "lower_price_threshold"
    private const val KEY_VOLATILITY_THRESHOLD = "volatility_threshold"
    private const val KEY_FLUCTUATION_PERCENTAGE_THRESHOLD = "fluctuation_percentage_threshold"
    private const val KEY_FLUCTUATION_DIRECTION = "fluctuation_direction"
    private const val KEY_FLUCTUATION_TIME_WINDOW = "fluctuation_time_window_seconds"
    private const val KEY_SYSTEM_NOTIFICATIONS_ENABLED = "system_notifications_enabled"
    private const val KEY_FLUCTUATION_SOUND_ENABLED = "fluctuation_sound_enabled"

    private const val DEFAULT_USERNAME = "ayman"
    private const val DEFAULT_LICENSE = "SH-9924-SEC-ALPHA-88X"
    private const val DEFAULT_API_KEY = "sh_live_k82f990141be297d09873a"
    private const val DEFAULT_UPPER_THRESHOLD = 98520.0
    private const val DEFAULT_LOWER_THRESHOLD = 98320.0
    private const val DEFAULT_VOLATILITY_THRESHOLD = 0.35
    private const val DEFAULT_FLUCTUATION_THRESHOLD = 0.50
    private const val DEFAULT_FLUCTUATION_DIRECTION = "BOTH"
    private const val DEFAULT_FLUCTUATION_TIME_WINDOW = 10
  }

  fun loadConfig(): ShaheenConfig {
    val username = prefs.getString(KEY_USERNAME, DEFAULT_USERNAME) ?: DEFAULT_USERNAME
    val licenseKey = prefs.getString(KEY_LICENSE_KEY, DEFAULT_LICENSE) ?: DEFAULT_LICENSE
    val apiKey = prefs.getString(KEY_API_KEY, DEFAULT_API_KEY) ?: DEFAULT_API_KEY
    val disclaimerAccepted = prefs.getBoolean(KEY_DISCLAIMER_ACCEPTED, false)
    val extraUsers = prefs.getInt(KEY_EXTRA_USERS, 0)
    val priceAlertsEnabled = prefs.getBoolean(KEY_PRICE_ALERTS_ENABLED, true)
    val upperThreshold = prefs.getFloat(KEY_UPPER_THRESHOLD, DEFAULT_UPPER_THRESHOLD.toFloat()).toDouble()
    val lowerThreshold = prefs.getFloat(KEY_LOWER_THRESHOLD, DEFAULT_LOWER_THRESHOLD.toFloat()).toDouble()
    val volatilityThreshold = prefs.getFloat(KEY_VOLATILITY_THRESHOLD, DEFAULT_VOLATILITY_THRESHOLD.toFloat()).toDouble()
    val fluctuationThreshold = prefs.getFloat(KEY_FLUCTUATION_PERCENTAGE_THRESHOLD, DEFAULT_FLUCTUATION_THRESHOLD.toFloat()).toDouble()
    val directionStr = prefs.getString(KEY_FLUCTUATION_DIRECTION, DEFAULT_FLUCTUATION_DIRECTION) ?: DEFAULT_FLUCTUATION_DIRECTION
    val direction = try {
      FluctuationDirection.valueOf(directionStr)
    } catch (_: Exception) {
      FluctuationDirection.BOTH
    }
    val timeWindow = prefs.getInt(KEY_FLUCTUATION_TIME_WINDOW, DEFAULT_FLUCTUATION_TIME_WINDOW)
    val systemNotifications = prefs.getBoolean(KEY_SYSTEM_NOTIFICATIONS_ENABLED, true)
    val soundEnabled = prefs.getBoolean(KEY_FLUCTUATION_SOUND_ENABLED, true)

    return ShaheenConfig(
      username = username,
      licenseKey = licenseKey,
      apiKey = apiKey,
      hasAcceptedDisclaimer = disclaimerAccepted,
      additionalUsersCount = extraUsers,
      priceAlertsEnabled = priceAlertsEnabled,
      upperPriceThreshold = upperThreshold,
      lowerPriceThreshold = lowerThreshold,
      volatilitySpikeThresholdPercent = volatilityThreshold,
      fluctuationPercentageThreshold = fluctuationThreshold,
      fluctuationDirection = direction,
      fluctuationTimeWindowSeconds = timeWindow,
      systemNotificationsEnabled = systemNotifications,
      fluctuationAlertSoundEnabled = soundEnabled
    )
  }

  fun saveConfig(config: ShaheenConfig) {
    prefs.edit()
      .putString(KEY_USERNAME, config.username)
      .putString(KEY_LICENSE_KEY, config.licenseKey)
      .putString(KEY_API_KEY, config.apiKey)
      .putBoolean(KEY_DISCLAIMER_ACCEPTED, config.hasAcceptedDisclaimer)
      .putInt(KEY_EXTRA_USERS, config.additionalUsersCount)
      .putBoolean(KEY_PRICE_ALERTS_ENABLED, config.priceAlertsEnabled)
      .putFloat(KEY_UPPER_THRESHOLD, config.upperPriceThreshold.toFloat())
      .putFloat(KEY_LOWER_THRESHOLD, config.lowerPriceThreshold.toFloat())
      .putFloat(KEY_VOLATILITY_THRESHOLD, config.volatilitySpikeThresholdPercent.toFloat())
      .putFloat(KEY_FLUCTUATION_PERCENTAGE_THRESHOLD, config.fluctuationPercentageThreshold.toFloat())
      .putString(KEY_FLUCTUATION_DIRECTION, config.fluctuationDirection.name)
      .putInt(KEY_FLUCTUATION_TIME_WINDOW, config.fluctuationTimeWindowSeconds)
      .putBoolean(KEY_SYSTEM_NOTIFICATIONS_ENABLED, config.systemNotificationsEnabled)
      .putBoolean(KEY_FLUCTUATION_SOUND_ENABLED, config.fluctuationAlertSoundEnabled)
      .apply()
  }

  fun saveUsername(username: String) {
    prefs.edit().putString(KEY_USERNAME, username).apply()
  }

  fun saveLicenseKey(licenseKey: String) {
    prefs.edit().putString(KEY_LICENSE_KEY, licenseKey).apply()
  }

  fun saveApiKey(apiKey: String) {
    prefs.edit().putString(KEY_API_KEY, apiKey).apply()
  }

  fun saveDisclaimerAccepted(accepted: Boolean) {
    prefs.edit().putBoolean(KEY_DISCLAIMER_ACCEPTED, accepted).apply()
  }

  fun saveExtraUsers(count: Int) {
    prefs.edit().putInt(KEY_EXTRA_USERS, count).apply()
  }

  fun savePriceAlertsEnabled(enabled: Boolean) {
    prefs.edit().putBoolean(KEY_PRICE_ALERTS_ENABLED, enabled).apply()
  }

  fun savePriceThresholds(upper: Double, lower: Double) {
    prefs.edit()
      .putFloat(KEY_UPPER_THRESHOLD, upper.toFloat())
      .putFloat(KEY_LOWER_THRESHOLD, lower.toFloat())
      .apply()
  }

  fun saveVolatilityThreshold(volatility: Double) {
    prefs.edit().putFloat(KEY_VOLATILITY_THRESHOLD, volatility.toFloat()).apply()
  }

  fun saveFluctuationPercentageThreshold(threshold: Double) {
    prefs.edit().putFloat(KEY_FLUCTUATION_PERCENTAGE_THRESHOLD, threshold.toFloat()).apply()
  }

  fun saveFluctuationDirection(direction: FluctuationDirection) {
    prefs.edit().putString(KEY_FLUCTUATION_DIRECTION, direction.name).apply()
  }

  fun saveFluctuationTimeWindow(seconds: Int) {
    prefs.edit().putInt(KEY_FLUCTUATION_TIME_WINDOW, seconds).apply()
  }

  fun saveSystemNotificationsEnabled(enabled: Boolean) {
    prefs.edit().putBoolean(KEY_SYSTEM_NOTIFICATIONS_ENABLED, enabled).apply()
  }

  fun saveFluctuationSoundEnabled(enabled: Boolean) {
    prefs.edit().putBoolean(KEY_FLUCTUATION_SOUND_ENABLED, enabled).apply()
  }

  fun getLastActiveTimestamp(): Long = prefs.getLong("last_active_timestamp", 0L)
  fun saveLastActiveTimestamp(timestamp: Long) {
    prefs.edit().putLong("last_active_timestamp", timestamp).apply()
  }

  fun getCooldownEndTime(): Long = prefs.getLong("cooldown_end_time", 0L)
  fun saveCooldownEndTime(endTime: Long) {
    prefs.edit().putLong("cooldown_end_time", endTime).apply()
  }
}
