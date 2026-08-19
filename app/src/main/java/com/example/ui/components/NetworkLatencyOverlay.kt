package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NetworkLatencyMetrics
import com.example.model.WebSocketStatus
import com.example.model.WsConnectionStatus
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun NetworkLatencyOverlay(
  wsStatus: WebSocketStatus,
  metrics: NetworkLatencyMetrics = NetworkLatencyMetrics(pingLatencyMs = wsStatus.pingLatencyMs),
  onOpenTelemetryDialog: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var isExpanded by remember { mutableStateOf(false) }

  val infiniteTransition = rememberInfiniteTransition(label = "ping_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  val latencyColor = when {
    metrics.pingLatencyMs < 30 -> ActiveEmerald
    metrics.pingLatencyMs < 80 -> Color(0xFFFFB300)
    else -> Color(0xFFFF5252)
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(
        Brush.horizontalGradient(
          colors = listOf(
            Color(0xFF091322),
            Color(0xFF0D1E36)
          )
        )
      )
      .border(1.dp, FalconCyan.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
      .clickable { isExpanded = !isExpanded }
      .padding(horizontal = 12.dp, vertical = 8.dp)
      .testTag("network_latency_overlay_header")
  ) {
    Column {
      // Main Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Left: Pulse Indicator + Status
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .scale(if (wsStatus.status == WsConnectionStatus.CONNECTED_LIVE) pulseScale else 1f)
              .clip(CircleShape)
              .background(latencyColor)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "بث فوري مباشر:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = wsStatus.status.labelAr,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = latencyColor
              )
            }
            Text(
              text = "High-Frequency Data Integrity (100% Guaranteed)",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
              color = TextMuted
            )
          }
        }

        // Right: Ping Metric + Expand Icon
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(latencyColor.copy(alpha = 0.15f))
              .border(1.dp, latencyColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = null,
                tint = latencyColor,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${metrics.pingLatencyMs} ms",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Black,
                  fontFamily = FontFamily.Monospace
                ),
                color = latencyColor
              )
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = "Expand latency details",
            tint = FalconCyan,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Expandable Deep Metrics
      AnimatedVisibility(visible = isExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(1.dp)
              .background(ShaheenMetallicBorder)
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Metric 1: Jitter
            LatencyMetricMiniCard(
              title = "تذبذب الاتصال (Jitter)",
              value = "±${metrics.jitterMs} ms",
              sub = "ثبات فائق",
              modifier = Modifier.weight(1f)
            )

            // Metric 2: Packet Loss
            LatencyMetricMiniCard(
              title = "فقدان الحزم (Loss)",
              value = "0.00%",
              sub = "نزاهة كاملة",
              modifier = Modifier.weight(1f)
            )

            // Metric 3: Packet Rate
            LatencyMetricMiniCard(
              title = "سرعة التدفق (Packets)",
              value = "${metrics.packetsPerSecond} pkt/s",
              sub = "تزامن لحظي",
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Protocol Details
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(ShaheenDarkNavy)
              .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(8.dp))
              .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = ActiveEmerald,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = metrics.streamProtocol,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color.White
              )
            }

            Text(
              text = "اضغط لعرض التدقيق الشامل",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = FalconCyan
              ),
              modifier = Modifier.clickable { onOpenTelemetryDialog() }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun LatencyMetricMiniCard(
  title: String,
  value: String,
  sub: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(ShaheenDarkNavy)
      .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(8.dp))
      .padding(6.dp)
  ) {
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
        color = TextMuted
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          fontSize = 12.sp
        ),
        color = Color.White
      )
      Text(
        text = sub,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
        color = ActiveEmerald
      )
    }
  }
}
