package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AssetRotationSignal
import com.example.model.MarketIntelligenceAlert
import com.example.model.MicroScalpTrade
import com.example.ui.theme.*

@Composable
fun PremiumIntelligenceSection(
  trades: List<MicroScalpTrade>,
  rotationSignal: AssetRotationSignal?,
  alerts: List<MarketIntelligenceAlert>,
  isScalpActive: Boolean,
  onToggleScalp: () -> Unit,
  onOpenUpgradeHub: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Scalp, 1: Rotation, 2: Radar News

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceElevated),
    border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(FalconCyan, FalconBlue, ActiveEmerald))),
    modifier = modifier.padding(horizontal = 14.dp, vertical = 6.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(ActiveEmerald.copy(alpha = 0.15f))
              .border(BorderStroke(1.dp, ActiveEmerald), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ElectricBolt,
              contentDescription = null,
              tint = ActiveEmerald,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "محرك البريميوم ورادار الحيتان العالمي",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = TextWhite
            )
            Text(
              text = "MICRO-SCALP • CROSS-EXCHANGE ROTATION • AI RADAR",
              style = MaterialTheme.typography.labelSmall,
              color = FalconCyan,
              fontFamily = FontFamily.Monospace,
              fontSize = 8.5.sp
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = Color(0xFFFFD600).copy(alpha = 0.15f),
          border = BorderStroke(1.dp, Color(0xFFFFD600).copy(alpha = 0.5f))
        ) {
          Text(
            text = "PRO TIER 3",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFFFD600),
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Tab selector
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(ShaheenSurfaceDark)
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        TabButton(
          title = "السكالبينج المجهري",
          icon = Icons.Default.TrendingUp,
          isSelected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          modifier = Modifier.weight(1f)
        )
        TabButton(
          title = "سقف الربح والخسارة",
          icon = Icons.Default.Tune,
          isSelected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          modifier = Modifier.weight(1f)
        )
        TabButton(
          title = "تبديل العملات",
          icon = Icons.Default.SwapHoriz,
          isSelected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          modifier = Modifier.weight(1f)
        )
        TabButton(
          title = "رادار الأخبار",
          icon = Icons.Default.RssFeed,
          isSelected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Tab Content
      when (selectedTab) {
        0 -> MicroScalpingTab(trades = trades, isScalpActive = isScalpActive, onToggle = onToggleScalp)
        1 -> RiskProfitTierTab(onOpenUpgradeHub = onOpenUpgradeHub)
        2 -> AssetRotationTab(signal = rotationSignal, onUpgrade = onOpenUpgradeHub)
        3 -> IntelligenceRadarTab(alerts = alerts)
      }
    }
  }
}

@Composable
private fun TabButton(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(6.dp),
    color = if (isSelected) FalconBlue else Color.Transparent,
    modifier = modifier.clickable { onClick() }
  ) {
    Row(
      modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isSelected) TextWhite else TextDim,
        modifier = Modifier.size(13.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        fontSize = 10.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = if (isSelected) TextWhite else TextDim
      )
    }
  }
}

@Composable
private fun MicroScalpingTab(
  trades: List<MicroScalpTrade>,
  isScalpActive: Boolean,
  onToggle: () -> Unit
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "استراتيجية جني الأرباح المجهرية السريعة (0.3% - 0.6%)",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = TextWhite,
          fontSize = 11.sp
        )
        Text(
          text = "إغلاق الصفقات في ثوانٍ وتراكم العوائد مع وقف خسارة فوري",
          style = MaterialTheme.typography.labelSmall,
          color = TextDim,
          fontSize = 9.5.sp
        )
      }

      Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isScalpActive) ActiveEmerald.copy(alpha = 0.2f) else ShaheenSurfaceDark,
        border = BorderStroke(1.dp, if (isScalpActive) ActiveEmerald else ShaheenMetallicBorder),
        modifier = Modifier.clickable { onToggle() }
      ) {
        Text(
          text = if (isScalpActive) "نشط / التراكم قيد التشغيل" else "متوقف",
          style = MaterialTheme.typography.labelSmall,
          color = if (isScalpActive) ActiveEmerald else TextMuted,
          fontSize = 9.sp,
          fontFamily = FontFamily.Monospace,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // List of quick trades
    trades.forEach { trade ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 3.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(ShaheenSurfaceDark)
          .border(BorderStroke(1.dp, ShaheenMetallicBorder), RoundedCornerShape(6.dp))
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = if (trade.type == "LONG") ActiveEmerald.copy(alpha = 0.2f) else InactiveCrimson.copy(alpha = 0.2f)
          ) {
            Text(
              text = trade.type,
              color = if (trade.type == "LONG") ActiveEmerald else InactiveCrimson,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = trade.pair, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "(${trade.durationSeconds}s)", color = TextDim, fontSize = 9.5.sp, fontFamily = FontFamily.Monospace)
        }

        Text(
          text = "+${trade.profitPercent}%",
          color = ActiveEmerald,
          fontWeight = FontWeight.Black,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }
  }
}

@Composable
private fun RiskProfitTierTab(
  onOpenUpgradeHub: () -> Unit
) {
  var selectedTier by remember { mutableStateOf(com.example.model.RiskTierMode.ULTRA_SAFE) }

  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      text = "تخصيص سقف الربح والخسارة الديناميكي لحماية المحفظة:",
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.Bold,
      color = TextWhite,
      fontSize = 11.sp
    )

    com.example.model.RiskTierMode.values().forEach { tier ->
      val isSelected = selectedTier == tier
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) FalconBlue.copy(alpha = 0.2f) else ShaheenSurfaceDark,
        border = BorderStroke(1.dp, if (isSelected) FalconCyan else ShaheenMetallicBorder),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { selectedTier = tier }
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              RadioButton(
                selected = isSelected,
                onClick = { selectedTier = tier },
                colors = RadioButtonDefaults.colors(
                  selectedColor = FalconCyan,
                  unselectedColor = TextDim
                ),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = tier.titleAr,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) FalconCyan else TextWhite,
                fontSize = 11.sp
              )
            }

            Surface(
              shape = RoundedCornerShape(4.dp),
              color = if (isSelected) ActiveEmerald.copy(alpha = 0.2f) else ShaheenSurfaceElevated
            ) {
              Text(
                text = "الرافعة: ${tier.maxLeverage}x",
                color = if (isSelected) ActiveEmerald else TextMuted,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = tier.description,
            style = MaterialTheme.typography.labelSmall,
            color = TextDim,
            fontSize = 10.sp,
            lineHeight = 13.sp
          )

          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "هدف الربح: ${tier.targetProfitRange}",
              color = ActiveEmerald,
              fontSize = 9.5.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "وقف الخسارة الصارم: ${tier.stopLossLimit}",
              color = InactiveCrimson,
              fontSize = 9.5.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AssetRotationTab(
  signal: AssetRotationSignal?,
  onUpgrade: () -> Unit
) {
  if (signal == null) {
    Text(text = "جاري مسح البورصات العالمية...", color = TextDim, fontSize = 11.sp)
    return
  }

  Column {
    Card(
      shape = RoundedCornerShape(10.dp),
      colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceDark),
      border = BorderStroke(1.dp, FalconCyan.copy(alpha = 0.5f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = signal.currentAsset, color = InactiveCrimson, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = FalconCyan, modifier = Modifier.size(14.dp).padding(horizontal = 2.dp))
            Text(text = signal.suggestedAsset, color = ActiveEmerald, fontWeight = FontWeight.Bold, fontSize = 11.sp)
          }
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = FalconCyan.copy(alpha = 0.15f)
          ) {
            Text(
              text = "دقة التوقع: ${signal.confidenceScore}%",
              color = FalconCyan,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(text = signal.reason, color = TextWhite, fontSize = 10.5.sp, lineHeight = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "الربح المتوقع: +${signal.targetGainPercent}%", color = ActiveEmerald, fontWeight = FontWeight.Bold, fontSize = 10.sp)
          Text(text = "مستوى الأمان: ${signal.riskLevel}", color = FalconCyan, fontSize = 10.sp)
        }
      }
    }
  }
}

@Composable
private fun IntelligenceRadarTab(
  alerts: List<MarketIntelligenceAlert>
) {
  Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
    alerts.forEach { alert ->
      val accentColor = when (alert.type) {
        "OPPORTUNITY" -> ActiveEmerald
        "DANGER_ALERT" -> InactiveCrimson
        else -> FalconCyan
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(ShaheenSurfaceDark)
          .border(BorderStroke(1.dp, accentColor.copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
          .padding(8.dp),
        verticalAlignment = Alignment.Top
      ) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = when (alert.type) {
              "OPPORTUNITY" -> Icons.Default.CheckCircle
              "DANGER_ALERT" -> Icons.Default.Warning
              else -> Icons.Default.Info
            },
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(14.dp)
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = alert.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 10.5.sp)
            Text(text = alert.timestamp, color = TextDim, fontSize = 8.5.sp)
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(text = alert.summary, color = TextDim, fontSize = 9.5.sp, lineHeight = 12.5.sp)
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "التوجيه: ${alert.actionAdvice}",
            color = accentColor,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}
