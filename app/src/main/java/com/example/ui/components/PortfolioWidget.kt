package com.example.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CurrencyDenomination
import com.example.model.PortfolioAsset
import com.example.model.PortfolioSummary
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.InactiveCrimson
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun PortfolioWidget(
  portfolio: PortfolioSummary,
  onDenominationChange: (CurrencyDenomination) -> Unit,
  onSweepToVault: () -> Unit,
  modifier: Modifier = Modifier
) {
  val haptic = LocalHapticFeedback.current
  var expandedAssets by remember { mutableStateOf(false) }

  // Format converted value
  val convertedTotal = when (portfolio.denomination) {
    CurrencyDenomination.USD, CurrencyDenomination.USDT -> portfolio.totalBalanceUsd
    CurrencyDenomination.SAR -> portfolio.totalBalanceUsd / portfolio.denomination.conversionRateToUsd
    CurrencyDenomination.EUR -> portfolio.totalBalanceUsd / portfolio.denomination.conversionRateToUsd
    CurrencyDenomination.BTC -> portfolio.totalBalanceUsd / portfolio.denomination.conversionRateToUsd
  }

  val formattedTotal = when (portfolio.denomination) {
    CurrencyDenomination.BTC -> String.format(Locale.US, "%.5f ₿", convertedTotal)
    CurrencyDenomination.SAR -> String.format(Locale.US, "%,.2f ر.س", convertedTotal)
    CurrencyDenomination.EUR -> String.format(Locale.US, "€%,.2f", convertedTotal)
    CurrencyDenomination.USDT -> String.format(Locale.US, "%,.2f USDT", convertedTotal)
    CurrencyDenomination.USD -> String.format(Locale.US, "$%,.2f", convertedTotal)
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .animateContentSize()
      .testTag("portfolio_widget_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, ShaheenMetallicBorder)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header: Wallet icon & Currency switcher chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF2979FF).copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AccountBalanceWallet,
              contentDescription = null,
              tint = Color(0xFF82B1FF),
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "محفظة الأصول الإجمالية (Live Portfolio)",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Text(
              text = "تقييم الأصول الحية والأرباح غير المحققة",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted
            )
          }
        }

        // Currency Denomination Toggle Selector
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ShaheenDarkNavy.copy(alpha = 0.8f))
            .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(10.dp))
            .padding(2.dp)
        ) {
          CurrencyDenomination.values().forEach { denom ->
            val isSelected = portfolio.denomination == denom
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) FalconCyan.copy(alpha = 0.25f) else Color.Transparent)
                .clickable {
                  HapticFeedbackHelper.performClickHaptic(haptic)
                  onDenominationChange(denom)
                }
                .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
              Text(
                text = denom.symbol,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 11.sp
                ),
                color = if (isSelected) FalconCyan else TextMuted
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Main Balance & P&L display
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Column {
          Text(
            text = "الرصيد الكلي المتاح:",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
          Text(
            text = formattedTotal,
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            ),
            color = Color.White
          )
        }

        // Unrealized P&L Pill
        Column(horizontalAlignment = Alignment.End) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(ActiveEmerald.copy(alpha = 0.15f))
              .border(1.dp, ActiveEmerald.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.TrendingUp,
              contentDescription = null,
              tint = ActiveEmerald,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = String.format(Locale.US, "+$%.2f (+%.2f%%)", portfolio.unrealizedPnlUsd, portfolio.unrealizedPnlPercent),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              ),
              color = ActiveEmerald
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "أرباح غير محققة (Unrealized)",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = TextMuted
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Multi-Asset Allocation Bar
      if (portfolio.assets.isNotEmpty()) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "توزيع المحفظة الاستثمارية (Asset Allocation)",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
              color = FalconCyan
            )
            Text(
              text = if (expandedAssets) "إخفاء التفاصيل" else "عرض الأصول (${portfolio.assets.size})",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
              color = FalconCyan,
              modifier = Modifier
                .clickable { expandedAssets = !expandedAssets }
                .padding(2.dp)
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          // Segmented Progress Bar
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(ShaheenDarkNavy)
          ) {
            portfolio.assets.forEach { asset ->
              Box(
                modifier = Modifier
                  .fillMaxHeight()
                  .weight((asset.allocationPercent / 100.0).toFloat().coerceAtLeast(0.02f))
                  .background(Color(asset.colorHex))
              )
            }
          }

          // Asset Legend / Expanded List
          if (expandedAssets) {
            Spacer(modifier = Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              portfolio.assets.forEach { asset ->
                AssetItemRow(asset = asset)
              }
            }
          } else {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              portfolio.assets.take(4).forEach { asset ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(Color(asset.colorHex))
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${asset.symbol} ${String.format(Locale.US, "%.0f%%", asset.allocationPercent)}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 11.sp,
                      fontFamily = FontFamily.Monospace
                    ),
                    color = Color.White.copy(alpha = 0.85f)
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Fast Action: Auto-Sweep to Isolated Vault
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(ShaheenDarkNavy.copy(alpha = 0.7f))
          .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(10.dp))
          .clickable {
            HapticFeedbackHelper.performClickHaptic(haptic)
            onSweepToVault()
          }
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = ActiveEmerald,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "تأمين ونقل الأرباح اليومية تلقائياً للخزنة المعزولة (Cold Vault)",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
          )
        }
        Text(
          text = "+$34.60",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          ),
          color = ActiveEmerald
        )
      }
    }
  }
}

@Composable
private fun AssetItemRow(asset: PortfolioAsset) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(ShaheenDarkNavy.copy(alpha = 0.5f))
      .padding(horizontal = 10.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(10.dp)
          .clip(CircleShape)
          .background(Color(asset.colorHex))
      )
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = asset.symbol,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = Color.White
        )
        Text(
          text = asset.nameAr,
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = TextMuted
        )
      }
    }

    Column(horizontalAlignment = Alignment.End) {
      Text(
        text = String.format(Locale.US, "$%,.2f", asset.valueUsd),
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        ),
        color = Color.White
      )
      Text(
        text = String.format(Locale.US, "%.4f • %+.2f%%", asset.amount, asset.change24h),
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        ),
        color = if (asset.change24h >= 0) ActiveEmerald else InactiveCrimson
      )
    }
  }
}
