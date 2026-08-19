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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.DiagnosticPerformanceReport
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun DiagnosticTelemetryDialog(
  report: DiagnosticPerformanceReport = DiagnosticPerformanceReport(),
  onDismiss: () -> Unit
) {
  val clipboard = LocalClipboardManager.current
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("diagnostic_telemetry_dialog"),
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
                .background(FalconCyan.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.QueryStats,
                contentDescription = null,
                tint = FalconCyan,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "تقرير التشخيص وتدقيق الأداء",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "Shaheen HFT Execution Telemetry Hub",
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

        // High-level Performance Metrics Grid
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          TelemetryMetricBox(
            title = "نسبة الصفقات الرابحة",
            value = "${report.winRatePercent}%",
            sub = "${report.profitableTradesCount} / ${report.totalTradesAnalyzed}",
            color = ActiveEmerald,
            modifier = Modifier.weight(1f)
          )
          TelemetryMetricBox(
            title = "سرعة التنفيذ الفعلي",
            value = "${report.averageExecutionSpeedMs} ms",
            sub = "Sub-Millisecond Engine",
            color = FalconCyan,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          TelemetryMetricBox(
            title = "معدل الانزلاق السعري",
            value = "${String.format(Locale.US, "%.4f", report.averageSlippagePercent)}%",
            sub = "Zero-Slippage Guaranteed",
            color = Color(0xFFFFD700),
            modifier = Modifier.weight(1f)
          )
          TelemetryMetricBox(
            title = "مؤشر شارب (Sharpe)",
            value = "${report.sharpeRatio}",
            sub = "كفاءة أرباح فائقة",
            color = ActiveEmerald,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Zero-Loss Ethical Shield Audit
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ShaheenDarkNavy)
            .border(1.dp, ActiveEmerald.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = ActiveEmerald,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "تدقيق درع منع الخسارة الأخلاقي (Zero-Loss Protection):",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = ActiveEmerald
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "• عدد تدخلات القاطع الفلاشي (Sub-100ms Flash Breaker): ${report.zeroLossShieldInterventions} تدخلات وقائية.\n• إجمالي رأس المال المحمي من الانهيارات المفاجئة: $${String.format(Locale.US, "%,.2f", report.capitalProtectedFromDumpsUsd)} USD.",
              style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
              color = Color.White
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Audit Hash & Verification Signature
        Text(
          text = "بصمة التشفير والتدقيق الرياضي:",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
          color = TextMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = report.auditSignatureSha256,
          style = MaterialTheme.typography.labelSmall.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp
          ),
          color = FalconCyan
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Export Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              val auditText = "SHAHEEN APEX TELEMETRY REPORT\nWinRate: ${report.winRatePercent}%\nSpeed: ${report.averageExecutionSpeedMs}ms\nCapital Protected: $${report.capitalProtectedFromDumpsUsd}\nSignature: ${report.auditSignatureSha256}"
              clipboard.setText(AnnotatedString(auditText))
              HapticFeedbackHelper.performSuccessHaptic(context)
            },
            colors = ButtonDefaults.buttonColors(containerColor = FalconCyan),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = "نسخ التقرير الرياضي",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF0A1118)
            )
          }

          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = ShaheenDarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, ShaheenMetallicBorder),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = "إغلاق",
              style = MaterialTheme.typography.labelMedium,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
private fun TelemetryMetricBox(
  title: String,
  value: String,
  sub: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(ShaheenDarkNavy)
      .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(10.dp))
      .padding(10.dp)
  ) {
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = TextMuted
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace
        ),
        color = color
      )
      Text(
        text = sub,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
        color = Color.White.copy(alpha = 0.8f)
      )
    }
  }
}
