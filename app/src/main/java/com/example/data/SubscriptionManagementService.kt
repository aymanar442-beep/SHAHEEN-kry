package com.example.data

import com.example.model.AutoDebitStatus
import com.example.model.BillingCycle
import com.example.model.CryptoAutoConversionConfig
import com.example.model.SubscriptionPlanId
import com.example.model.UserSubscriptionState
import java.util.Locale

object SubscriptionManagementService {

  /**
   * Evaluates if a requested feature or trading mode is permitted for the given active plan.
   */
  fun isFeatureAllowed(
    userPlan: SubscriptionPlanId,
    requiredPlan: SubscriptionPlanId
  ): Boolean {
    val tierWeights = mapOf(
      SubscriptionPlanId.BASIC_STARTER to 1,
      SubscriptionPlanId.GROWTH_PRO to 2,
      SubscriptionPlanId.ELITE_APEX to 3,
      SubscriptionPlanId.VIP_SOVEREIGN_WHALE to 4
    )
    val userWeight = tierWeights[userPlan] ?: 1
    val requiredWeight = tierWeights[requiredPlan] ?: 1
    return userWeight >= requiredWeight
  }

  /**
   * Simulates the Genius Multi-Crypto Auto-Conversion and instant Auto-Debit deduction.
   * If the preferred payment token (e.g. USDT) is absent, it seamlessly converts
   * whatever crypto token is present (e.g. SOL, SUI, BTC, ETH) into USDT at spot rate
   * and settles the recurring billing at the exact scheduled microsecond.
   */
  fun processRecurringAutoDebit(
    currentState: UserSubscriptionState,
    availableBalances: Map<String, Double>
  ): Pair<UserSubscriptionState, String> {
    val planDetails = SubscriptionPlanRepository.getAllPlans().firstOrNull { it.id == currentState.currentPlan }
    val requiredAmountUsd = if (currentState.billingCycle == BillingCycle.ANNUAL) {
      planDetails?.annualPriceUsd ?: 758.0
    } else {
      planDetails?.monthlyPriceUsd ?: 79.0
    }

    val primaryCurrency = currentState.autoDebitCurrency
    val primaryBalance = availableBalances[primaryCurrency] ?: 0.0

    val deductionReport: String
    if (primaryBalance >= requiredAmountUsd) {
      deductionReport = "تم الاقتطاع المباشر بنجاح بمبلغ $${String.format(Locale.US, "%.2f", requiredAmountUsd)} $primaryCurrency بالتوقيت الذري الدقيق."
    } else {
      // Smart Auto-Conversion Fallback Triggered
      val candidateCurrency = currentState.conversionConfig.fallbackPriorityCurrencies.firstOrNull {
        (availableBalances[it] ?: 0.0) > 0.0
      } ?: "SOL"

      deductionReport = "التحويل الذكي الفوري (Auto-Conversion Engine): تم رصد رصيد بعملة $candidateCurrency وتحويلها فورياً بدون انزلاق سعري إلى $requiredAmountUsd USDT لتسوية الاشتراك دون أي تأخير."
    }

    val txHash = "0x" + LongArray(4) { kotlin.random.Random.nextLong() }.joinToString("") { "%08x".format(it) }

    val updatedState = currentState.copy(
      isActive = true,
      autoDebitStatus = AutoDebitStatus.ACTIVE_SYNCED,
      lastDeductionTxHash = txHash.take(18) + "...",
      totalSavedFeesUsd = currentState.totalSavedFeesUsd + (requiredAmountUsd * 0.12)
    )

    return Pair(updatedState, deductionReport)
  }
}
