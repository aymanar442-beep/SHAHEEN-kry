package com.example.data

import com.example.model.CrossPairArbitrageRoute
import com.example.model.SubscriptionPlanDetails
import com.example.model.SubscriptionPlanId

object SubscriptionPlanRepository {

  val ETHICAL_ZERO_LOSS_PROTECTIONS = listOf(
    "درع الحماية من الخسارة التلقائي (Zero-Loss Sub-100ms Circuit Breaker) - متاح مجاناً للجميع وبدون شروط",
    "قفل الانضباط النفسي ومنع الطمع (Bet23 Psycho-Temporal Guard) - أساس أخلاقي مجاني بالكامل",
    "زر الطوارئ الفوري وإلغاء الأوامر المفتوحة (Emergency 1-Tap Kill-Switch) - حق أصيل لكل مستخدم",
    "الخزنة الباردة المعزولة لحفظ الأرباح المسحوبة (Cold Vault Custody) - أمان عتادي مجاني بدون قيود",
    "حماية المفاتيح المشفرة العتادية (Knox Keystore Zero-Leak Enclave) - معيار أمني قياسي للجميع"
  )

  val WHY_SHAHEEN_JUSTIFIES_PRICE = listOf(
    "معمارية HFT حوسبة طرفية (Edge Engine) فائقة السرعة على الهاتف مباشرة تسبق الخوادم السحابية بـ 150-300ms.",
    "محرك التدوير والتحكيم الذاتي عبر الأزواج (Cross-Pair Arbitrage) الذي يحقق أرباحاً صافية من فروق أسعار السيولة.",
    "منع الانزلاق السعري (Zero-Slippage Routing) الذي يوفر شهرياً مئات إلى آلاف الدولارات في الأوامر الكبيرة.",
    "نظام الاقتطاع التلقائي العبقري (Smart Multi-Crypto Auto-Debit) الذي يحول أي عملة متوفرة في المحفظة دون انقطاع الاشتراك."
  )

  fun getAllPlans(): List<SubscriptionPlanDetails> = listOf(
    SubscriptionPlanDetails(
      id = SubscriptionPlanId.BASIC_STARTER,
      monthlyPriceUsd = 0.0,
      annualPriceUsd = 0.0,
      billingPeriodAr = "مجاناً 100% مدى الحياة (حماية سيادية)",
      isPopular = false,
      isVipExclusive = false,
      targetAudienceAr = "للمبتدئين وأصحاب المحافظ المجهرية ($5-$150) الراغبين بحماية رأس المال ومتابعة الأسعار اللحظية",
      unlockedFeatures = listOf(
        "بث حي مباشر بالميلي ثانية لعمق السوق وسجل الأوامر (WebSocket 0-Delay)",
        "منظومة التنبيهات المخصصة للتذبذب السعري مع تحديد نسبة التغير",
        "تتبع المحفظة وتوزيع الأصول بـ 5 عملات عالمية",
        "حماية رأس المال الكاملة وقاطع الدائرة الفوري مجاناً للأبد (Zero-Loss Shield)"
      ),
      maxPortfolioBudgetCap = "حتى $150 رأس مال تشغيلي",
      profitPotentialRating = "نمو معتدل ومستقر (10% - 25% شهرياً)",
      priorityLevelAr = "أولوية قياسية (Standard Gateway)",
      latencyGuarantee = "< 45 ms",
      capitalHandlingTier = "Micro ($5 - $150)"
    ),
    SubscriptionPlanDetails(
      id = SubscriptionPlanId.GROWTH_PRO,
      monthlyPriceUsd = 29.0,
      annualPriceUsd = 249.0,
      billingPeriodAr = "شهرياً ($29) أو سنوياً ($249 - وفر شهرين)",
      isPopular = true,
      isVipExclusive = false,
      targetAudienceAr = "الخطة الأكثر طلباً: للمتداولين النشطين ومحبي اقتناص الفرص وتدوير العملات المجهرية",
      unlockedFeatures = listOf(
        "خاصية الصرافة الذكية وتبديل العملات (بيع القمم وشراء القيعان آلياً بين الأزواج)",
        "محرك الاستراتيجيات التلقائي المجهري (Auto-Strategy Scalper) لمحافظ حتى $1,000",
        "تنبيهات الذكاء التنبؤي للزخم اللحظي (Momentum-Shift Heuristics)",
        "مسح العملات ذات الزخم العالي (SOL, SUI, NEAR, RENDER)",
        "سحب الأرباح اليومي التلقائي إلى الخزنة الباردة"
      ),
      maxPortfolioBudgetCap = "حتى $1,000 رأس مال تشغيلي",
      profitPotentialRating = "نمو متسارع ومضاعف (40% - 90% شهرياً)",
      priorityLevelAr = "أولوية سريعة معززة (Enhanced HFT Priority)",
      latencyGuarantee = "< 18 ms",
      capitalHandlingTier = "Mid-Growth ($150 - $1,000)"
    ),
    SubscriptionPlanDetails(
      id = SubscriptionPlanId.ELITE_APEX,
      monthlyPriceUsd = 49.0,
      annualPriceUsd = 399.0,
      billingPeriodAr = "شهرياً ($49) أو سنوياً ($399 - وفر 35%)",
      isPopular = false,
      isVipExclusive = false,
      targetAudienceAr = "للمحترفين وأصحاب المحافظ المتوسطة إلى الكبيرة الباحثين عن التحكيم المثلثي الكامل",
      unlockedFeatures = listOf(
        "محرك التحكيم التكيفي الشامل بين الأزواج (Adaptive Cross-Pair Arbitrage)",
        "محرك التدوير الذاتي الكامل (Full Autonomous AI Auto-Swap) بدون قيود",
        "اقتناص الجواهر الصاعدة غير المدرجة (Alpha Gems Scanner)",
        "رادار الحيتان ورصد أوامر المؤسسات العميقة (Whale Radar & Book Imbalance)",
        "أولوية اتصال HFT فائقة السرعة مع زمن استجابة دون 10ms",
        "مركز القياس التشخيصي الشامل وتقارير تدقيق الأداء المصدرة"
      ),
      maxPortfolioBudgetCap = "حتى $10,000 رأس مال تشغيلي",
      profitPotentialRating = "عائد سيادي مؤسساتي بأعلى كفاءة رياضية",
      priorityLevelAr = "أولوية نخبوية مباشرة (Dedicated Apex Relay)",
      latencyGuarantee = "< 8 ms",
      capitalHandlingTier = "High-Growth ($1,000 - $10,000)"
    ),
    SubscriptionPlanDetails(
      id = SubscriptionPlanId.VIP_SOVEREIGN_WHALE,
      monthlyPriceUsd = 199.0,
      annualPriceUsd = 1590.0,
      billingPeriodAr = "شهرياً ($199) أو سنوياً ($1,590)",
      isPopular = false,
      isVipExclusive = true,
      targetAudienceAr = "👑 خاص بكبار المتداولين وأصحاب المبالغ الضخمة (آلاف إلى مئات آلاف الدولارات)",
      unlockedFeatures = listOf(
        "تنفيذ صفقات المبالغ الضخمة عبر أحواض السيولة المظلمة (Dark Pool Zero-Price-Impact Execution)",
        "درع الحماية الكامل من هجمات الساندوتش وفرونت رانينج (MEV Anti-Front-Running Protection)",
        "بوابة HFT مخصصة وموجهة مباشرة للألياف الضوئية بسرعة دون 3ms (Ultra-Low Latency Fiber Line)",
        "قنوات إشارات الألفا المؤسساتية المغلقة مع كبار صانعي السوق (Private Institutional Order Flow)",
        "إعادة موازنة المحافظ الكبيرة آلياً عبر بروتوكولات العائد اللامركزي مع سحب تلقائي للخزائن المتعددة",
        "مستشار استراتيجي كمي خاص متاح 24/7 ومراقبة حساب مخصصة لكبار الشخصيات",
        "تداول غير محدود بدون أي سقف للمبالغ (Handling $50k - $1M+ USD With 0% Capital Risk Limit)"
      ),
      maxPortfolioBudgetCap = "مبالغ ضخمة غير محدودة ($10,000 - $1,000,000+ USD)",
      profitPotentialRating = "سيادة مالية مطلقة مع تعظيم أرباح الحيتان",
      priorityLevelAr = "👑 أولوية الحوت السيادي القصوى (Sovereign Tier-0 Fiber Direct)",
      latencyGuarantee = "< 3 ms",
      capitalHandlingTier = "VIP Whale ($10k - $1M+ USD)"
    )
  )

  fun getCrossPairArbitrageRoutes(userPlan: SubscriptionPlanId): List<CrossPairArbitrageRoute> = listOf(
    CrossPairArbitrageRoute(
      routeId = "ROUTE-SOL-SUI-USDT",
      sourceAsset = "USDT",
      intermediateAsset = "SOL",
      targetAsset = "SUI",
      estimatedNetYieldPercent = 1.48,
      executionSpeedMs = 6.4,
      liquidityDepthUsd = 485000.0,
      minRequiredTier = SubscriptionPlanId.GROWTH_PRO,
      isExecuted = true
    ),
    CrossPairArbitrageRoute(
      routeId = "ROUTE-BTC-NEAR-USDT",
      sourceAsset = "BTC",
      intermediateAsset = "NEAR",
      targetAsset = "USDT",
      estimatedNetYieldPercent = 2.14,
      executionSpeedMs = 4.8,
      liquidityDepthUsd = 920000.0,
      minRequiredTier = SubscriptionPlanId.ELITE_APEX,
      isExecuted = true
    ),
    CrossPairArbitrageRoute(
      routeId = "ROUTE-DARKPOOL-ETH-RENDER-USDC",
      sourceAsset = "ETH",
      intermediateAsset = "RENDER",
      targetAsset = "USDC",
      estimatedNetYieldPercent = 3.65,
      executionSpeedMs = 2.9,
      liquidityDepthUsd = 3450000.0,
      minRequiredTier = SubscriptionPlanId.VIP_SOVEREIGN_WHALE,
      isExecuted = false
    ),
    CrossPairArbitrageRoute(
      routeId = "ROUTE-INSTITUTIONAL-SUI-SOL-BTC",
      sourceAsset = "SUI",
      intermediateAsset = "SOL",
      targetAsset = "BTC",
      estimatedNetYieldPercent = 4.22,
      executionSpeedMs = 2.1,
      liquidityDepthUsd = 7800000.0,
      minRequiredTier = SubscriptionPlanId.VIP_SOVEREIGN_WHALE,
      isExecuted = false
    )
  )
}

