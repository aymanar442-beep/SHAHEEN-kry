package com.example.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.model.FluctuationDirection
import com.example.model.MarketPriceAlert
import com.example.model.ThresholdBreachType
import java.util.Locale

object AlertNotificationManager {
  const val CHANNEL_ID = "shaheen_price_fluctuations"
  private const val CHANNEL_NAME = "تنبيهات تقلبات الأسعار (Price Fluctuation Alerts)"
  private const val CHANNEL_DESC = "إشعارات فورية عند تجاوز تذبذب أسعار العملات الرقمية النسبة المحددة من قبل المستخدم"

  fun ensureNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
        description = CHANNEL_DESC
        enableVibration(true)
        vibrationPattern = longArrayOf(0, 250, 100, 250)
      }
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun postPriceFluctuationNotification(
    context: Context,
    alert: MarketPriceAlert,
    soundEnabled: Boolean = true
  ) {
    try {
      ensureNotificationChannel(context)

      // Permission check for Android 13+
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionCheck = ContextCompat.checkSelfPermission(
          context,
          android.Manifest.permission.POST_NOTIFICATIONS
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
          return // Cannot post system notification without permission
        }
      }

      val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
      }
      val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )

      val directionSymbol = when (alert.fluctuationDirection) {
        FluctuationDirection.SURGE_ONLY -> "▲ صعود حاد"
        FluctuationDirection.DROP_ONLY -> "▼ هبوط حاد"
        FluctuationDirection.BOTH -> if (alert.deltaPercent >= 0) "▲ قفزة سعرية" else "▼ انخفاض سريع"
      }

      val formattedPrice = String.format(Locale.US, "$%,.2f", alert.triggerPrice)
      val formattedDelta = String.format(Locale.US, "%+.2f%%", alert.deltaPercent)

      val title = "⚡ شاهين APEX: تذبذب سعري ($formattedDelta) في ${alert.pair}"
      val summary = "السعر الحالي: $formattedPrice ($directionSymbol) | الإجراء: ${alert.suggestedAction}"

      val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setContentTitle(title)
        .setContentText(summary)
        .setStyle(
          NotificationCompat.BigTextStyle()
            .bigText("$summary\n• نوع التنبيه: ${alert.breachType.labelAr}\n• التوقيت: ${alert.timestamp}")
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(if (soundEnabled) NotificationCompat.DEFAULT_ALL else NotificationCompat.DEFAULT_LIGHTS)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

      val notificationId = (System.currentTimeMillis() % 100000).toInt()
      NotificationManagerCompat.from(context).notify(notificationId, notification)
    } catch (_: Exception) {
      // Gracefully ignore notification delivery failures
    }
  }
}
