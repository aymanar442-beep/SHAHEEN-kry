package com.example.data

import com.example.model.AutoStrategyState
import com.example.model.MicroTradeExecution
import com.example.model.StrategyPriority
import java.util.Locale
import kotlin.random.Random

object AutoStrategyEngine {

  fun generateMicroTrades(
    portfolioBalanceUsd: Double,
    priority: StrategyPriority,
    targetPairs: List<String>
  ): List<MicroTradeExecution> {
    val allocationPerTrade = (portfolioBalanceUsd * 0.12).coerceIn(5.0, 500.0)

    val pairTemplates = listOf(
      Triple("SUI/USDT", 3.42, 0.88),
      Triple("SOL/USDT", 188.40, 0.94),
      Triple("NEAR/USDT", 5.64, 0.82),
      Triple("RENDER/USDT", 7.15, 0.86),
      Triple("BTC/USDT", 98450.0, 0.99)
    )

    return pairTemplates.mapIndexed { idx, (pair, basePrice, liqFraction) ->
      val isBuy = idx % 2 == 0
      val gainPercent = when (priority) {
        StrategyPriority.MAX_GROWTH -> 1.45 + (Random.nextDouble() * 1.8)
        StrategyPriority.BALANCED_LIQUIDITY -> 0.65 + (Random.nextDouble() * 0.9)
        StrategyPriority.CAPITAL_PRESERVATION -> 0.35 + (Random.nextDouble() * 0.4)
      }
      val targetPrice = if (isBuy) basePrice * (1.0 + gainPercent / 100.0) else basePrice * (1.0 - gainPercent / 100.0)
      val profitUsd = allocationPerTrade * (gainPercent / 100.0)

      MicroTradeExecution(
        id = "MTRADE-${1000 + idx}",
        pair = pair,
        side = if (isBuy) "BUY (شراء قاع)" else "SWAP (تدوير ربح)",
        allocatedAmountUsd = allocationPerTrade,
        entryPrice = basePrice,
        exitTargetPrice = targetPrice,
        currentProfitUsd = profitUsd,
        currentProfitPercent = gainPercent,
        status = if (idx == 0) "HARVESTED (تم جني الربح)" else "EXECUTING (نشط)",
        timeElapsed = "${(idx + 1) * 14}s ago",
        liquidityScore = (liqFraction * 100).toInt(),
        volatilityIndex = 1.2 + (Random.nextDouble() * 1.5)
      )
    }
  }
}
