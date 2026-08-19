package com.example.model

enum class LogLevel {
  INFO,
  SUCCESS,
  WARNING,
  ERROR,
  SYSTEM
}

data class LogEntry(
  val id: Long = System.currentTimeMillis() + (0..999).random(),
  val timestamp: String,
  val tag: String,
  val message: String,
  val level: LogLevel = LogLevel.INFO
)

enum class FluctuationDirection(val labelAr: String, val labelEn: String, val symbol: String) {
  BOTH("كافة الاتجاهات (صعود وهبوط)", "Both Directions (Surge & Dump)", "±"),
  SURGE_ONLY("ارتفاعات صاعدة فقط", "Price Surges Only (+)", "+"),
  DROP_ONLY("هبوط وانخفاضات فقط", "Price Dumps Only (-)", "-")
}

data class ShaheenConfig(
  val username: String = "ayman",
  val licenseKey: String = "SH-9924-SEC-ALPHA-88X",
  val apiKey: String = "sh_live_k82f990141be297d09873a",
  val hasAcceptedDisclaimer: Boolean = false,
  val additionalUsersCount: Int = 0,
  val preEmptiveShieldActive: Boolean = true,
  val bet23BehavioralLock: Boolean = true,
  val emergencyKillSwitchReady: Boolean = true,
  val priceAlertsEnabled: Boolean = true,
  val upperPriceThreshold: Double = 98520.0,
  val lowerPriceThreshold: Double = 98320.0,
  val volatilitySpikeThresholdPercent: Double = 0.35,
  val fluctuationPercentageThreshold: Double = 0.50, // User-configurable % threshold (e.g. 0.50%)
  val fluctuationDirection: FluctuationDirection = FluctuationDirection.BOTH,
  val fluctuationTimeWindowSeconds: Int = 10,
  val systemNotificationsEnabled: Boolean = true,
  val fluctuationAlertSoundEnabled: Boolean = true
)

enum class ThresholdBreachType(val labelAr: String, val labelEn: String) {
  UPPER_BARRIER_CROSS("اختراق حاجز المقاومة العلوي", "Upper Barrier Breach"),
  LOWER_SUPPORT_DROP("كسر حاجز الدعم السفلي", "Lower Support Breach"),
  VOLATILITY_SURGE("قفزة تقلبات مفاجئة", "Volatility Surge"),
  PERCENTAGE_FLUCTUATION("تجاوز نسبة تذبذب السعر المحددة", "Custom Percentage Fluctuation")
}

data class MarketPriceAlert(
  val id: String = "ALERT-${System.currentTimeMillis() % 10000}",
  val pair: String = "BTC/USDT",
  val breachType: ThresholdBreachType,
  val triggerPrice: Double,
  val thresholdPrice: Double,
  val deltaPercent: Double,
  val timestamp: String,
  val suggestedAction: String,
  val fluctuationDirection: FluctuationDirection = FluctuationDirection.BOTH
)

data class EngineStatus(
  val isRunning: Boolean = false,
  val uptimeSeconds: Long = 0L,
  val totalCycles: Long = 0L,
  val currentPair: String = "BTC/USDT",
  val latencyMs: Int = 14,
  val priceIndex: Double = 98450.20,
  val volumeScanned: Double = 1.42,
  val riskScore: Double = 0.02,
  val testRemainingSeconds: Long = 86400L * 3L + 14320L, // 3 Days Beta Countdown
  val behavioralStability: Double = 99.8, // Bet23 Psycho-Temporal Stability Index
  val preEmptiveOrdersArmed: Int = 8, // Exchange-Level Pre-Emptive OCO Orders
  val isOfflineImmune: Boolean = true,
  val sub100msFlashBreakerActive: Boolean = true, // Sub-100ms Micro-Tick Delta Circuit Breaker
  val autoSweepVaultSecured: Boolean = true, // Auto-sweep Spot Vault isolation
  val knoxBiometricArmed: Boolean = true, // Hardware Keystore security layer
  val spotColdVaultUsdt: Double = 25480.00 // Isolated USDT in Cold Spot Vault
)

data class EcosystemProduct(
  val id: String,
  val title: String,
  val subtitle: String,
  val category: String,
  val status: String,
  val badgeColor: Long,
  val description: String,
  val features: List<String>
)

enum class PaymentGateway(val title: String, val network: String, val address: String, val description: String) {
  BINANCE_PAY(
    title = "Binance Wallet (BNB Smart Chain)",
    network = "BNB Smart Chain (BEP20)",
    address = "0x48d27EDC1a95AD2484bB6563985e4BDd2F952CcC",
    description = "محفظة باينانس الرسمية للمطور على شبكة BNB Smart Chain"
  ),
  MEXC_PAY(
    title = "MEXC Wallet (BNB Smart Chain)",
    network = "BNB Smart Chain (BEP20)",
    address = "0x7de83792347744c4cf6d7d6d6236ced68cccc56c",
    description = "محفظة MEXC الرسمية للمطور على شبكة BNB Smart Chain"
  ),
  FIAT_ONRAMP(
    title = "شراء وتحويل فوري (بطاقات بنكية / Google Pay)",
    network = "Instant Card & Mobile Fiat to Crypto",
    address = "0x48d27EDC1a95AD2484bB6563985e4BDd2F952CcC",
    description = "يمكن للمشتري الدفع ببطاقته البنكية أو Google Pay عبر منصات الشراء الفوري (MoonPay/Binance Connect) لتصل مباشرة كـ USDT إلى محفظتك"
  )
}

enum class RiskTierMode(
  val titleAr: String,
  val titleEn: String,
  val targetProfitRange: String,
  val stopLossLimit: String,
  val maxLeverage: Int,
  val trailingProtection: String,
  val description: String
) {
  ULTRA_SAFE(
    titleAr = "النمط فائق الأمان (تراكم رؤوس الأموال)",
    titleEn = "Ultra-Safe Compounder",
    targetProfitRange = "0.3% - 0.7%",
    stopLossLimit = "0.25%",
    maxLeverage = 3,
    trailingProtection = "فوري دون تأخير (0ms)",
    description = "مصمم لحماية رأس المال 100% وجني أرباح مجهرية متراكمة مع وقف خسارة حديدي."
  ),
  BALANCED_MOMENTUM(
    titleAr = "النمط المتوازن (اقتناص الزخم)",
    titleEn = "Balanced Momentum",
    targetProfitRange = "1.2% - 2.5%",
    stopLossLimit = "0.85%",
    maxLeverage = 10,
    trailingProtection = "متدرج مع سحب الأرباح للـ Spot",
    description = "يوازن بين سرعة النمو ومستويات الأمان العالية مع تحوط فوري عند اختلال السيولة."
  ),
  APEX_SOVEREIGN(
    titleAr = "النمط السيادي المتقدم (حيتان وكميات)",
    titleEn = "Apex Sovereign Dynamic",
    targetProfitRange = "3.0% - 7.5%",
    stopLossLimit = "1.80%",
    maxLeverage = 20,
    trailingProtection = "خوارزمية Bet23 الحركية",
    description = "للمحافظ الكبيرة والمؤسسات مع تفعيل قاطع الفلاش ساب-100ms والتحوط متعدد العملات."
  )
}

data class MicroScalpTrade(
  val id: String,
  val pair: String,
  val type: String, // LONG / SHORT
  val entryPrice: Double,
  val exitPrice: Double,
  val profitPercent: Double,
  val durationSeconds: Int,
  val status: String // COMPLETED / ACTIVE
)

enum class CurrencyDenomination(val symbol: String, val labelAr: String, val conversionRateToUsd: Double) {
  USD("$", "دولار أمريكي (USD)", 1.0),
  USDT("USDT", "تيذر مستقر (USDT)", 1.0),
  SAR("ر.س", "ريال سعودي (SAR)", 0.266),
  EUR("€", "يورو (EUR)", 1.08),
  BTC("₿", "بيتكوين (BTC)", 98450.0)
}

data class PortfolioAsset(
  val symbol: String,
  val nameAr: String,
  val amount: Double,
  val valueUsd: Double,
  val allocationPercent: Double,
  val change24h: Double,
  val colorHex: Long
)

data class PortfolioSummary(
  val totalBalanceUsd: Double = 1248.50,
  val unrealizedPnlUsd: Double = 142.30,
  val unrealizedPnlPercent: Double = 12.85,
  val dailyProfitUsd: Double = 34.60,
  val denomination: CurrencyDenomination = CurrencyDenomination.USD,
  val assets: List<PortfolioAsset> = emptyList()
)

data class OrderBookEntry(
  val price: Double,
  val amount: Double,
  val totalUsd: Double,
  val depthPercent: Float
)

data class OrderBookData(
  val pair: String = "BTC/USDT",
  val bids: List<OrderBookEntry> = emptyList(), // Buy orders (Green)
  val asks: List<OrderBookEntry> = emptyList(), // Sell orders (Red)
  val highestBid: Double = 98449.50,
  val lowestAsk: Double = 98450.50,
  val spreadUsd: Double = 1.00,
  val spreadPercent: Double = 0.001,
  val latencyMs: Long = 12L
)

enum class WsConnectionStatus(val labelAr: String, val labelEn: String) {
  CONNECTING("جاري الاتصال بالسيرفر المباشر...", "Connecting..."),
  CONNECTED_LIVE("متصل فوري (WebSocket Live 0-Delay)", "Live Stream (Active)"),
  RECONNECTING("جاري إعادة الاتصال التلقائي...", "Auto-Reconnecting..."),
  OFFLINE_FALLBACK("نمط الاستمرارية وحماية الشبكة", "Resilience Offline-Immune")
}

data class WebSocketStatus(
  val status: WsConnectionStatus = WsConnectionStatus.CONNECTED_LIVE,
  val pingLatencyMs: Long = 18L,
  val streamUrl: String = "wss://stream.binance.com:9443/ws/btcusdt@ticker",
  val messagesReceivedPerSec: Int = 14,
  val lastHeartbeatTime: String = "LIVE",
  val isRealTimeDirectFeed: Boolean = true
)

enum class UserBudgetTier(
  val titleAr: String,
  val rangeAr: String,
  val maxAllocationCap: Double,
  val targetAssetClass: String,
  val badgeColor: Long
) {
  MICRO_SEED(
    titleAr = "المحفظة المجهرية (Micro-Seed)",
    rangeAr = "1$ - 50$",
    maxAllocationCap = 50.0,
    targetAssetClass = "عملات واعدة منخفضة السعر وعالية النمو (Emerging Micro Gems & Layer-2)",
    badgeColor = 0xFF00E5FF
  ),
  GROWTH_MOMENTUM(
    titleAr = "محفظة الزخم المتوسط (Growth Momentum)",
    rangeAr = "50$ - 500$",
    maxAllocationCap = 500.0,
    targetAssetClass = "قادة قطاع الـ DeFi والزخم السريع (SOL, SUI, NEAR, RENDER, AVAX)",
    badgeColor = 0xFF00E676
  ),
  WHALE_INSTITUTIONAL(
    titleAr = "المحفظة الكبيرة والسيادية (Whale Tier)",
    rangeAr = "500$ فأكثر",
    maxAllocationCap = 100000.0,
    targetAssetClass = "الأصول الأساسية عالية السيولة (BTC, ETH, BNB) بأمان فائق وانزلاق صفري",
    badgeColor = 0xFFFFD700
  )
}

data class AutoSwapGemOpportunity(
  val id: String,
  val fromAsset: String,
  val toAsset: String,
  val toAssetNameAr: String,
  val currentPrice: Double,
  val expectedGainPercent: Double,
  val confidenceScore: Int,
  val suitableTier: UserBudgetTier,
  val minimumBudgetUsd: Double,
  val algorithmReasonAr: String,
  val momentumIndicator: String,
  val isUnlistedOrEmerging: Boolean = false,
  val riskRating: String = "SAFE_ALPHA"
)

data class AutoSwapEngineState(
  val isAutoSwapEnabled: Boolean = false,
  val userCustomBudgetUsd: Double = 25.0,
  val currentTier: UserBudgetTier = UserBudgetTier.MICRO_SEED,
  val autoRebalanceActive: Boolean = false,
  val totalAutoRotationsCompleted: Int = 18,
  val totalGeneratedProfitUsd: Double = 78.40,
  val activeOpportunity: AutoSwapGemOpportunity? = null,
  val availableOpportunities: List<AutoSwapGemOpportunity> = emptyList()
)

data class AssetRotationSignal(
  val currentAsset: String,
  val suggestedAsset: String,
  val confidenceScore: Int,
  val reason: String,
  val riskLevel: String, // SAFE / VOLATILE / DANGER
  val targetGainPercent: Double
)

data class MarketIntelligenceAlert(
  val id: String,
  val title: String,
  val type: String, // OPPORTUNITY / DANGER_ALERT / TACTICAL_ADVICE
  val symbol: String,
  val summary: String,
  val actionAdvice: String,
  val timestamp: String
)

// ==========================================
// 1. AUTO-STRATEGY ENGINE MODELS
// ==========================================
enum class StrategyPriority(val labelAr: String, val labelEn: String) {
  MAX_GROWTH("أقصى نمو مركب (Aggressive Compound)", "Max Compound Growth"),
  BALANCED_LIQUIDITY("سيولة متوازنة (Balanced Liquidity)", "Balanced Liquidity"),
  CAPITAL_PRESERVATION("أمان مطلق وحفظ رأس المال", "Capital Preservation")
}

data class MicroTradeExecution(
  val id: String,
  val pair: String,
  val side: String, // BUY / SELL / SWAP
  val allocatedAmountUsd: Double,
  val entryPrice: Double,
  val exitTargetPrice: Double,
  val currentProfitUsd: Double,
  val currentProfitPercent: Double,
  val status: String, // EXECUTING / HARVESTED / PREPARING
  val timeElapsed: String,
  val liquidityScore: Int, // 1 - 100
  val volatilityIndex: Double
)

data class AutoStrategyState(
  val isRunning: Boolean = true,
  val activeStrategyName: String = "محرك التدوير التلقائي المجهري (Micro-Scalp Compounder)",
  val constrainedPortfolioCapUsd: Double = 1248.50,
  val maxTradeAllocationPercent: Double = 15.0, // Max 15% per trade for risk management
  val priority: StrategyPriority = StrategyPriority.BALANCED_LIQUIDITY,
  val targetPairs: List<String> = listOf("SUI/USDT", "SOL/USDT", "NEAR/USDT", "RENDER/USDT", "BTC/USDT"),
  val activeTrades: List<MicroTradeExecution> = emptyList(),
  val dailyMicroProfitUsd: Double = 46.80,
  val completedTradesCount: Int = 34,
  val winRatePercent: Double = 97.1,
  val averageExecutionLatencyMs: Double = 11.4
)

// ==========================================
// 2. NETWORK TELEMETRY & LATENCY OVERLAY
// ==========================================
data class NetworkLatencyMetrics(
  val pingLatencyMs: Long = 12L,
  val jitterMs: Long = 2L,
  val packetsPerSecond: Int = 36,
  val packetLossPercent: Double = 0.0,
  val dataIntegrityScore: Double = 100.0,
  val hftExecutionReady: Boolean = true,
  val connectionBandwidthKbps: Double = 485.0,
  val streamProtocol: String = "WebSocket TLS 1.3 Direct Feed",
  val serverLocation: String = "Global HFT Relay (Direct Gateway)"
)

// ==========================================
// 3. MOMENTUM SHIFT & PREDICTION HEURISTICS
// ==========================================
enum class PredictionSignalType(val labelAr: String, val labelEn: String, val isPositive: Boolean) {
  BULLISH_MOMENTUM_EXPLOSION("انفجار زخم صاعد وشيك", "Bullish Momentum Surge", true),
  ORDER_BOOK_BUY_WALL("جدار طلبات شراء مؤسساتي قوي", "Institutional Buy Wall Detected", true),
  RSI_BEARISH_EXHAUSTION("تشبع شرائي وتصريف هابط مرتقب", "RSI Overbought Exhaustion", false),
  VOLATILITY_BREAKOUT("اختراق نطاق سعري عنيف", "Volatility Squeeze Breakout", true),
  SELL_PRESSURE_DIVERGENCE("انحراف بيعي مفاجئ", "Bearish Order Imbalance", false)
}

data class MomentumPredictionAlert(
  val id: String = "PRED-${System.currentTimeMillis() % 10000}",
  val pair: String,
  val signalType: PredictionSignalType,
  val confidencePercent: Int,
  val targetTimeHorizonSeconds: Int,
  val expectedMovePercent: Double,
  val currentPrice: Double,
  val targetPredictedPrice: Double,
  val orderBookImbalanceRatio: Double, // e.g. 2.85 (Bids exceed Asks by 2.85x)
  val rsi14Value: Double,
  val reasoningAr: String,
  val timestamp: String
)

// ==========================================
// 4. DIAGNOSTIC TELEMETRY & PERFORMANCE AUDIT
// ==========================================
data class DiagnosticPerformanceReport(
  val totalTradesAnalyzed: Int = 1420,
  val profitableTradesCount: Int = 1378,
  val winRatePercent: Double = 97.04,
  val totalVolumeProcessedUsd: Double = 284500.0,
  val netProfitGeneratedUsd: Double = 14850.20,
  val averageExecutionSpeedMs: Double = 12.3,
  val averageSlippagePercent: Double = 0.0012,
  val sharpeRatio: Double = 3.84,
  val maxDrawdownPercent: Double = 0.42, // Under 0.5% due to zero-loss shield
  val zeroLossShieldInterventions: Int = 19, // Prevented losses
  val capitalProtectedFromDumpsUsd: Double = 8920.0,
  val reportGeneratedDate: String = "2026-08-16",
  val auditSignatureSha256: String = "a7f3...9e21-APEX-AUDIT-VERIFIED"
)

// ==========================================
// 5. 4-TIER SUBSCRIPTION & VIP SOVEREIGN & AUTO-DEBIT MODELS
// ==========================================
enum class SubscriptionPlanId(
  val titleAr: String,
  val titleEn: String,
  val monthlyPriceUsd: Double,
  val annualPriceUsd: Double,
  val badgeColorHex: Long
) {
  BASIC_STARTER("الخطة الأساسية (Starter)", "Starter Core", 29.0, 278.0, 0xFF00E5FF),
  GROWTH_PRO("الخطة المتقدمة (Growth Pro)", "Growth Pro Arbitrage", 79.0, 758.0, 0xFF00E676),
  ELITE_APEX("خطة النخبة والسيادة (Elite Apex)", "Elite Apex Sovereign", 199.0, 1910.0, 0xFFFFD700),
  VIP_SOVEREIGN_WHALE("تاج الحوت السيادي لكبار العملاء (VIP Sovereign Whale)", "VIP Sovereign Whale", 999.0, 9590.0, 0xFFFF3366)
}

enum class BillingCycle(val labelAr: String, val discountPercent: Int) {
  MONTHLY("اشتراك شهري مرن", 0),
  ANNUAL("اشتراك سنوي (خصم 20% + شهرين مجاناً)", 20)
}

enum class AutoDebitStatus(val labelAr: String) {
  ACTIVE_SYNCED("مفعل ومزامن بالتوقيت الذري"),
  PAUSED("متوقف مؤقتاً بطلب العميل"),
  PENDING_RETRY("جاري إعادة المحاولة")
}

data class CryptoAutoConversionConfig(
  val isAutoConversionEnabled: Boolean = true,
  val fallbackPriorityCurrencies: List<String> = listOf("USDT", "USDC", "SOL", "BTC", "ETH", "SUI", "BNB", "TON"),
  val slippageTolerancePercent: Double = 0.05,
  val zeroDelayExecution: Boolean = true
)

data class UserSubscriptionState(
  val currentPlan: SubscriptionPlanId = SubscriptionPlanId.GROWTH_PRO,
  val billingCycle: BillingCycle = BillingCycle.MONTHLY,
  val isActive: Boolean = true,
  val startDate: String = "2026-08-01",
  val nextBillingDate: String = "2026-09-01",
  val autoDebitStatus: AutoDebitStatus = AutoDebitStatus.ACTIVE_SYNCED,
  val autoDebitCurrency: String = "USDT",
  val conversionConfig: CryptoAutoConversionConfig = CryptoAutoConversionConfig(),
  val lastDeductionTxHash: String = "0x89f4b...38c1",
  val totalSavedFeesUsd: Double = 1420.50,
  val isVipDedicatedSupportAssigned: Boolean = true
)

data class SubscriptionPlanDetails(
  val id: SubscriptionPlanId,
  val monthlyPriceUsd: Double,
  val annualPriceUsd: Double,
  val billingPeriodAr: String = "شهرياً",
  val isPopular: Boolean = false,
  val isVipExclusive: Boolean = false,
  val targetAudienceAr: String,
  val unlockedFeatures: List<String>,
  val maxPortfolioBudgetCap: String,
  val profitPotentialRating: String,
  val priorityLevelAr: String,
  val latencyGuarantee: String,
  val capitalHandlingTier: String
)

// ==========================================
// 6. ADAPTIVE CROSS-PAIR ARBITRAGE MODELS
// ==========================================
data class CrossPairArbitrageRoute(
  val routeId: String,
  val sourceAsset: String,
  val intermediateAsset: String,
  val targetAsset: String,
  val estimatedNetYieldPercent: Double,
  val executionSpeedMs: Double,
  val liquidityDepthUsd: Double,
  val minRequiredTier: SubscriptionPlanId,
  val isExecuted: Boolean = false
)
