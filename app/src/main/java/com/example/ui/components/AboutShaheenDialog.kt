package com.example.ui.components

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
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.example.data.SubscriptionPlanRepository
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted

@Composable
fun AboutShaheenDialog(
  onOpenSubscriptionHub: () -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("about_shaheen_dialog"),
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
      border = androidx.compose.foundation.BorderStroke(1.dp, FalconCyan)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                  Brush.linearGradient(
                    listOf(FalconCyan.copy(alpha = 0.35f), Color(0xFF0055FF).copy(alpha = 0.35f))
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Memory,
                contentDescription = null,
                tint = FalconCyan,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "عن نظام شاهين APEX AI",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "HFT Architecture, Security & Edge Proposition",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Architecture Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
              Brush.horizontalGradient(
                listOf(Color(0xFF071B2F), Color(0xFF103358))
              )
            )
            .border(1.dp, FalconCyan, RoundedCornerShape(14.dp))
            .padding(14.dp)
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = null,
                tint = FalconCyan,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "معمارية التداول عالي التردد الطرفية (Edge HFT Engine)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "تعتمد معظم البوتات التقليدية على خوادم سحابية مشتركة تعاني من تأخير استجابة (200-500ms) مما يسبب انزلاقاً سعرياً مستمراً. بينما يعمل محرك شاهين محلياً على هاتفك بمعالجة C++ وKotlin المتوازية وبث WebSocket ثنائي مشفر يتيح سرعة استجابة دون 12ms واقتناص الفروق السعرية اللحظية قبل المنصات المركزية.",
              style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
              color = Color.White.copy(alpha = 0.95f)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Latency Comparison Table
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ShaheenDarkNavy)
            .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Column {
            Text(
              text = "مقارنة سرعة التنفيذ والانزلاق السعري:",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "المعيار", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(text = "البوتات السحابية", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              Text(text = "شاهين APEX", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = FalconCyan)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "زمن الاستجابة", style = MaterialTheme.typography.labelSmall, color = Color.White)
              Text(text = "250 - 450 ms", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF5252))
              Text(text = "3 - 12 ms", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = ActiveEmerald)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "معدل الانزلاق السعري", style = MaterialTheme.typography.labelSmall, color = Color.White)
              Text(text = "0.45% - 1.2%", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF5252))
              Text(text = "0.0012% (شبه منعدم)", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = ActiveEmerald)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "حماية صفقات الحيتان", style = MaterialTheme.typography.labelSmall, color = Color.White)
              Text(text = "مكشوفة لهجمات MEV", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF5252))
              Text(text = "Dark Pool + درع MEV", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = ActiveEmerald)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Security Protocols Section
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ShaheenDarkNavy)
            .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "بروتوكولات الأمان العتادي وتشفير المفاتيح (Security Protocols):",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFFFD700)
                )
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "• تشفير المفاتيح عبر شريحة Knox Hardware Keystore المعزولة عتادياً داخل المعالج.\n• صفر وصول سحابي: مفاتيح API لا تغادر ذاكرة الجهاز مطلقاً.\n• قاطع الدائرة الفلاشي الأوتوماتيكي لمنع الانهيارات اللحظية.\n• تحويل أرباح التداول اليومية مباشرة إلى الخزنة الباردة المعزولة.",
              style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
              color = Color.White.copy(alpha = 0.9f)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Why Specific Premium Features Provide an Optimized Trading Edge
        Text(
          text = "لماذا تقدم الميزات المتقدمة وVIP تفوقاً استثمارياً حقيقياً؟",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        SubscriptionPlanRepository.WHY_SHAHEEN_JUSTIFIES_PRICE.forEach { reason ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = FalconCyan,
              modifier = Modifier
                .size(14.dp)
                .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = reason,
              style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
              color = Color.White.copy(alpha = 0.9f)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Button(
          onClick = {
            onDismiss()
            onOpenSubscriptionHub()
          },
          colors = ButtonDefaults.buttonColors(containerColor = FalconCyan),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth().testTag("open_subscription_from_about_btn")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Diamond,
              contentDescription = null,
              tint = Color(0xFF070E16),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "استعراض باقات الاشتراك ونادي الحوت VIP",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF070E16)
            )
          }
        }
      }
    }
  }
}
