package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FluctuationDirection
import com.example.model.MarketPriceAlert
import com.example.model.ThresholdBreachType
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.ConsoleCyan
import com.example.ui.theme.FalconBlue
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.InactiveCrimson
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.ShaheenSurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.util.Locale

@Composable
fun MarketFluctuationBanner(
  alert: MarketPriceAlert?,
  onDismiss: () -> Unit,
  onOpenSettings: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = alert != null,
    enter = fadeIn() + expandVertically(),
    exit = fadeOut() + shrinkVertically(),
    modifier = modifier
  ) {
    if (alert != null) {
      val isPositive = alert.deltaPercent >= 0
      val primaryColor = when {
        alert.breachType == ThresholdBreachType.LOWER_SUPPORT_DROP || !isPositive -> InactiveCrimson
        alert.breachType == ThresholdBreachType.UPPER_BARRIER_CROSS || isPositive -> ActiveEmerald
        else -> FalconCyan
      }

      val infiniteTransition = rememberInfiniteTransition(label = "alert_pulse")
      val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
          animation = tween(700, easing = FastOutSlowInEasing),
          repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
      )

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 6.dp)
          .testTag("market_fluctuation_alert_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceDark),
        border = BorderStroke(1.5.dp, primaryColor)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  primaryColor.copy(alpha = 0.18f),
                  ShaheenSurfaceCard.copy(alpha = 0.95f),
                  primaryColor.copy(alpha = 0.08f)
                )
              )
            )
            .padding(12.dp)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = 0.25f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isPositive) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(20.dp)
                  )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = alert.breachType.labelAr,
                      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                      color = TextWhite
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = primaryColor.copy(alpha = 0.2f),
                      border = BorderStroke(1.dp, primaryColor)
                    ) {
                      Text(
                        text = alert.pair,
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontWeight = FontWeight.Bold,
                          fontFamily = FontFamily.Monospace,
                          fontSize = 9.sp
                        ),
                        color = primaryColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }

                  Text(
                    text = "رصد تذبذب: ${String.format(Locale.US, "%+.2f%%", alert.deltaPercent)} | السعر: $${String.format(Locale.US, "%,.2f", alert.triggerPrice)}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontFamily = FontFamily.Monospace,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    ),
                    color = primaryColor
                  )
                }
              }

              IconButton(
                onClick = onDismiss,
                modifier = Modifier
                  .size(28.dp)
                  .testTag("dismiss_fluctuation_banner_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Dismiss",
                  tint = TextMuted,
                  modifier = Modifier.size(16.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action line & CTA buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = alert.suggestedAction,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = TextWhite.copy(alpha = 0.9f),
                modifier = Modifier.weight(1f)
              )

              Spacer(modifier = Modifier.width(8.dp))

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                  onClick = onOpenSettings,
                  colors = ButtonDefaults.buttonColors(containerColor = FalconBlue),
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                  modifier = Modifier.height(28.dp).testTag("configure_alert_threshold_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(12.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "ضبط النسبة",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
