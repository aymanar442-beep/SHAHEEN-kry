package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AutoStrategyState
import com.example.model.MicroTradeExecution
import com.example.model.StrategyPriority
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun AutoStrategySection(
  strategyState: AutoStrategyState,
  onToggleStrategy: (Boolean) -> Unit,
  onPriorityChanged: (StrategyPriority) -> Unit,
  onHarvestTrade: (MicroTradeExecution) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val haptic = LocalHapticFeedback.current

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("auto_strategy_section_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (strategyState.isRunning) FalconCyan else ShaheenMetallicBorder
    )
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header: Engine Title & Active Toggle
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(
                  colors = listOf(FalconCyan.copy(alpha = 0.35f), ActiveEmerald.copy(alpha = 0.25f))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoGraph,
              contentDescription = null,
              tint = FalconCyan,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "محرك الاستراتيجيات التلقائي المجهري",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Text(
              text = "Portfolio-Constrained Micro-Trade Scalper",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted
            )
          }
        }

        // On/Off Action
        Button(
          onClick = {
            HapticFeedbackHelper.performHeavyActionHaptic(context)
            onToggleStrategy(!strategyState.isRunning)
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (strategyState.isRunning) ActiveEmerald.copy(alpha = 0.2f) else ShaheenDarkNavy
          ),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (strategyState.isRunning) ActiveEmerald else ShaheenMetallicBorder
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("auto_strategy_toggle_btn")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (strategyState.isRunning) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
              contentDescription = null,
              tint = if (strategyState.isRunning) ActiveEmerald else Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (strategyState.isRunning) "نشط ومراقب" else "تشغيل",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = if (strategyState.isRunning) ActiveEmerald else Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Portfolio Constraint & Risk Management Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ShaheenDarkNavy)
          .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(12.dp))
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AccountBalanceWallet,
              contentDescription = null,
              tint = FalconCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "قيد رأس المال الفعلي:",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = String.format(Locale.US, "$%,.2f USD", strategyState.constrainedPortfolioCapUsd),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            ),
            color = Color.White
          )
        }

        // Stats Column
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "نسبة الصفقات الرابحة:",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${strategyState.winRatePercent}% (34 صفقات)",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            ),
            color = ActiveEmerald
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Priority Mode Selector
      Text(
        text = "أولوية التوزيع وإدارة السيولة:",
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        color = Color.White
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        StrategyPriority.values().forEach { priority ->
          val isSelected = strategyState.priority == priority
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) FalconCyan.copy(alpha = 0.2f) else ShaheenDarkNavy)
              .border(1.dp, if (isSelected) FalconCyan else ShaheenMetallicBorder, RoundedCornerShape(8.dp))
              .clickable {
                HapticFeedbackHelper.performClickHaptic(haptic)
                onPriorityChanged(priority)
              }
              .padding(vertical = 8.dp, horizontal = 4.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = priority.labelAr,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              ),
              color = if (isSelected) FalconCyan else TextMuted
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Micro-Trades List (Volatile Pairs)
      Text(
        text = "الصفقات المجهرية الجارية عبر أزواج الزخم والسيولة:",
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        color = Color.White
      )

      Spacer(modifier = Modifier.height(8.dp))

      strategyState.activeTrades.take(4).forEach { trade ->
        MicroTradeItemRow(
          trade = trade,
          onHarvest = {
            HapticFeedbackHelper.performSuccessHaptic(context)
            onHarvestTrade(trade)
          }
        )
        Spacer(modifier = Modifier.height(6.dp))
      }

      // Footer: Daily Micro Profit Summary
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF061826))
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Savings,
            contentDescription = null,
            tint = ActiveEmerald,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "أرباح الصفقات المجهرية اليومية:",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = Color.White
          )
        }

        Text(
          text = String.format(Locale.US, "+$%.2f USD", strategyState.dailyMicroProfitUsd),
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          ),
          color = ActiveEmerald
        )
      }
    }
  }
}

@Composable
private fun MicroTradeItemRow(
  trade: MicroTradeExecution,
  onHarvest: () -> Unit
) {
  val isHarvested = trade.status.contains("HARVESTED")

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(ShaheenDarkNavy)
      .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(10.dp))
      .padding(10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Pair & Target Info
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = trade.pair,
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            ),
            color = Color.White
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = trade.side,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            color = if (trade.side.contains("BUY")) ActiveEmerald else FalconCyan
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "مخصص: $${String.format(Locale.US, "%.1f", trade.allocatedAmountUsd)} | سيولة: ${trade.liquidityScore}%",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = TextMuted
        )
      }

      // Right: Profit + Harvest Button
      Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = String.format(Locale.US, "+$%.2f", trade.currentProfitUsd),
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            ),
            color = ActiveEmerald
          )
          Text(
            text = String.format(Locale.US, "+%.2f%%", trade.currentProfitPercent),
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace
            ),
            color = ActiveEmerald
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
          onClick = onHarvest,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isHarvested) ActiveEmerald.copy(alpha = 0.2f) else FalconCyan.copy(alpha = 0.2f)
          ),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isHarvested) ActiveEmerald else FalconCyan
          ),
          shape = RoundedCornerShape(6.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
          modifier = Modifier.height(26.dp)
        ) {
          Text(
            text = if (isHarvested) "محصود ✓" else "جني الآن",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            ),
            color = if (isHarvested) ActiveEmerald else FalconCyan
          )
        }
      }
    }
  }
}
