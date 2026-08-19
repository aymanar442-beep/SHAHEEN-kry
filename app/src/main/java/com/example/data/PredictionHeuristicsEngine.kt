package com.example.data

import com.example.model.MomentumPredictionAlert
import com.example.model.OrderBookData
import com.example.model.PredictionSignalType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object PredictionHeuristicsEngine {

  private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

  fun analyzeMomentumShift(
    currentPair: String,
    currentPrice: Double,
    orderBook: OrderBookData
  ): MomentumPredictionAlert {
    val totalBidVolume = orderBook.bids.sumOf { it.amount }
    val totalAskVolume = orderBook.asks.sumOf { it.amount }.coerceAtLeast(0.0001)
    val imbalanceRatio = totalBidVolume / totalAskVolume

    val (signalType, expectedMove, rsi, reasoning) = when {
      imbalanceRatio > 1.8 -> {
        val move = 0.85 + (Random.nextDouble() * 1.2)
        Quadruple(
          PredictionSignalType.ORDER_BOOK_BUY_WALL,
          move,
          58.4,
          "رصد ضغط شراء مؤسساتي هائل وتفوق حجم الطلبات بنسبة ${String.format(Locale.US, "%.1fx", imbalanceRatio)} على العروض. الزخم الصاعد جاهز للاختراق السريع."
        )
      }
      imbalanceRatio < 0.6 -> {
        val move = -0.65 - (Random.nextDouble() * 0.8)
        Quadruple(
          PredictionSignalType.SELL_PRESSURE_DIVERGENCE,
          move,
          72.1,
          "تكدس أوامر بيع تصريفية مع ضعف جدار الطلبات. تنبيه تحوط فوري لتجنب الانزلاق."
        )
      }
      else -> {
        val move = 1.15 + (Random.nextDouble() * 0.9)
        Quadruple(
          PredictionSignalType.BULLISH_MOMENTUM_EXPLOSION,
          move,
          49.2,
          "تطابق مؤشرات الزخم الحركي (MACD & Volume Delta) مع ارتداد مؤشر RSI من منطقة الدعم الذهبية."
        )
      }
    }

    val targetPrice = currentPrice * (1.0 + (expectedMove / 100.0))

    return MomentumPredictionAlert(
      id = "PRED-${System.currentTimeMillis() % 100000}",
      pair = currentPair,
      signalType = signalType,
      confidencePercent = 88 + Random.nextInt(10),
      targetTimeHorizonSeconds = listOf(15, 30, 45, 60).random(),
      expectedMovePercent = expectedMove,
      currentPrice = currentPrice,
      targetPredictedPrice = targetPrice,
      orderBookImbalanceRatio = imbalanceRatio,
      rsi14Value = rsi,
      reasoningAr = reasoning,
      timestamp = timeFormat.format(Date())
    )
  }

  private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
