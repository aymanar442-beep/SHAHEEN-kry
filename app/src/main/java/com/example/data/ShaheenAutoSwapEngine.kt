package com.example.data

import com.example.model.AutoSwapEngineState
import com.example.model.AutoSwapGemOpportunity
import com.example.model.UserBudgetTier
import kotlin.random.Random

object ShaheenAutoSwapEngine {

  fun getTierForBudget(budgetUsd: Double): UserBudgetTier {
    return when {
      budgetUsd < 50.0 -> UserBudgetTier.MICRO_SEED
      budgetUsd <= 500.0 -> UserBudgetTier.GROWTH_MOMENTUM
      else -> UserBudgetTier.WHALE_INSTITUTIONAL
    }
  }

  fun generateAlphaOpportunities(budgetUsd: Double): List<AutoSwapGemOpportunity> {
    val tier = getTierForBudget(budgetUsd)

    val microGems = listOf(
      AutoSwapGemOpportunity(
        id = "GEM-SUI-01",
        fromAsset = "USDT",
        toAsset = "SUI",
        toAssetNameAr = "سوي (SUI Network)",
        currentPrice = 3.42,
        expectedGainPercent = 4.85,
        confidenceScore = 94,
        suitableTier = UserBudgetTier.MICRO_SEED,
        minimumBudgetUsd = 2.0,
        algorithmReasonAr = "انفجار حجم التداول على شبكة Layer-1 واختراق قمة 4 ساعات مع تدفق سيولة كورية.",
        momentumIndicator = "RSI 61.2 (زخم متسارع صاعد)",
        isUnlistedOrEmerging = false,
        riskRating = "SAFE_MICRO_ALPHA"
      ),
      AutoSwapGemOpportunity(
        id = "GEM-KAS-02",
        fromAsset = "USDT",
        toAsset = "KAS",
        toAssetNameAr = "كاسبا (Kaspa BlockDAG)",
        currentPrice = 0.164,
        expectedGainPercent = 6.20,
        confidenceScore = 91,
        suitableTier = UserBudgetTier.MICRO_SEED,
        minimumBudgetUsd = 1.0,
        algorithmReasonAr = "تراكم خوارزمي في مجمعات التعدين وكسر مقاومة الـ 200 EMA اللحظية.",
        momentumIndicator = "MACD Golden Cross Bullish",
        isUnlistedOrEmerging = true,
        riskRating = "HIGH_GROWTH_GEM"
      ),
      AutoSwapGemOpportunity(
        id = "GEM-RENDER-03",
        fromAsset = "USDT",
        toAsset = "RENDER",
        toAssetNameAr = "رندر للذكاء الاصطناعي (Render AI)",
        currentPrice = 6.85,
        expectedGainPercent = 5.40,
        confidenceScore = 89,
        suitableTier = UserBudgetTier.MICRO_SEED,
        minimumBudgetUsd = 3.0,
        algorithmReasonAr = "طلب سيولة ضخم على حوسبة الـ GPU للذكاء الاصطناعي وتحرك الحيتان لشراء القيعان.",
        momentumIndicator = "Volume Delta +185%",
        isUnlistedOrEmerging = false,
        riskRating = "AI_MOMENTUM"
      ),
      AutoSwapGemOpportunity(
        id = "GEM-TIA-04",
        fromAsset = "USDT",
        toAsset = "TIA",
        toAssetNameAr = "سيليستيا (Celestia Modular)",
        currentPrice = 4.90,
        expectedGainPercent = 3.90,
        confidenceScore = 88,
        suitableTier = UserBudgetTier.MICRO_SEED,
        minimumBudgetUsd = 2.5,
        algorithmReasonAr = "طلب عالي على الـ Modular Data Availability مع ارتداد قوي من خط الدعم.",
        momentumIndicator = "Bollinger Band Squeeze Breakout",
        isUnlistedOrEmerging = false,
        riskRating = "SAFE_SCALP"
      )
    )

    val growthOpportunities = listOf(
      AutoSwapGemOpportunity(
        id = "GROWTH-SOL-01",
        fromAsset = "USDT",
        toAsset = "SOL",
        toAssetNameAr = "سولانا (Solana)",
        currentPrice = 188.40,
        expectedGainPercent = 3.20,
        confidenceScore = 96,
        suitableTier = UserBudgetTier.GROWTH_MOMENTUM,
        minimumBudgetUsd = 50.0,
        algorithmReasonAr = "نشاط قياسي في التبادل اللامركزي DEX وعوائد Staking متصاعدة مع اختراق مقاومة 185$.",
        momentumIndicator = "Order Book Imbalance (Bids 72%)",
        isUnlistedOrEmerging = false,
        riskRating = "PROVEN_LEADER"
      ),
      AutoSwapGemOpportunity(
        id = "GROWTH-AVAX-02",
        fromAsset = "USDT",
        toAsset = "AVAX",
        toAssetNameAr = "أفالانش (Avalanche)",
        currentPrice = 32.80,
        expectedGainPercent = 3.85,
        confidenceScore = 92,
        suitableTier = UserBudgetTier.GROWTH_MOMENTUM,
        minimumBudgetUsd = 50.0,
        algorithmReasonAr = "شراكات مؤسساتية جديدة لترميز الأصول الحقيقية RWA وارتفاع أحجام تداول الـ Subnets.",
        momentumIndicator = "Stochastic RSI 24 (Oversold Bounce)",
        isUnlistedOrEmerging = false,
        riskRating = "DEFI_VALUE"
      ),
      AutoSwapGemOpportunity(
        id = "GROWTH-LINK-03",
        fromAsset = "USDT",
        toAsset = "LINK",
        toAssetNameAr = "تشين لينك (Chainlink CCIP)",
        currentPrice = 17.50,
        expectedGainPercent = 2.95,
        confidenceScore = 93,
        suitableTier = UserBudgetTier.GROWTH_MOMENTUM,
        minimumBudgetUsd = 50.0,
        algorithmReasonAr = "تدفقات سيولة قياسية عبر بروتوكول CCIP بين البنوك وشبكات البلوكتشين.",
        momentumIndicator = "Whale Accumulation Wave 4",
        isUnlistedOrEmerging = false,
        riskRating = "INSTITUTIONAL_DEFENSE"
      )
    )

    val whaleOpportunities = listOf(
      AutoSwapGemOpportunity(
        id = "WHALE-BTC-01",
        fromAsset = "USDT",
        toAsset = "BTC",
        toAssetNameAr = "البيتكوين الرقمي (Bitcoin Spot)",
        currentPrice = 98450.0,
        expectedGainPercent = 2.10,
        confidenceScore = 98,
        suitableTier = UserBudgetTier.WHALE_INSTITUTIONAL,
        minimumBudgetUsd = 500.0,
        algorithmReasonAr = "تدفقات مؤسساتية صافية عبر الـ ETFs تجاوزت 480 مليون دولار خلال جلسة اليوم وأمان سيولة مطلق.",
        momentumIndicator = "Institutional Delta Flow +$480M",
        isUnlistedOrEmerging = false,
        riskRating = "SOVEREIGN_GRADE"
      ),
      AutoSwapGemOpportunity(
        id = "WHALE-ETH-02",
        fromAsset = "USDT",
        toAsset = "ETH",
        toAssetNameAr = "الإيثيريوم (Ethereum 2.0)",
        currentPrice = 3120.0,
        expectedGainPercent = 2.65,
        confidenceScore = 95,
        suitableTier = UserBudgetTier.WHALE_INSTITUTIONAL,
        minimumBudgetUsd = 500.0,
        algorithmReasonAr = "انخفاض المعروض في المنصات لأدنى مستوى تاريخي مع تسارع حرق الـ Gas واختراق المقاومة الفنية.",
        momentumIndicator = "Exchange Reserve Supply Deficit",
        isUnlistedOrEmerging = false,
        riskRating = "BLUE_CHIP"
      )
    )

    return when (tier) {
      UserBudgetTier.MICRO_SEED -> microGems + growthOpportunities.take(1)
      UserBudgetTier.GROWTH_MOMENTUM -> growthOpportunities + microGems.take(2) + whaleOpportunities.take(1)
      UserBudgetTier.WHALE_INSTITUTIONAL -> whaleOpportunities + growthOpportunities.take(2)
    }
  }
}
