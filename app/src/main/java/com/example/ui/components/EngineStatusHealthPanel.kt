package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.entity.EngineStatusEntity
import com.example.model.EngineStatus
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.ActiveEmeraldDark
import com.example.ui.theme.ConsoleGreen
import com.example.ui.theme.FalconBlue
import com.example.ui.theme.FalconBlueLight
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.InactiveCrimson
import com.example.ui.theme.ShaheenBackground
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenMetallicBorderLight
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.ShaheenSurfaceDark
import com.example.ui.theme.ShaheenSurfaceElevated
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningAmber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Visual component displaying the engine status directly from the Room Database cache,
 * highlighting real-time connectivity, latency metrics, and operational health indicators.
 */
@Composable
fun EngineStatusHealthPanel(
    statusEntity: EngineStatusEntity?,
    modifier: Modifier = Modifier
) {
    val status = statusEntity?.toDomain() ?: EngineStatus()
    val lastUpdatedText = statusEntity?.let {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.US)
        sdf.format(Date(it.lastUpdated))
    } ?: "مباشر"

    // Infinite breathing/pulsing animation for live connectivity dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween( durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("engine_status_health_panel"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
        border = BorderStroke(1.dp, ShaheenMetallicBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ShaheenSurfaceElevated.copy(alpha = 0.6f),
                            ShaheenSurfaceCard
                        )
                    )
                )
                .padding(14.dp)
        ) {
            // Header: Title, Room DB Badge & Live Link Pulse
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(FalconBlue.copy(alpha = 0.3f), FalconCyan.copy(alpha = 0.15f))
                                )
                            )
                            .border(1.dp, FalconBlue.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = "Operational Health",
                            tint = FalconCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "مؤشرات الحالة والاتصال الحي",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Room SQLite Engine Status Cache",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Room DB Live Status Indicator
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ShaheenSurfaceDark,
                    border = BorderStroke(1.dp, if (status.isRunning) ActiveEmerald.copy(alpha = 0.5f) else ShaheenMetallicBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (status.isRunning) ActiveEmerald else InactiveCrimson)
                                .alpha(if (status.isRunning) pulseAlpha else 0.8f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (status.isRunning) "متصل (Room Live)" else "خامل (Cached)",
                            color = if (status.isRunning) ActiveEmerald else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Primary Live Telemetry Row (Latency, Pair, Cycles, Last DB Sync)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ShaheenSurfaceDark)
                    .border(1.dp, ShaheenMetallicBorderLight.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Latency Indicator
                TelemetryMetricItem(
                    label = "استجابة النواة",
                    value = "${status.latencyMs} ms",
                    icon = Icons.Default.Sensors,
                    tint = if (status.latencyMs < 20) ActiveEmerald else WarningAmber
                )

                Box(modifier = Modifier.width(1.dp).height(24.dp).background(ShaheenMetallicBorder))

                // Active Pair
                TelemetryMetricItem(
                    label = "الزوج النشط",
                    value = status.currentPair,
                    icon = Icons.Default.Bolt,
                    tint = FalconCyan
                )

                Box(modifier = Modifier.width(1.dp).height(24.dp).background(ShaheenMetallicBorder))

                // Total Cycles
                TelemetryMetricItem(
                    label = "دورات المعالجة",
                    value = "#${status.totalCycles}",
                    icon = Icons.Default.Sync,
                    tint = TextWhite
                )

                Box(modifier = Modifier.width(1.dp).height(24.dp).background(ShaheenMetallicBorder))

                // Last DB Update
                TelemetryMetricItem(
                    label = "آخر حفظ DB",
                    value = lastUpdatedText,
                    icon = Icons.Default.Storage,
                    tint = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Operational Health Grid (4 Key Security & Stability Layers)
            Text(
                text = "دروع الأمان والجاهزية التشغيلية",
                color = TextDim,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OperationalHealthBadge(
                    modifier = Modifier.weight(1f),
                    title = "Flash Breaker",
                    subtitle = "< 100ms Delta",
                    isActive = status.sub100msFlashBreakerActive,
                    icon = Icons.Default.Shield
                )
                OperationalHealthBadge(
                    modifier = Modifier.weight(1f),
                    title = "Offline Immune",
                    subtitle = "Sovereign Node",
                    isActive = status.isOfflineImmune,
                    icon = Icons.Default.CloudDone
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OperationalHealthBadge(
                    modifier = Modifier.weight(1f),
                    title = "Knox Defense",
                    subtitle = "Biometric Lock",
                    isActive = status.knoxBiometricArmed,
                    icon = Icons.Default.Security
                )
                OperationalHealthBadge(
                    modifier = Modifier.weight(1f),
                    title = "Cold Vault",
                    subtitle = "$${String.format(Locale.US, "%,.0f", status.spotColdVaultUsdt)} USDT",
                    isActive = status.autoSweepVaultSecured,
                    icon = Icons.Default.Lock
                )
            }
        }
    }
}

@Composable
private fun TelemetryMetricItem(
    label: String,
    value: String,
    icon: ImageVector,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = value,
                color = tint,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = TextDim,
            fontSize = 9.sp
        )
    }
}

@Composable
private fun OperationalHealthBadge(
    title: String,
    subtitle: String,
    isActive: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = ShaheenSurfaceDark,
        border = BorderStroke(
            1.dp,
            if (isActive) ActiveEmeraldDark.copy(alpha = 0.35f) else ShaheenMetallicBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) ActiveEmerald.copy(alpha = 0.15f) else InactiveCrimson.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isActive) ActiveEmerald else InactiveCrimson,
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = title,
                    color = TextWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = if (isActive) ActiveEmerald.copy(alpha = 0.8f) else TextDim,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
