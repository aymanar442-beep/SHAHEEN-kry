package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.FluctuationDirection
import com.example.model.MarketPriceAlert
import com.example.model.ShaheenConfig
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.ConsoleCyan
import com.example.ui.theme.ConsoleYellow
import com.example.ui.theme.FalconBlue
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.InactiveCrimson
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenMetallicBorderLight
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.ShaheenSurfaceDark
import com.example.ui.theme.ShaheenSurfaceElevated
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.util.Locale

@Composable
fun FluctuationAlertConfigDialog(
  config: ShaheenConfig,
  currentPrice: Double,
  recentAlerts: List<MarketPriceAlert>,
  onPriceAlertsToggle: (Boolean) -> Unit,
  onFluctuationThresholdChange: (Double) -> Unit,
  onDirectionChange: (FluctuationDirection) -> Unit,
  onTimeWindowChange: (Int) -> Unit,
  onSystemNotificationsToggle: (Boolean) -> Unit,
  onAlertSoundToggle: (Boolean) -> Unit,
  onTriggerTestAlert: () -> Unit,
  onDismiss: () -> Unit,
  autoSaveActive: Boolean = false
) {
  val context = LocalContext.current
  var sliderValue by remember(config.fluctuationPercentageThreshold) {
    mutableFloatStateOf(config.fluctuationPercentageThreshold.toFloat())
  }

  val presetValues = listOf(0.15, 0.25, 0.50, 0.75, 1.00, 2.00)
  val timeWindows = listOf(5, 10, 15, 30, 60)

  // Live calculation of dollar delta at current price
  val calculatedDollarDelta = (currentPrice * (sliderValue.toDouble() / 100.0))
  val upperTriggerPrice = currentPrice + calculatedDollarDelta
  val lowerTriggerPrice = currentPrice - calculatedDollarDelta

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .padding(vertical = 16.dp)
        .testTag("fluctuation_alert_config_dialog"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceDark),
      border = BorderStroke(1.5.dp, FalconCyan)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(FalconBlue.copy(alpha = 0.25f))
                .border(1.dp, FalconCyan, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AddAlert,
                contentDescription = null,
                tint = FalconCyan,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "نظام تنبيهات تذبذب الأسعار",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextWhite
              )
              Text(
                text = "Configurable Price Fluctuation Alert System",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontFamily = FontFamily.Monospace,
                  fontSize = 10.sp
                ),
                color = FalconCyan
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp).testTag("close_fluctuation_dialog_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextMuted,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = ShaheenMetallicBorder)
        Spacer(modifier = Modifier.height(14.dp))

        // 1. Master Toggle
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (config.priceAlertsEnabled) FalconBlue.copy(alpha = 0.25f) else ShaheenSurfaceCard,
          border = BorderStroke(1.dp, if (config.priceAlertsEnabled) FalconCyan else ShaheenMetallicBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Icon(
                imageVector = if (config.priceAlertsEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                contentDescription = null,
                tint = if (config.priceAlertsEnabled) ActiveEmerald else TextMuted,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "مراقبة التذبذب والإشعارات الفورية",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                  color = TextWhite
                )
                Text(
                  text = if (config.priceAlertsEnabled) "النظام نشط ويراقب تذبذب كل شمعة وسعر لحظي" else "المراقبة متوقفة حالياً",
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                  color = if (config.priceAlertsEnabled) ActiveEmerald else TextDim
                )
              }
            }

            Switch(
              checked = config.priceAlertsEnabled,
              onCheckedChange = { onPriceAlertsToggle(it) },
              colors = SwitchDefaults.colors(
                checkedThumbColor = TextWhite,
                checkedTrackColor = ActiveEmerald,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = ShaheenSurfaceElevated
              ),
              modifier = Modifier.testTag("fluctuation_alerts_master_switch")
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Threshold Percentage Slider Section
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = ShaheenSurfaceCard,
          border = BorderStroke(1.dp, ShaheenMetallicBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Tune,
                  contentDescription = null,
                  tint = FalconCyan,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "نسبة التذبذب المشغلة للتنبيه (%):",
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  color = TextWhite
                )
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = FalconBlue.copy(alpha = 0.35f),
                border = BorderStroke(1.dp, FalconCyan)
              ) {
                Text(
                  text = String.format(Locale.US, "±%.2f%%", sliderValue),
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                  ),
                  color = FalconCyan,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
              value = sliderValue,
              onValueChange = {
                sliderValue = it
                onFluctuationThresholdChange(it.toDouble())
              },
              valueRange = 0.05f..5.00f,
              steps = 98,
              colors = SliderDefaults.colors(
                thumbColor = FalconCyan,
                activeTrackColor = FalconCyan,
                inactiveTrackColor = ShaheenMetallicBorder
              ),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("fluctuation_threshold_slider")
            )

            // Preset Chips
            Text(
              text = "خيارات سريعة وموصى بها (Presets):",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = TextMuted
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(presetValues) { preset ->
                val isSelected = Math.abs(sliderValue - preset) < 0.02
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSelected) FalconCyan.copy(alpha = 0.25f) else ShaheenSurfaceElevated,
                  border = BorderStroke(1.dp, if (isSelected) FalconCyan else ShaheenMetallicBorderLight),
                  modifier = Modifier.clickable {
                    sliderValue = preset.toFloat()
                    onFluctuationThresholdChange(preset)
                  }
                ) {
                  Text(
                    text = String.format(Locale.US, "±%.2f%%", preset),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      fontFamily = FontFamily.Monospace
                    ),
                    color = if (isSelected) FalconCyan else TextWhite,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Direction Filter Selection
        Text(
          text = "اتجاه التذبذب المطلوب مراقبته (Direction Filter):",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = FalconCyan
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          FluctuationDirection.values().forEach { direction ->
            val isSelected = config.fluctuationDirection == direction
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) FalconBlue.copy(alpha = 0.3f) else ShaheenSurfaceCard,
              border = BorderStroke(1.dp, if (isSelected) ActiveEmerald else ShaheenMetallicBorder),
              modifier = Modifier
                .weight(1f)
                .clickable { onDirectionChange(direction) }
            ) {
              Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  imageVector = when (direction) {
                    FluctuationDirection.BOTH -> Icons.Default.SyncAlt
                    FluctuationDirection.SURGE_ONLY -> Icons.AutoMirrored.Filled.TrendingUp
                    FluctuationDirection.DROP_ONLY -> Icons.AutoMirrored.Filled.TrendingDown
                  },
                  contentDescription = null,
                  tint = when {
                    isSelected && direction == FluctuationDirection.SURGE_ONLY -> ActiveEmerald
                    isSelected && direction == FluctuationDirection.DROP_ONLY -> InactiveCrimson
                    isSelected -> FalconCyan
                    else -> TextMuted
                  },
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = when (direction) {
                    FluctuationDirection.BOTH -> "الكل (±)"
                    FluctuationDirection.SURGE_ONLY -> "صعود (+)"
                    FluctuationDirection.DROP_ONLY -> "هبوط (-)"
                  },
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = if (isSelected) TextWhite else TextMuted,
                  textAlign = TextAlign.Center
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Live Dollar Movement Calculator Box
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = ShaheenSurfaceCard,
          border = BorderStroke(1.dp, FalconBlue.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Speed,
                  contentDescription = null,
                  tint = ConsoleYellow,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "حساب الأثر اللحظي على السعر (BTC/USDT):",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = TextWhite
                )
              }

              Text(
                text = String.format(Locale.US, "$%,.2f", currentPrice),
                style = MaterialTheme.typography.labelSmall.copy(
                  fontFamily = FontFamily.Monospace,
                  color = ConsoleCyan
                )
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Upper Target
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = ShaheenSurfaceElevated,
                border = BorderStroke(1.dp, ActiveEmerald.copy(alpha = 0.4f)),
                modifier = Modifier.weight(1f)
              ) {
                Column(modifier = Modifier.padding(8.dp)) {
                  Text(
                    text = "سقف الصعود (+${String.format(Locale.US, "%.2f", sliderValue)}%)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = ActiveEmerald
                  )
                  Text(
                    text = String.format(Locale.US, "$%,.2f", upperTriggerPrice),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontFamily = FontFamily.Monospace
                    ),
                    color = TextWhite
                  )
                  Text(
                    text = String.format(Locale.US, "(+ $%,.2f)", calculatedDollarDelta),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 8.sp,
                      fontFamily = FontFamily.Monospace
                    ),
                    color = ActiveEmerald
                  )
                }
              }

              // Lower Target
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = ShaheenSurfaceElevated,
                border = BorderStroke(1.dp, InactiveCrimson.copy(alpha = 0.4f)),
                modifier = Modifier.weight(1f)
              ) {
                Column(modifier = Modifier.padding(8.dp)) {
                  Text(
                    text = "قاع الهبوط (-${String.format(Locale.US, "%.2f", sliderValue)}%)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = InactiveCrimson
                  )
                  Text(
                    text = String.format(Locale.US, "$%,.2f", lowerTriggerPrice),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontFamily = FontFamily.Monospace
                    ),
                    color = TextWhite
                  )
                  Text(
                    text = String.format(Locale.US, "(- $%,.2f)", calculatedDollarDelta),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 8.sp,
                      fontFamily = FontFamily.Monospace
                    ),
                    color = InactiveCrimson
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Time Window and Channel Options
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Time Window
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = ShaheenSurfaceCard,
            border = BorderStroke(1.dp, ShaheenMetallicBorder),
            modifier = Modifier.weight(1f)
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "نافذة القياس الزمنية:",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = TextMuted
              )
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                timeWindows.forEach { sec ->
                  val isSelected = config.fluctuationTimeWindowSeconds == sec
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isSelected) FalconCyan.copy(alpha = 0.3f) else ShaheenSurfaceElevated,
                    border = BorderStroke(1.dp, if (isSelected) FalconCyan else ShaheenMetallicBorderLight),
                    modifier = Modifier
                      .weight(1f)
                      .clickable { onTimeWindowChange(sec) }
                  ) {
                    Text(
                      text = "${sec}s",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                      ),
                      color = if (isSelected) FalconCyan else TextWhite,
                      textAlign = TextAlign.Center,
                      modifier = Modifier.padding(vertical = 4.dp)
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // System Notification and Sound Toggles
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // System Notification toggle
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = ShaheenSurfaceCard,
            border = BorderStroke(1.dp, ShaheenMetallicBorder),
            modifier = Modifier.weight(1f)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "إشعار نظام Android",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = TextWhite
                )
                Text(
                  text = "Heads-Up Notification",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                  ),
                  color = FalconCyan
                )
              }
              Switch(
                checked = config.systemNotificationsEnabled,
                onCheckedChange = { onSystemNotificationsToggle(it) },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = TextWhite,
                  checkedTrackColor = FalconBlue,
                  uncheckedThumbColor = TextMuted,
                  uncheckedTrackColor = ShaheenSurfaceElevated
                ),
                modifier = Modifier.size(36.dp)
              )
            }
          }

          // Sound Chime toggle
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = ShaheenSurfaceCard,
            border = BorderStroke(1.dp, ShaheenMetallicBorder),
            modifier = Modifier.weight(1f)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "رنين صوت واهتزاز",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = TextWhite
                )
                Text(
                  text = "High-Priority Chime",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                  ),
                  color = FalconCyan
                )
              }
              Switch(
                checked = config.fluctuationAlertSoundEnabled,
                onCheckedChange = { onAlertSoundToggle(it) },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = TextWhite,
                  checkedTrackColor = FalconBlue,
                  uncheckedThumbColor = TextMuted,
                  uncheckedTrackColor = ShaheenSurfaceElevated
                ),
                modifier = Modifier.size(36.dp)
              )
            }
          }
        }

        // Auto Save Toast feedback
        AnimatedVisibility(visible = autoSaveActive) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = ActiveEmerald.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, ActiveEmerald),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ActiveEmerald,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "تم حفظ إعدادات التنبيه تلقائياً وتحديث محرك المراقبة",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = ActiveEmerald
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons: Test Alert & Save/Done
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              onTriggerTestAlert()
              Toast.makeText(context, "تم إطلاق اختبار تنبيه بنسبة ±${String.format(Locale.US, "%.2f", sliderValue)}%", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("test_fluctuation_alert_button"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, FalconCyan)
          ) {
            Icon(
              imageVector = Icons.Default.Campaign,
              contentDescription = null,
              tint = FalconCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "تجربة إشعار فوري",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = TextWhite
            )
          }

          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = FalconBlue),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("save_and_close_fluctuation_alert_button")
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = TextWhite,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "تطبيق وإغلاق",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = TextWhite
            )
          }
        }
      }
    }
  }
}
