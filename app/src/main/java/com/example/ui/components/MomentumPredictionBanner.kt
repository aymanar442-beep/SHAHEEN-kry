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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MomentumPredictionAlert
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun MomentumPredictionBanner(
  prediction: MomentumPredictionAlert,
  onRefreshPrediction: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val isBullish = prediction.signalType.isPositive
  val statusColor = if (isBullish) ActiveEmerald else Color(0xFFFF5252)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("momentum_prediction_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Heuristic AI Title + Confidence Gauge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(statusColor.copy(alpha = 0.2f))
              .border(1.dp, statusColor, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isBullish) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
              contentDescription = null,
              tint = statusColor,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "تنبؤ الزخم اللحظي (Momentum Heuristic)",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Text(
              text = "${prediction.pair} • في غضون ${prediction.targetTimeHorizonSeconds} ثانية",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = TextMuted
            )
          }
        }

        // Confidence Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(statusColor.copy(alpha = 0.15f))
            .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = "دقة التنبؤ: ${prediction.confidencePercent}%",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace,
              fontSize = 10.sp
            ),
            color = statusColor
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Prediction Highlight Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(ShaheenDarkNavy)
          .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(10.dp))
          .padding(10.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = prediction.signalType.labelAr,
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = statusColor
            )

            Text(
              text = String.format(Locale.US, "%+,.2f%% متوقع", prediction.expectedMovePercent),
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              ),
              color = statusColor
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = prediction.reasoningAr,
            style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
            color = Color.White.copy(alpha = 0.9f)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Micro Metrics Footer: Order Book Imbalance + RSI
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "اختلال الطلبات: ${String.format(Locale.US, "%.2fx", prediction.orderBookImbalanceRatio)} Bids",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontFamily = FontFamily.Monospace),
          color = FalconCyan
        )

        Text(
          text = "مؤشر القوة (RSI-14): ${String.format(Locale.US, "%.1f", prediction.rsi14Value)}",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontFamily = FontFamily.Monospace),
          color = TextMuted
        )
      }
    }
  }
}
