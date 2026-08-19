package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderBookData
import com.example.model.OrderBookEntry
import com.example.model.WebSocketStatus
import com.example.model.WsConnectionStatus
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.InactiveCrimson
import com.example.ui.theme.ShaheenDarkNavy
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun LiveOrderBookComponent(
  orderBook: OrderBookData,
  wsStatus: WebSocketStatus,
  modifier: Modifier = Modifier
) {
  val connectionColor by animateColorAsState(
    targetValue = when (wsStatus.status) {
      WsConnectionStatus.CONNECTED_LIVE -> ActiveEmerald
      WsConnectionStatus.CONNECTING -> FalconCyan
      WsConnectionStatus.RECONNECTING -> Color(0xFFFFB300)
      WsConnectionStatus.OFFLINE_FALLBACK -> Color(0xFFFF9100)
    },
    label = "ws_status_color"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("live_order_book_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, ShaheenMetallicBorder)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header with WebSocket Live Stream indicator
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(FalconCyan.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.SwapVert,
              contentDescription = null,
              tint = FalconCyan,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "سجل الأوامر وعمق السوق الحي (Live Order Book)",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Text(
              text = "${orderBook.pair} • بث مباشر ميلي-ثانية (Zero-Delay)",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted
            )
          }
        }

        // Live Feed Status Badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(connectionColor.copy(alpha = 0.15f))
            .border(1.dp, connectionColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(connectionColor)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${wsStatus.pingLatencyMs}ms",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            ),
            color = connectionColor
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Column Headers
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(6.dp))
          .background(ShaheenDarkNavy.copy(alpha = 0.6f))
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "السعر (USDT)", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.weight(1.2f))
        Text(text = "الكمية", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.weight(1f))
        Text(text = "الإجمالي ($)", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.weight(1.2f))
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Asks (Sells - Red)
      orderBook.asks.take(4).forEach { ask ->
        OrderBookRow(entry = ask, isAsk = true)
        Spacer(modifier = Modifier.height(3.dp))
      }

      // Middle Spread Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF0F2038))
          .border(1.dp, FalconCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.ElectricBolt,
            contentDescription = null,
            tint = FalconCyan,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "فارق السبريد (Spread):",
            style = MaterialTheme.typography.labelSmall,
            color = FalconCyan
          )
        }
        Text(
          text = String.format(Locale.US, "$%.2f (%.4f%%)", orderBook.spreadUsd, orderBook.spreadPercent),
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          ),
          color = Color.White
        )
      }

      // Bids (Buys - Green)
      orderBook.bids.take(4).forEach { bid ->
        OrderBookRow(entry = bid, isAsk = false)
        Spacer(modifier = Modifier.height(3.dp))
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Market Depth Health Footer
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Sensors,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = wsStatus.status.labelAr,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = TextMuted
          )
        }

        Text(
          text = "${wsStatus.messagesReceivedPerSec} ticks/sec",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          ),
          color = FalconCyan
        )
      }
    }
  }
}

@Composable
private fun OrderBookRow(
  entry: OrderBookEntry,
  isAsk: Boolean
) {
  val barColor = if (isAsk) InactiveCrimson.copy(alpha = 0.18f) else ActiveEmerald.copy(alpha = 0.18f)
  val textColor = if (isAsk) InactiveCrimson else ActiveEmerald

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(24.dp)
      .clip(RoundedCornerShape(4.dp))
  ) {
    // Horizontal Depth Fill Bar
    Box(
      modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(entry.depthPercent)
        .align(if (isAsk) Alignment.CenterStart else Alignment.CenterEnd)
        .background(barColor)
    )

    // Row Data
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .align(Alignment.Center),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = String.format(Locale.US, "%.2f", entry.price),
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        ),
        color = textColor,
        modifier = Modifier.weight(1.2f)
      )
      Text(
        text = String.format(Locale.US, "%.3f", entry.amount),
        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
        color = Color.White.copy(alpha = 0.85f),
        modifier = Modifier.weight(1f)
      )
      Text(
        text = String.format(Locale.US, "$%,.0f", entry.totalUsd),
        style = MaterialTheme.typography.labelSmall.copy(
          fontFamily = FontFamily.Monospace,
          fontSize = 11.sp
        ),
        color = TextMuted,
        modifier = Modifier.weight(1.2f)
      )
    }
  }
}
