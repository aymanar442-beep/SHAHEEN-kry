package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.model.AutoSwapEngineState
import com.example.model.AutoSwapGemOpportunity
import com.example.model.UserBudgetTier
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun AutoSwapOpportunitySection(
  autoSwapState: AutoSwapEngineState,
  onToggleAutoSwap: (Boolean) -> Unit,
  onBudgetChange: (Double) -> Unit,
  onExecuteManualSwap: (AutoSwapGemOpportunity) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val haptic = LocalHapticFeedback.current

  val activeTier = autoSwapState.currentTier
  val tierColor = Color(activeTier.badgeColor)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("auto_swap_section_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (autoSwapState.isAutoSwapEnabled) FalconCyan else ShaheenMetallicBorder
    )
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header with Glowing AI Sparkle
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
                  colors = listOf(FalconCyan.copy(alpha = 0.3f), Color(0xFF7C4DFF).copy(alpha = 0.3f))
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = FalconCyan,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "محرك التدوير الذكي واقتناص العملات الصاعدة",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Text(
              text = "Shaheen Quantum Auto-Swap Engine",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted
            )
          }
        }

        // Master Switch
        Switch(
          checked = autoSwapState.isAutoSwapEnabled,
          onCheckedChange = { isChecked ->
            HapticFeedbackHelper.performHeavyActionHaptic(context)
            onToggleAutoSwap(isChecked)
          },
          colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = FalconCyan,
            uncheckedThumbColor = TextMuted,
            uncheckedTrackColor = ShaheenDarkNavy
          ),
          modifier = Modifier.testTag("auto_swap_master_switch")
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Direct User Question Prompt (As requested by User)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                Color(0xFF0F2642),
                Color(0xFF14375A)
              )
            )
          )
          .border(1.dp, FalconCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
          .padding(14.dp)
      ) {
        Row(verticalAlignment = Alignment.Top) {
          Icon(
            imageVector = Icons.Default.Psychology,
            contentDescription = null,
            tint = FalconCyan,
            modifier = Modifier
              .size(24.dp)
              .padding(top = 2.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "«بدك أنا أقلبلك من عملة لعملة بطريقتي وخليك تربح بحسب قراءتي للعملات اللي طالعة حتى لو كانت غير معروفة؟»",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
              ),
              color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (autoSwapState.isAutoSwapEnabled)
                "المحرك الذكي مفعّل حالياً ويقوم بمسح السوق وتحويل الرصيد آلياً لأعلى عملة واعدة تناسب حجم محفظتك."
              else
                "قم بتفعيل الزر بالأعلى واختيار حجم رصيدك، وسيقوم النظام بتوجيه أموالك نحو أفضل الفرص المناسبة لحجم رأس مالك.",
              style = MaterialTheme.typography.labelSmall,
              color = if (autoSwapState.isAutoSwapEnabled) ActiveEmerald else TextMuted
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // User Budget Selection Slider & Tier Matching
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ShaheenDarkNavy.copy(alpha = 0.6f))
          .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(12.dp))
          .padding(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "رأس المال المخصص للتدوير:",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
          )
          Text(
            text = String.format(Locale.US, "$%.0f USD", autoSwapState.userCustomBudgetUsd),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            ),
            color = FalconCyan
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Preset Chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(5.0, 25.0, 100.0, 500.0, 1500.0).forEach { budget ->
            val isSelected = Math.abs(autoSwapState.userCustomBudgetUsd - budget) < 2.0
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) FalconCyan.copy(alpha = 0.25f) else ShaheenSurfaceCard)
                .border(1.dp, if (isSelected) FalconCyan else ShaheenMetallicBorder, RoundedCornerShape(8.dp))
                .clickable {
                  HapticFeedbackHelper.performClickHaptic(haptic)
                  onBudgetChange(budget)
                }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "$${budget.toInt()}",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontFamily = FontFamily.Monospace
                ),
                color = if (isSelected) FalconCyan else TextMuted
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
          value = autoSwapState.userCustomBudgetUsd.toFloat(),
          onValueChange = { onBudgetChange(it.toDouble()) },
          valueRange = 1f..2000f,
          colors = SliderDefaults.colors(
            thumbColor = FalconCyan,
            activeTrackColor = FalconCyan,
            inactiveTrackColor = ShaheenMetallicBorder
          ),
          modifier = Modifier.fillMaxWidth().testTag("auto_swap_budget_slider")
        )

        // Tier Identification Card
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(tierColor.copy(alpha = 0.12f))
            .border(1.dp, tierColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Diamond,
            contentDescription = null,
            tint = tierColor,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Column {
            Text(
              text = "${activeTier.titleAr} (${activeTier.rangeAr})",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = tierColor
            )
            Text(
              text = activeTier.targetAssetClass,
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = Color.White.copy(alpha = 0.85f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Scanned Opportunities List
      Text(
        text = "أبرز الفرص الصاعدة المكتشفة لحظياً لحجم محفظتك:",
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        color = Color.White
      )

      Spacer(modifier = Modifier.height(8.dp))

      autoSwapState.availableOpportunities.take(3).forEach { opp ->
        OpportunityItemCard(
          opportunity = opp,
          isAutoEnabled = autoSwapState.isAutoSwapEnabled,
          onExecuteSwap = {
            HapticFeedbackHelper.performSuccessHaptic(context)
            onExecuteManualSwap(opp)
          }
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Performance Stats Footer
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF071828))
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = ActiveEmerald,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "تم تنفيذ ${autoSwapState.totalAutoRotationsCompleted} عملية تدوير ناجحة",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = Color.White
          )
        }
        Text(
          text = String.format(Locale.US, "أرباح محققة: +$%.2f", autoSwapState.totalGeneratedProfitUsd),
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp
          ),
          color = ActiveEmerald
        )
      }
    }
  }
}

@Composable
private fun OpportunityItemCard(
  opportunity: AutoSwapGemOpportunity,
  isAutoEnabled: Boolean,
  onExecuteSwap: () -> Unit
) {
  val tierColor = Color(opportunity.suitableTier.badgeColor)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(ShaheenDarkNavy.copy(alpha = 0.8f))
      .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(12.dp))
      .padding(12.dp)
  ) {
    Column {
      // Row 1: Pair & Target Gain
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(tierColor.copy(alpha = 0.2f))
              .border(1.dp, tierColor, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = opportunity.toAsset.take(2),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              ),
              color = tierColor
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${opportunity.fromAsset} ➔ ${opportunity.toAsset}",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace
                ),
                color = Color.White
              )
              if (opportunity.isUnlistedOrEmerging) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "ALPHA GEM",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                  ),
                  color = Color(0xFFFFD700),
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFD700).copy(alpha = 0.15f))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
            Text(
              text = opportunity.toAssetNameAr,
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = TextMuted
            )
          }
        }

        // Expected Profit Badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ActiveEmerald.copy(alpha = 0.15f))
            .border(1.dp, ActiveEmerald.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.TrendingUp,
            contentDescription = null,
            tint = ActiveEmerald,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = String.format(Locale.US, "+%.2f%%", opportunity.expectedGainPercent),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            ),
            color = ActiveEmerald
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Algorithm Rationale
      Text(
        text = opportunity.algorithmReasonAr,
        style = MaterialTheme.typography.labelSmall.copy(lineHeight = 14.sp),
        color = Color.White.copy(alpha = 0.85f)
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Footer: Momentum indicator & Swap Action Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "مؤشر: ${opportunity.momentumIndicator}",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 10.sp,
            color = FalconCyan
          )
        )

        Button(
          onClick = onExecuteSwap,
          colors = ButtonDefaults.buttonColors(containerColor = FalconCyan.copy(alpha = 0.2f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, FalconCyan),
          shape = RoundedCornerShape(6.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.height(28.dp).testTag("execute_swap_btn_${opportunity.id}")
        ) {
          Icon(
            imageVector = Icons.Default.SwapHoriz,
            contentDescription = null,
            tint = FalconCyan,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "تبديل فوري الآن",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              color = FalconCyan
            )
          )
        }
      }
    }
  }
}
