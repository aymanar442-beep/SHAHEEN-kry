package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.InactiveCrimson
import com.example.ui.theme.TextWhite
import kotlin.math.max
import kotlin.math.min

@Composable
fun PriceHistoryChartComponent(
    assetName: String,
    priceDataPoints: List<Double>,
    currentPrice: Double,
    modifier: Modifier = Modifier
) {
    if (priceDataPoints.isEmpty()) return

    val maxPrice = priceDataPoints.maxOrNull() ?: currentPrice
    val minPrice = priceDataPoints.minOrNull() ?: currentPrice
    
    // Determine trend color based on first vs last point
    val isPositiveTrend = priceDataPoints.last() >= priceDataPoints.first()
    val lineColor = if (isPositiveTrend) ActiveEmerald else InactiveCrimson
    val gradientColors = listOf(lineColor.copy(alpha = 0.4f), Color.Transparent)

    Column(
        modifier = modifier
            .background(Color(0xFF151923), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF2C3242), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = assetName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextWhite
                )
                Text(
                    text = "Live Market Trajectory",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${String.format(java.util.Locale.US, "%.2f", currentPrice)}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = FalconCyan
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Canvas Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                if (maxPrice == minPrice) return@Canvas // Prevent division by zero

                val priceRange = maxPrice - minPrice
                val stepX = width / max((priceDataPoints.size - 1).toFloat(), 1f)

                val path = Path()
                val fillPath = Path()

                priceDataPoints.forEachIndexed { index, price ->
                    // Normalize Y coordinate (0 at top, height at bottom, so we invert)
                    val normalizedY = 1f - ((price - minPrice) / priceRange).toFloat()
                    val x = index * stepX
                    val y = normalizedY * height

                    if (index == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, height) // Start fill from bottom
                        fillPath.lineTo(x, y)
                    } else {
                        path.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }
                }

                // Close the fill path
                fillPath.lineTo(width, height)
                fillPath.close()

                // Draw Gradient Fill
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                        startY = 0f,
                        endY = height
                    )
                )

                // Draw Line
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}
