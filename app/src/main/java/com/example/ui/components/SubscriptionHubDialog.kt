package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import com.example.data.SubscriptionPlanRepository
import com.example.model.AutoDebitStatus
import com.example.model.BillingCycle
import com.example.model.SubscriptionPlanDetails
import com.example.model.SubscriptionPlanId
import com.example.model.UserSubscriptionState
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun SubscriptionHubDialog(
  subscriptionState: UserSubscriptionState,
  onSelectPlan: (SubscriptionPlanId, BillingCycle) -> Unit,
  onTriggerAutoDebit: () -> Unit,
  onToggleAutoRenew: (Boolean) -> Unit,
  onDismiss: () -> Unit
) {
  var selectedCycle by remember { mutableStateOf(subscriptionState.billingCycle) }
  var selectedTab by remember { mutableStateOf(0) } // 0: الباقات والـ VIP, 1: محرك الاقتطاع التلقائي والتحويل الذكي, 2: الأمان الأخلاقي
  val context = LocalContext.current
  val haptic = LocalHapticFeedback.current

  val allPlans = SubscriptionPlanRepository.getAllPlans()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("subscription_hub_dialog"),
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
      border = androidx.compose.foundation.BorderStroke(1.dp, FalconCyan)
    ) {
      Column(
        modifier = Modifier
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
                .background(
                  Brush.linearGradient(
                    listOf(FalconCyan.copy(alpha = 0.35f), Color(0xFFFF3366).copy(alpha = 0.35f))
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Diamond,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "باقات شاهين APEX ونادي الحوت السيادي VIP",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "Shaheen Subscription & VIP Sovereign Whale Hub",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Navigation Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = ShaheenDarkNavy,
          contentColor = FalconCyan,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = FalconCyan
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = {
              HapticFeedbackHelper.performClickHaptic(haptic)
              selectedTab = 0
            },
            text = { Text("الباقات والـ VIP", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = {
              HapticFeedbackHelper.performClickHaptic(haptic)
              selectedTab = 1
            },
            text = { Text("الاقتطاع والتحويل الذكي", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = {
              HapticFeedbackHelper.performClickHaptic(haptic)
              selectedTab = 2
            },
            text = { Text("الأمان المجاني 100%", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
          0 -> {
            // 👑 Genesis $100 License & Sovereign Rules Card
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                  Brush.linearGradient(listOf(Color(0xFF1B232D), Color(0xFF0F1820)))
                )
                .border(
                  BorderStroke(1.5.dp, Brush.linearGradient(listOf(FalconGold, FalconCyan))),
                  RoundedCornerShape(14.dp)
                )
                .padding(14.dp)
            ) {
              Column {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Diamond,
                      contentDescription = null,
                      tint = FalconGold,
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "رخصة امتلاك شاهين: $100 لمرة واحدة",
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                      color = Color.White
                    )
                  }
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(FalconGold)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text("GENESIS LICENSE", fontWeight = FontWeight.Bold, fontSize = 9.sp, color = Color.Black)
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = "• تملك دائم للمحرك السيادي مع ربط البصمة العتادية الحصرية بجهازك.\n• اشتراك صيانة سنوي 25$ للمستخدمين العاديين، أو إعفاء كامل بنسبة 100% من رسم الـ 25$ عند الاشتراك في أي باقة تكتيكية لمدة 6 أشهر فأكثر!",
                  style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp, fontSize = 11.sp),
                  color = Color(0xFFE2E8F0)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Billing Cycle Selector (Monthly vs Annual with 20% discount)
            BillingCycleSelector(
              selectedCycle = selectedCycle,
              onSelectCycle = {
                HapticFeedbackHelper.performClickHaptic(haptic)
                selectedCycle = it
              }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Plans List (Basic, Growth Pro, Elite Apex, and VIP Sovereign Whale)
            allPlans.forEach { plan ->
              PlanCardItem(
                plan = plan,
                billingCycle = selectedCycle,
                isActive = subscriptionState.currentPlan == plan.id,
                onSelectPlan = {
                  HapticFeedbackHelper.performSuccessHaptic(context)
                  onSelectPlan(plan.id, selectedCycle)
                }
              )
              Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            // One-Click Subscription Cancellation & Fair Downgrade Card
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF161B22))
                .border(1.dp, Color(0xFF30363D), RoundedCornerShape(12.dp))
                .padding(12.dp)
            ) {
              Column {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Shield,
                      contentDescription = null,
                      tint = Color(0xFF8B949E),
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "التحكم الكامل: إلغاء التجديد بنقرة واحدة",
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                      color = Color.White
                    )
                  }

                  Text(
                    text = if (subscriptionState.autoDebitStatus == AutoDebitStatus.ACTIVE_SYNCED) "التجديد نشط" else "التجديد متوقف",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (subscriptionState.autoDebitStatus == AutoDebitStatus.ACTIVE_SYNCED) ActiveEmerald else Color(0xFFFFA500)
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "بإمكانك إيقاف التجديد التلقائي في أي ثانية دون أي رسوم أو شروط معقدة. ستحتفظ بجميع ميزات باقتك حتى نهاية الفترة الحالية، ثم تعود تلقائياً لحماية شاهين الأساسية المجانية مدى الحياة.",
                  style = MaterialTheme.typography.labelSmall.copy(lineHeight = 15.sp, fontSize = 10.sp),
                  color = TextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End
                ) {
                  Button(
                    onClick = {
                      val newRenew = subscriptionState.autoDebitStatus != AutoDebitStatus.ACTIVE_SYNCED
                      onToggleAutoRenew(newRenew)
                    },
                    colors = ButtonDefaults.buttonColors(
                      containerColor = if (subscriptionState.autoDebitStatus == AutoDebitStatus.ACTIVE_SYNCED) Color(0xFF391E24) else Color(0xFF1E3A2F)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = if (subscriptionState.autoDebitStatus == AutoDebitStatus.ACTIVE_SYNCED) "إلغاء التجديد التلقائي للدفعة القادمة" else "إعادة تفعيل التجديد التلقائي",
                      fontSize = 10.sp,
                      color = if (subscriptionState.autoDebitStatus == AutoDebitStatus.ACTIVE_SYNCED) Color(0xFFFF7B72) else ActiveEmerald,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }
          }

          1 -> {
            // Smart Auto-Debit & Multi-Crypto Auto-Conversion Protocol Section
            SmartAutoDebitSection(
              subscriptionState = subscriptionState,
              onTriggerAutoDebit = {
                HapticFeedbackHelper.performSuccessHaptic(context)
                onTriggerAutoDebit()
              },
              onToggleAutoRenew = onToggleAutoRenew
            )
          }

          2 -> {
            // Ethical Free Shield Section
            EthicalFreeShieldSection()
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = ShaheenDarkNavy),
          border = androidx.compose.foundation.BorderStroke(1.dp, ShaheenMetallicBorder),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "إغلاق نافذة الاشتراكات", color = Color.White)
        }
      }
    }
  }
}

@Composable
private fun BillingCycleSelector(
  selectedCycle: BillingCycle,
  onSelectCycle: (BillingCycle) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(ShaheenDarkNavy)
      .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(12.dp))
      .padding(4.dp),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    BillingCycle.values().forEach { cycle ->
      val isSelected = selectedCycle == cycle
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(8.dp))
          .background(if (isSelected) FalconCyan.copy(alpha = 0.2f) else Color.Transparent)
          .border(
            1.dp,
            if (isSelected) FalconCyan else Color.Transparent,
            RoundedCornerShape(8.dp)
          )
          .clickable { onSelectCycle(cycle) }
          .padding(vertical = 8.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = cycle.labelAr,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              fontSize = 11.sp
            ),
            color = if (isSelected) FalconCyan else TextMuted
          )
          if (cycle == BillingCycle.ANNUAL) {
            Text(
              text = "وفر 20% فوراً",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
              ),
              color = ActiveEmerald
            )
          }
        }
      }
    }
  }
}

@Composable
private fun PlanCardItem(
  plan: SubscriptionPlanDetails,
  billingCycle: BillingCycle,
  isActive: Boolean,
  onSelectPlan: () -> Unit
) {
  val isVip = plan.isVipExclusive
  val isAnnual = billingCycle == BillingCycle.ANNUAL
  val displayPrice = if (isAnnual) "$${plan.annualPriceUsd.toInt()}/سنة" else "$${plan.monthlyPriceUsd.toInt()}/شهر"
  val badgeColor = Color(plan.id.badgeColorHex)

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("plan_card_${plan.id.name}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = if (isVip) Color(0xFF140810) else ShaheenDarkNavy),
    border = androidx.compose.foundation.BorderStroke(
      if (isVip || isActive) 2.dp else 1.dp,
      if (isActive) ActiveEmerald else if (isVip) Color(0xFFFF3366) else ShaheenMetallicBorder
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header: Plan Name, VIP / Popular Badge, and Price
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = if (isVip) Icons.Default.Diamond else Icons.Default.WorkspacePremium,
            contentDescription = null,
            tint = badgeColor,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = plan.id.titleAr,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (isVip) 15.sp else 14.sp
              ),
              color = badgeColor
            )
            Text(
              text = plan.targetAudienceAr,
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = TextMuted
            )
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = displayPrice,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            ),
            color = Color.White
          )
          if (isAnnual) {
            Text(
              text = "توفير $${String.format(Locale.US, "%.0f", (plan.monthlyPriceUsd * 12) - plan.annualPriceUsd)}",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
              color = ActiveEmerald
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // High Capital Handling & Guaranteed Latency Badge for VIP
      if (isVip) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.horizontalGradient(listOf(Color(0xFF330818), Color(0xFF1E0A16)))
            )
            .border(1.dp, Color(0xFFFF3366), RoundedCornerShape(10.dp))
            .padding(10.dp)
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color(0xFFFF3366),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "مخصص للمبالغ العالية (آلاف وملايين الدولارات):",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFFF3366)
                )
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "• سرعة توجيه مباشرة بالألياف الضوئية دون 3ms بدون أي انزلاق سعري في الصفقات الضخمة.\n• قنوات سيولة مظلمة (Dark Pool) تمنع حركة السعر ضدك أو استهداف أوامرك من الروبوتات المنافسة.",
              style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
              color = Color.White.copy(alpha = 0.95f)
            )
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      // Feature list
      plan.unlockedFeatures.forEach { feature ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = badgeColor,
            modifier = Modifier
              .size(14.dp)
              .padding(top = 2.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = feature,
            style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
            color = Color.White.copy(alpha = 0.9f)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Emotional ROI & Profit Potential Highlight
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF0F1E28))
          .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
          .padding(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Paid,
              contentDescription = null,
              tint = badgeColor,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "العائد المتوقع واسترداد القيمة:",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
              color = Color.White
            )
          }

          Text(
            text = plan.profitPotentialRating,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Black,
              fontSize = 10.sp
            ),
            color = badgeColor
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Bottom Row: Capital Handling Limit + Action Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "نطاق رأس المال:",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            color = TextMuted
          )
          Text(
            text = plan.capitalHandlingTier,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            ),
            color = Color.White
          )
        }

        Button(
          onClick = onSelectPlan,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) ActiveEmerald else badgeColor
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("select_plan_${plan.id.name}_btn")
        ) {
          Text(
            text = if (isActive) "الخطة الحالية النشطة ✓" else if (isVip) "انضم لنادي الحوت السيادي" else "تفعيل الخطة الآن",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            ),
            color = Color(0xFF070E16)
          )
        }
      }
    }
  }
}

@Composable
private fun SmartAutoDebitSection(
  subscriptionState: UserSubscriptionState,
  onTriggerAutoDebit: () -> Unit,
  onToggleAutoRenew: (Boolean) -> Unit
) {
  Column {
    // Engine Introduction Box
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(
          Brush.horizontalGradient(
            listOf(Color(0xFF092036), Color(0xFF13385E))
          )
        )
        .border(1.dp, FalconCyan, RoundedCornerShape(14.dp))
        .padding(14.dp)
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CurrencyExchange,
            contentDescription = null,
            tint = FalconCyan,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "محرك الاقتطاع التلقائي والتحويل الذكي من أي كريبتو",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "ميزة عبقرية تضمن عدم انقطاع اشتراكك أبداً: إذا لم يتوفر رصيد كافٍ بعملة الدفع المحددة (USDT)، يقوم المحرك فورياً بتحويل أي عملة متوفرة في محفظتك (مثل SOL أو BTC أو ETH أو SUI أو BNB) إلى عملة الدفع بالسعر اللحظي بدون انزلاق واقتطاعها بالتوقيت الذري الدقيق دون أي تأخير.",
          style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
          color = Color.White.copy(alpha = 0.95f)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Subscription Status Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ShaheenDarkNavy)
        .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(12.dp))
        .padding(12.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "حالة الاقتطاع التلقائي:",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
          Text(
            text = subscriptionState.autoDebitStatus.labelAr,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = ActiveEmerald
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "تاريخ التجديد القادم:",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
          Text(
            text = subscriptionState.nextBillingDate,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            ),
            color = Color.White
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "العملات المدعومة للتحويل الذكي الفوري:",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
          Text(
            text = "USDT, USDC, SOL, BTC, ETH, SUI, BNB, TON",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            ),
            color = FalconCyan
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Simulation Trigger Button
    Button(
      onClick = onTriggerAutoDebit,
      colors = ButtonDefaults.buttonColors(containerColor = FalconCyan),
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier.fillMaxWidth().testTag("trigger_auto_debit_btn")
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Sync,
          contentDescription = null,
          tint = Color(0xFF070E16),
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "اختبار الاقتطاع والتحويل الذكي الفوري الآن",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF070E16)
        )
      }
    }
  }
}

@Composable
private fun EthicalFreeShieldSection() {
  Column {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(
          Brush.horizontalGradient(
            listOf(Color(0xFF07241A), Color(0xFF0E3E2E))
          )
        )
        .border(1.dp, ActiveEmerald, RoundedCornerShape(14.dp))
        .padding(14.dp)
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = ActiveEmerald,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "الميثاق الأخلاقي: حماية رأس المال مجانية 100% للجميع",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = ActiveEmerald
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "تطبيق شاهين مبني على مبدأ عدم استغلال المتداولين في أدوات الأمان الأساسية. جميع أدوات منع الخسارة، القاطع الفلاشي التلقائي، وقفل الطمع السلوكي مجانية دائماً لجميع المستخدمين في كافة الباقات ولا تتطلب أي دفع.",
          style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
          color = Color.White
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "الأدوات الوقائية المجانية بالكامل:",
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
      color = Color.White
    )

    Spacer(modifier = Modifier.height(6.dp))

    SubscriptionPlanRepository.ETHICAL_ZERO_LOSS_PROTECTIONS.forEach { item ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
      ) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = ActiveEmerald,
          modifier = Modifier
            .size(16.dp)
            .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = item,
          style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
          color = Color.White
        )
      }
    }
  }
}
