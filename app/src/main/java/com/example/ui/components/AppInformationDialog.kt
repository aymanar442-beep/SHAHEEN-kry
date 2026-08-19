package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
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
import com.example.model.SubscriptionPlanDetails
import com.example.model.SubscriptionPlanId
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun AppInformationDialog(
  onSelectPlanToPay: (SubscriptionPlanDetails) -> Unit = {},
  onDismiss: () -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) }
  val context = LocalContext.current
  val haptic = LocalHapticFeedback.current

  val plans = SubscriptionPlanRepository.getAllPlans()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("app_information_dialog"),
      shape = RoundedCornerShape(20.dp),
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
                .size(36.dp)
                .clip(CircleShape)
                .background(
                  Brush.linearGradient(listOf(FalconCyan.copy(alpha = 0.3f), Color(0xFFFFD700).copy(alpha = 0.3f)))
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = FalconCyan,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "دليل شاهين APEX الشامل",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "Shaheen Apex AI • Ecosystem Specification",
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

        // Tabs: 0: ميزات التطبيق والتفوق, 1: باقات الاشتراك (3 مراحل), 2: درع الأمان الأخلاقي المجاني
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
            text = { Text("لماذا شاهين؟", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = {
              HapticFeedbackHelper.performClickHaptic(haptic)
              selectedTab = 1
            },
            text = { Text("باقات البريميوم", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = {
              HapticFeedbackHelper.performClickHaptic(haptic)
              selectedTab = 2
            },
            text = { Text("الأمان المجاني", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
          0 -> WhyShaheenSection()
          1 -> SubscriptionTiersSection(
            plans = plans,
            onSelectPlan = { plan ->
              HapticFeedbackHelper.performSuccessHaptic(context)
              onSelectPlanToPay(plan)
            }
          )
          2 -> EthicalZeroLossSection()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = ShaheenDarkNavy),
          border = androidx.compose.foundation.BorderStroke(1.dp, ShaheenMetallicBorder),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "إغلاق الدليل", color = Color.White)
        }
      }
    }
  }
}

@Composable
private fun WhyShaheenSection() {
  Column {
    // Unique Advantage Box
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(
          Brush.horizontalGradient(
            colors = listOf(Color(0xFF0C243B), Color(0xFF143B5E))
          )
        )
        .border(1.dp, FalconCyan, RoundedCornerShape(12.dp))
        .padding(14.dp)
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = FalconCyan,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "بماذا يتميز شاهين ولماذا هو الأفضل؟",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "شاهين ليس مجرد بوت تداول تقليدي، بل هو أول نظام خوارزمي كمي يعمل بتقنية الحوسبة الطرفية (Edge Computing) مباشرة على هاتفك، مما يمنحه سرعة استجابة بالميلي ثانية تسبق خوادم التداول السحابية البطيئة.",
          style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
          color = Color.White.copy(alpha = 0.95f)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "لماذا يستحق التطبيق سعره؟",
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
      color = Color(0xFFFFD700)
    )

    Spacer(modifier = Modifier.height(6.dp))

    SubscriptionPlanRepository.WHY_SHAHEEN_JUSTIFIES_PRICE.forEach { reason ->
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
          text = reason,
          style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
          color = Color.White
        )
      }
    }
  }
}

@Composable
private fun SubscriptionTiersSection(
  plans: List<SubscriptionPlanDetails>,
  onSelectPlan: (SubscriptionPlanDetails) -> Unit
) {
  Column {
    Text(
      text = "باقات البريميوم المدروسة (اختر خطة النمو المناسبة لحجم محفظتك):",
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
      color = Color.White
    )

    Spacer(modifier = Modifier.height(10.dp))

    plans.forEach { plan ->
      val isPro = plan.id == SubscriptionPlanId.GROWTH_PRO
      val isApex = plan.id == SubscriptionPlanId.ELITE_APEX
      val badgeColor = if (isApex) Color(0xFFFFD700) else if (isPro) FalconCyan else ActiveEmerald

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ShaheenDarkNavy)
          .border(
            1.dp,
            if (isPro) FalconCyan else if (isApex) Color(0xFFFFD700) else ShaheenMetallicBorder,
            RoundedCornerShape(12.dp)
          )
          .padding(12.dp)
      ) {
        Column {
          // Tier Header & Price
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (isApex) Icons.Default.Diamond else Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = badgeColor,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = plan.id.titleAr,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = badgeColor
              )
            }

            Text(
              text = "$${plan.monthlyPriceUsd.toInt()}/شهر",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              ),
              color = Color.White
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = plan.targetAudienceAr,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = TextMuted
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Unlocked Features
          plan.unlockedFeatures.forEach { feat ->
            Row(
              modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = badgeColor,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = feat,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color.White.copy(alpha = 0.9f)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "سقف المحفظة: ${plan.maxPortfolioBudgetCap}",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
              )
            )

            Button(
              onClick = { onSelectPlan(plan) },
              colors = ButtonDefaults.buttonColors(containerColor = badgeColor),
              shape = RoundedCornerShape(6.dp),
              contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 2.dp),
              modifier = Modifier.height(28.dp)
            ) {
              Text(
                text = "تفعيل الخطة الآن",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                ),
                color = Color(0xFF0B1420)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}

@Composable
private fun EthicalZeroLossSection() {
  Column {
    // Moral Foundation Box
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(
          Brush.horizontalGradient(
            colors = listOf(Color(0xFF09291D), Color(0xFF0F3E2C))
          )
        )
        .border(1.dp, ActiveEmerald, RoundedCornerShape(12.dp))
        .padding(14.dp)
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = ActiveEmerald,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "ميثاق شاهين الأخلاقي: حماية رأس المال مجانية 100%",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = ActiveEmerald
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "هدفنا الأساسي والأول هو منع خسارة أموال المتداولين. لذلك، فإن كافة ميزات وقف الخسارة الذاتي، القاطع الفلاشي، وقفل الطمع السلوكي متاحة مجاناً لكافة المستخدمين في كل الباقات ولا تدخل ضمن الباقات المدفوعة.",
          style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
          color = Color.White
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "الميزات الوقائية المتاحة مجاناً للجميع بدون دفع:",
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
      color = Color.White
    )

    Spacer(modifier = Modifier.height(6.dp))

    SubscriptionPlanRepository.ETHICAL_ZERO_LOSS_PROTECTIONS.forEach { prot ->
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
          text = prot,
          style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
          color = Color.White
        )
      }
    }
  }
}
