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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
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
import com.example.data.SubscriptionPlanRepository
import com.example.model.CrossPairArbitrageRoute
import com.example.model.SubscriptionPlanId
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun AdaptiveCrossPairArbitrageSection(
  userPlan: SubscriptionPlanId,
  isArbitrageEnabled: Boolean,
  onToggleArbitrage: (Boolean) -> Unit,
  onOpenUpgradeDialog: () -> Unit,
  onExecuteRoute: (CrossPairArbitrageRoute) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val haptic = LocalHapticFeedback.current
  val routes = SubscriptionPlanRepository.getCrossPairArbitrageRoutes(userPlan)
  val isVip = userPlan == SubscriptionPlanId.VIP_SOVEREIGN_WHALE

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("cross_pair_arbitrage_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isArbitrageEnabled) FalconCyan else ShaheenMetallicBorder
    )
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header
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
                  colors = listOf(FalconCyan.copy(alpha = 0.35f), Color(0xFFFFD700).copy(alpha = 0.25f))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.SwapHoriz,
              contentDescription = null,
              tint = FalconCyan,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "محرك التحكيم التكيفي بين الأزواج",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              if (isVip) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFF3366).copy(alpha = 0.2f))
                    .border(1.dp, Color(0xFFFF3366), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "VIP WHALE",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Black
                    ),
                    color = Color(0xFFFF3366)
                  )
                }
              }
            }
            Text(
              text = "Adaptive Cross-Pair Triangular Arbitrage & Liquidity Rotator",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted
            )
          }
        }

        // Toggle / Activation
        Button(
          onClick = {
            if (userPlan == SubscriptionPlanId.BASIC_STARTER) {
              onOpenUpgradeDialog()
            } else {
              HapticFeedbackHelper.performHeavyActionHaptic(context)
              onToggleArbitrage(!isArbitrageEnabled)
            }
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isArbitrageEnabled) ActiveEmerald.copy(alpha = 0.2f) else ShaheenDarkNavy
          ),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isArbitrageEnabled) ActiveEmerald else ShaheenMetallicBorder
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("arbitrage_toggle_btn")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (isArbitrageEnabled) Icons.Default.CheckCircle else if (userPlan == SubscriptionPlanId.BASIC_STARTER) Icons.Default.Lock else Icons.Default.PlayArrow,
              contentDescription = null,
              tint = if (isArbitrageEnabled) ActiveEmerald else Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (userPlan == SubscriptionPlanId.BASIC_STARTER) "ترقية الخطة" else if (isArbitrageEnabled) "نشط ومزامن" else "تشغيل",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = if (isArbitrageEnabled) ActiveEmerald else Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Informative Advantage Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(
            Brush.horizontalGradient(
              listOf(Color(0xFF07192C), Color(0xFF0E2847))
            )
          )
          .border(1.dp, FalconCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
          .padding(12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = FalconCyan,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "يقوم المحرك بمسح الفروقات السعرية بين الأزواج المتعددة واقتناص عوائد مثلثة تلقائياً بأقل من 5ms دون تعريض رأس المال لمخاطر الهبوط العام.",
            style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
            color = Color.White.copy(alpha = 0.95f)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "المسارات الحية المتاحة وفقاً لخطة اشتراكك (${userPlan.titleAr}):",
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        color = Color.White
      )

      Spacer(modifier = Modifier.height(8.dp))

      routes.forEach { route ->
        ArbitrageRouteItemCard(
          route = route,
          userPlan = userPlan,
          onExecute = {
            HapticFeedbackHelper.performSuccessHaptic(context)
            onExecuteRoute(route)
          },
          onUpgrade = onOpenUpgradeDialog
        )
        Spacer(modifier = Modifier.height(8.dp))
      }
    }
  }
}

@Composable
private fun ArbitrageRouteItemCard(
  route: CrossPairArbitrageRoute,
  userPlan: SubscriptionPlanId,
  onExecute: () -> Unit,
  onUpgrade: () -> Unit
) {
  val isUnlocked = com.example.data.SubscriptionManagementService.isFeatureAllowed(userPlan, route.minRequiredTier)
  val isDarkPool = route.minRequiredTier == SubscriptionPlanId.VIP_SOVEREIGN_WHALE

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(ShaheenDarkNavy)
      .border(
        1.dp,
        if (isDarkPool) Color(0xFFFF3366).copy(alpha = 0.5f) else ShaheenMetallicBorder,
        RoundedCornerShape(12.dp)
      )
      .padding(12.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Route Path: Source -> Intermediate -> Target
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = route.sourceAsset,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
            color = Color.White
          )
          Text(text = " ➔ ", color = FalconCyan, fontSize = 12.sp)
          Text(
            text = route.intermediateAsset,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
            color = FalconCyan
          )
          Text(text = " ➔ ", color = FalconCyan, fontSize = 12.sp)
          Text(
            text = route.targetAsset,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
            color = ActiveEmerald
          )
        }

        // Expected Net Yield
        Text(
          text = "+${String.format(Locale.US, "%.2f", route.estimatedNetYieldPercent)}% صافي",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          ),
          color = ActiveEmerald
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "سرعة التنفيذ: ${route.executionSpeedMs}ms | عمق السيولة: $${String.format(Locale.US, "%,.0f", route.liquidityDepthUsd)}",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = TextMuted)
        )

        if (isUnlocked) {
          Button(
            onClick = onExecute,
            colors = ButtonDefaults.buttonColors(containerColor = FalconCyan.copy(alpha = 0.2f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, FalconCyan),
            shape = RoundedCornerShape(6.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp),
            modifier = Modifier.height(26.dp)
          ) {
            Text(
              text = if (route.isExecuted) "تم التحكيم ✓" else "تنفيذ فوري",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
              color = FalconCyan
            )
          }
        } else {
          Button(
            onClick = onUpgrade,
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isDarkPool) Color(0xFFFF3366).copy(alpha = 0.2f) else Color(0xFFFFD700).copy(alpha = 0.2f)
            ),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isDarkPool) Color(0xFFFF3366) else Color(0xFFFFD700)
            ),
            shape = RoundedCornerShape(6.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            modifier = Modifier.height(26.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = if (isDarkPool) Color(0xFFFF3366) else Color(0xFFFFD700),
                modifier = Modifier.size(10.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isDarkPool) "VIP Whale حصري" else "متاح في Apex",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold
                ),
                color = if (isDarkPool) Color(0xFFFF3366) else Color(0xFFFFD700)
              )
            }
          }
        }
      }
    }
  }
}
