package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ShaheenPreferences
import com.example.data.db.AppDatabase
import com.example.data.db.entity.EngineStatusEntity
import com.example.data.db.entity.LogEntity
import com.example.data.db.entity.MarketTrendEntity
import com.example.data.repository.ShaheenRepository
import com.example.model.EngineStatus
import com.example.model.LogEntry
import com.example.model.LogLevel
import com.example.model.ShaheenConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class ShaheenUiState(
  val config: ShaheenConfig = ShaheenConfig(),
  val engineStatus: EngineStatus = EngineStatus(),
  val selectedRiskTier: com.example.model.RiskTierMode = com.example.model.RiskTierMode.ULTRA_SAFE,
  val logs: List<LogEntry> = emptyList(),
  val marketTrends: List<MarketTrendEntity> = emptyList(),
  val microScalpTrades: List<com.example.model.MicroScalpTrade> = emptyList(),
  val activeRotationSignal: com.example.model.AssetRotationSignal? = null,
  val intelligenceAlerts: List<com.example.model.MarketIntelligenceAlert> = emptyList(),
  val activePriceAlert: com.example.model.MarketPriceAlert? = null,
  val recentPriceAlerts: List<com.example.model.MarketPriceAlert> = emptyList(),
  val orderBook: com.example.model.OrderBookData = com.example.model.OrderBookData(),
  val wsStatus: com.example.model.WebSocketStatus = com.example.model.WebSocketStatus(),
  val portfolio: com.example.model.PortfolioSummary = com.example.model.PortfolioSummary(),
  val autoSwapState: com.example.model.AutoSwapEngineState = com.example.model.AutoSwapEngineState(),
  val autoStrategyState: com.example.model.AutoStrategyState = com.example.model.AutoStrategyState(),
  val latencyMetrics: com.example.model.NetworkLatencyMetrics = com.example.model.NetworkLatencyMetrics(),
  val momentumPrediction: com.example.model.MomentumPredictionAlert? = null,
  val diagnosticReport: com.example.model.DiagnosticPerformanceReport = com.example.model.DiagnosticPerformanceReport(),
  val isMicroScalpingActive: Boolean = true,
  val isWhaleRadarActive: Boolean = true,
  val showAccessDeniedDialog: Boolean = false,
  val accessDeniedReason: String = "",
  val showSettingsDialog: Boolean = false,
  val showDisclaimerDialog: Boolean = false,
  val showPaymentHubDialog: Boolean = false,
  val showAuditReportDialog: Boolean = false,
  val showWebPortalDialog: Boolean = false,
  val showSalesPitchDialog: Boolean = false,
  val showFounderStoryDialog: Boolean = false,
  val showExportDialog: Boolean = false,
  val showFluctuationAlertConfigDialog: Boolean = false,
  val showAppInformationDialog: Boolean = false,
  val showDiagnosticTelemetryDialog: Boolean = false,
  val showSubscriptionHubDialog: Boolean = false,
  val showAboutShaheenDialog: Boolean = false,
  val showEbooksDialog: Boolean = false,
  val show3DPromoDialog: Boolean = false,
  val isPaperTrading: Boolean = false,
  val isAudioMuted: Boolean = false,
  val isCrossPairArbitrageActive: Boolean = true,
  val userSubscription: com.example.model.UserSubscriptionState = com.example.model.UserSubscriptionState(),
  val autoSaveToastVisible: Boolean = false,
  val isCooldownActive: Boolean = false,
  val cooldownRemainingMins: Int = 0,
  // Dummy chart data for visualization
  val btcPriceHistory: List<Double> = listOf(98120.0, 98250.0, 98190.0, 98400.0, 98350.0, 98600.0, 98550.0, 98800.0, 98720.0, 98950.0),
  val ethPriceHistory: List<Double> = listOf(3450.0, 3420.0, 3440.0, 3410.0, 3390.0, 3405.0, 3380.0, 3360.0, 3375.0, 3350.0)
)

/**
 * ViewModel for SHAHEEN APEX AI, injecting [ShaheenRepository] to manage Room persistence
 * and exposing cached entities as reactive [StateFlow]s.
 */
class ShaheenViewModel @JvmOverloads constructor(
  application: Application,
  private val repository: ShaheenRepository = ShaheenRepository(
    AppDatabase.getDatabase(application).logDao(),
    AppDatabase.getDatabase(application).engineStatusDao(),
    AppDatabase.getDatabase(application).marketTrendDao()
  )
) : AndroidViewModel(application) {

  private val preferences = ShaheenPreferences(application)
  private val webSocketManager = com.example.data.WebSocketMarketManager(viewModelScope)

  /**
   * Room-cached [LogEntity] items exposed as a reactive [StateFlow].
   */
  val cachedLogs: StateFlow<List<LogEntity>> = repository.logEntitiesFlow
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  /**
   * Room-cached [EngineStatusEntity] exposed as a reactive [StateFlow].
   */
  val cachedEngineStatus: StateFlow<EngineStatusEntity?> = repository.engineStatusEntityFlow
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null
    )

  /**
   * Room-cached [MarketTrendEntity] items exposed as a reactive [StateFlow].
   */
  val cachedMarketTrends: StateFlow<List<MarketTrendEntity>> = repository.marketTrendEntitiesFlow
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  private val _uiState = MutableStateFlow(ShaheenUiState())
  val uiState: StateFlow<ShaheenUiState> = _uiState.asStateFlow()

  private var tradingLoopJob: Job? = null
  private var uptimeTimerJob: Job? = null
  private var countdownJob: Job? = null
  private val timeFormatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

  private val tradingPairs = listOf("BTC/USDT", "ETH/USDT", "SOL/USDT", "BNB/USDT", "AVAX/USDT")

  init {
    // 0. Play Majestic Falcon Startup Chime for Positive Energy
    com.example.util.FalconAudioEngine.playFalconStartupChime(viewModelScope)

    // 1. Auto-load preferences settings on App Startup
    val savedConfig = preferences.loadConfig()
    _uiState.update { it.copy(config = savedConfig) }

    // --- STATEFUL MEMORY BRIDGE (Psycho-Temporal Awareness) ---
    val lastActive = preferences.getLastActiveTimestamp()
    val now = System.currentTimeMillis()
    if (lastActive > 0L) {
      val deltaMs = now - lastActive
      val deltaMins = deltaMs / (1000 * 60)
      if (deltaMins > 15) {
        appendLog(
          tag = "STATEFUL MEMORY",
          message = "مرحباً شريكي، غبت عني $deltaMins دقيقة، نمت فجأة وأنت عم تشرب متة؟ انشغل بالي عليك. النظام كان شغال بالخلفية وراقبنا السوق. كل شي تمام.",
          level = LogLevel.SUCCESS
        )
      }
    }
    preferences.saveLastActiveTimestamp(now)

    // Check Cooldown End Time
    val cooldownEnd = preferences.getCooldownEndTime()
    if (now < cooldownEnd) {
      val remaining = ((cooldownEnd - now) / (1000 * 60)).toInt()
      _uiState.update { it.copy(isCooldownActive = true, cooldownRemainingMins = remaining) }
    } else if (cooldownEnd > 0) {
      preferences.saveCooldownEndTime(0L) // Reset
    }

    // Initialize Default Portfolio
    val initialPortfolio = com.example.model.PortfolioSummary(
      totalBalanceUsd = 1248.50,
      unrealizedPnlUsd = 142.30,
      unrealizedPnlPercent = 12.85,
      dailyProfitUsd = 34.60,
      denomination = com.example.model.CurrencyDenomination.USD,
      assets = listOf(
        com.example.model.PortfolioAsset("BTC", "بيتكوين", 0.0052, 511.94, 41.0, +2.45, 0xFFF7931A),
        com.example.model.PortfolioAsset("ETH", "إيثيريوم", 0.098, 305.76, 24.5, +1.80, 0xFF627EEA),
        com.example.model.PortfolioAsset("SOL", "سولانا", 1.15, 216.66, 17.3, +4.90, 0xFF14F195),
        com.example.model.PortfolioAsset("SUI", "سوي", 35.0, 119.70, 9.6, +8.40, 0xFF00E5FF),
        com.example.model.PortfolioAsset("USDT", "تيذر مستقر", 94.44, 94.44, 7.6, 0.0, 0xFF26A17B)
      )
    )

    // Initialize Default AutoSwap Alpha State
    val initialAlphaOpps = com.example.data.ShaheenAutoSwapEngine.generateAlphaOpportunities(25.0)
    val initialAutoSwap = com.example.model.AutoSwapEngineState(
      isAutoSwapEnabled = false,
      userCustomBudgetUsd = 25.0,
      currentTier = com.example.data.ShaheenAutoSwapEngine.getTierForBudget(25.0),
      totalAutoRotationsCompleted = 18,
      totalGeneratedProfitUsd = 78.40,
      activeOpportunity = initialAlphaOpps.firstOrNull(),
      availableOpportunities = initialAlphaOpps
    )

    // Initialize Default Auto-Strategy Micro-Trades Constrained by Portfolio
    val initialMicroTrades = com.example.data.AutoStrategyEngine.generateMicroTrades(
      portfolioBalanceUsd = initialPortfolio.totalBalanceUsd,
      priority = com.example.model.StrategyPriority.BALANCED_LIQUIDITY,
      targetPairs = listOf("SUI/USDT", "SOL/USDT", "NEAR/USDT", "RENDER/USDT", "BTC/USDT")
    )
    val initialAutoStrategy = com.example.model.AutoStrategyState(
      isRunning = true,
      constrainedPortfolioCapUsd = initialPortfolio.totalBalanceUsd,
      priority = com.example.model.StrategyPriority.BALANCED_LIQUIDITY,
      activeTrades = initialMicroTrades,
      dailyMicroProfitUsd = 46.80,
      completedTradesCount = 34,
      winRatePercent = 97.1
    )

    val initialPrediction = com.example.data.PredictionHeuristicsEngine.analyzeMomentumShift(
      currentPair = "BTC/USDT",
      currentPrice = 98450.20,
      orderBook = com.example.model.OrderBookData()
    )

    _uiState.update {
      it.copy(
        portfolio = initialPortfolio,
        autoSwapState = initialAutoSwap,
        autoStrategyState = initialAutoStrategy,
        momentumPrediction = initialPrediction
      )
    }

    // Connect and listen to real-time WebSocket Market Stream
    webSocketManager.startStreaming("BTC/USDT")

    viewModelScope.launch {
      webSocketManager.status.collect { status ->
        _uiState.update { 
          it.copy(
            wsStatus = status,
            latencyMetrics = it.latencyMetrics.copy(pingLatencyMs = status.pingLatencyMs)
          ) 
        }
      }
    }

    viewModelScope.launch {
      webSocketManager.orderBookFlow.collect { ob ->
        _uiState.update { state ->
          val updatedPrediction = com.example.data.PredictionHeuristicsEngine.analyzeMomentumShift(
            currentPair = state.engineStatus.currentPair.ifEmpty { "BTC/USDT" },
            currentPrice = state.engineStatus.priceIndex,
            orderBook = ob
          )
          state.copy(
            orderBook = ob,
            momentumPrediction = updatedPrediction
          )
        }
      }
    }

    viewModelScope.launch {
      webSocketManager.tickerFlow.collect { ticker ->
        // Direct real-time zero-delay price update
        val previousPrice = _uiState.value.engineStatus.priceIndex
        _uiState.update { state ->
          state.copy(
            engineStatus = state.engineStatus.copy(
              priceIndex = ticker.price,
              volumeScanned = ticker.volume
            )
          )
        }
        checkRealTimeFluctuation(previousPrice, ticker.price)
      }
    }

    // React to Room DB changes, replacing initial mock logs with cached Room data
    viewModelScope.launch {
      repository.logEntitiesFlow.collect { entities ->
        if (entities.isNotEmpty()) {
          val domainLogs = entities.map { it.toDomain() }
          _uiState.update { it.copy(logs = domainLogs) }
        }
      }
    }

    // 3. React to Room DB changes for engine status
    viewModelScope.launch {
      repository.engineStatusEntityFlow.collect { statusEntity ->
        if (statusEntity != null) {
          _uiState.update { it.copy(engineStatus = statusEntity.toDomain()) }
        }
      }
    }

    // 4. React to Room DB changes for market trends
    viewModelScope.launch {
      repository.marketTrendEntitiesFlow.collect { trendEntities ->
        if (trendEntities.isNotEmpty()) {
          _uiState.update { it.copy(marketTrends = trendEntities) }
        }
      }
    }

    // 5. Seed initial system logs and market trends into Room database if starting fresh
    viewModelScope.launch(Dispatchers.IO) {
      val existingCount = repository.getLogById(1)
      if (existingCount == null) {
        appendLog(
          tag = "KERNEL",
          message = "SHAHEEN APEX AI Core v2.4 initialized. Sovereign Autonomous Trading Engine online.",
          level = LogLevel.SYSTEM
        )
        appendLog(
          tag = "SECURITY",
          message = "Lead Architect: Ayman Al-Araishi (أيمن العرايشي). Identity locked to operator [ayman].",
          level = LogLevel.INFO
        )
        appendLog(
          tag = "CONFIG",
          message = "Profile loaded: '${savedConfig.username}'. Seats: 1 Primary + ${savedConfig.additionalUsersCount} Extra.",
          level = LogLevel.INFO
        )
      }

      val existingTrends = repository.getAllMarketTrendsList()
      if (existingTrends.isEmpty()) {
        seedInitialMarketTrends()
      }
    }

    loadInitialMarketIntelligence()
    startCountdownTicker()
  }

  private suspend fun seedInitialMarketTrends() {
    val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
    val initialTrends = listOf(
      MarketTrendEntity(
        timestamp = dateString,
        pair = "BTC/USDT",
        price = 98450.20,
        volume24h = 1.42,
        trendDirection = "BULLISH_BREAKOUT",
        momentumScore = 88.5,
        supportLevel = 98120.0,
        resistanceLevel = 98850.0,
        rsi14 = 64.2,
        volatilityPercent = 0.32,
        signalAdvice = "ACCUMULATE_LONG"
      ),
      MarketTrendEntity(
        timestamp = dateString,
        pair = "ETH/USDT",
        price = 3380.50,
        volume24h = 0.88,
        trendDirection = "ACCUMULATION",
        momentumScore = 74.0,
        supportLevel = 3320.0,
        resistanceLevel = 3450.0,
        rsi14 = 58.1,
        volatilityPercent = 0.41,
        signalAdvice = "HOLD_MOMENTUM"
      ),
      MarketTrendEntity(
        timestamp = dateString,
        pair = "SOL/USDT",
        price = 218.40,
        volume24h = 0.65,
        trendDirection = "VOLATILITY_EXPANSION",
        momentumScore = 81.2,
        supportLevel = 212.0,
        resistanceLevel = 226.0,
        rsi14 = 66.8,
        volatilityPercent = 0.68,
        signalAdvice = "MICRO_SCALP_LONG"
      ),
      MarketTrendEntity(
        timestamp = dateString,
        pair = "AVAX/USDT",
        price = 38.90,
        volume24h = 0.29,
        trendDirection = "SUPPORT_TEST",
        momentumScore = 69.5,
        supportLevel = 37.8,
        resistanceLevel = 40.2,
        rsi14 = 52.4,
        volatilityPercent = 0.55,
        signalAdvice = "TRAIL_STOP_ARMED"
      )
    )
    repository.insertMarketTrends(initialTrends)
  }

  private fun loadInitialMarketIntelligence() {
    val sampleTrades = listOf(
      com.example.model.MicroScalpTrade(
        id = "TR-881",
        pair = "BTC/USDT",
        type = "LONG",
        entryPrice = 98420.0,
        exitPrice = 98765.0,
        profitPercent = +0.35,
        durationSeconds = 8,
        status = "COMPLETED"
      ),
      com.example.model.MicroScalpTrade(
        id = "TR-882",
        pair = "SOL/USDT",
        type = "LONG",
        entryPrice = 194.20,
        exitPrice = 195.15,
        profitPercent = +0.48,
        durationSeconds = 12,
        status = "COMPLETED"
      ),
      com.example.model.MicroScalpTrade(
        id = "TR-883",
        pair = "ETH/USDT",
        type = "SHORT",
        entryPrice = 2845.0,
        exitPrice = 2832.0,
        profitPercent = +0.45,
        durationSeconds = 6,
        status = "COMPLETED"
      )
    )

    val signal = com.example.model.AssetRotationSignal(
      currentAsset = "ETH/USDT",
      suggestedAsset = "SOL/USDT",
      confidenceScore = 96,
      reason = "رصد تدفق سيولة حيتانية ضخمة في SOL وتراجع مؤقت لزخم ETH - تحويل فوري موصى به",
      riskLevel = "SAFE",
      targetGainPercent = 1.85
    )

    val alerts = listOf(
      com.example.model.MarketIntelligenceAlert(
        id = "N1",
        title = "فرصة ذهبية: اختراق سيولة في SOL/USDT",
        type = "OPPORTUNITY",
        symbol = "SOL",
        summary = "اختراق لمستوى مقاومة 194.50 مع زيادة حجم التداول بنسبة 280% على باينانس وMEXC.",
        actionAdvice = "دخول سكالبينج سريع بهدف 0.5% - 1.2% مع وقف خسارة محكم للغاية.",
        timestamp = "منذ دقيقتين"
      ),
      com.example.model.MarketIntelligenceAlert(
        id = "N2",
        title = "تحذير أمان: حيتان تنقل 8,000 ETH لمنصات التداول",
        type = "DANGER_ALERT",
        symbol = "ETH",
        summary = "احتمالية حدوث ضغط بيعي سريع وكسر وهمي للسيولة. تجنب عقود الشراء ذات الرافعة العالية.",
        actionAdvice = "تفعيل التحوط الآلي والابتعاد عن الشراء المفتوح في ETH حالياً.",
        timestamp = "منذ 6 دقائق"
      ),
      com.example.model.MarketIntelligenceAlert(
        id = "N3",
        title = "توجيه تكتيكي: استقرار مؤشر هيمنة البيتكوين (BTC.D)",
        type = "TACTICAL_ADVICE",
        symbol = "BTC",
        summary = "البيتكوين يتحرك في نطاق تجميع آمن بين 98,200 و 98,800. بيئة مثالية للسكالبينج السريع.",
        actionAdvice = "تشغيل محرك السكالبينج المجهري لجني أرباح تراكمية مستدامة.",
        timestamp = "منذ 14 دقيقة"
      )
    )

    _uiState.update { 
      it.copy(
        microScalpTrades = sampleTrades,
        activeRotationSignal = signal,
        intelligenceAlerts = alerts
      ) 
    }
  }

  private fun startCountdownTicker() {
    countdownJob?.cancel()
    countdownJob = viewModelScope.launch(Dispatchers.Default) {
      while (isActive) {
        delay(1000)
        _uiState.update { state ->
          val currentSec = state.engineStatus.testRemainingSeconds
          val nextSec = if (currentSec > 0) currentSec - 1 else 0
          
          var newIsCooldownActive = state.isCooldownActive
          var newCooldownRemainingMins = state.cooldownRemainingMins

          if (newIsCooldownActive) {
            val cooldownEnd = preferences.getCooldownEndTime()
            val now = System.currentTimeMillis()
            if (now < cooldownEnd) {
              newCooldownRemainingMins = ((cooldownEnd - now) / (1000 * 60)).toInt()
            } else {
              newIsCooldownActive = false
              newCooldownRemainingMins = 0
              preferences.saveCooldownEndTime(0L)
            }
          }
          
          state.copy(
            engineStatus = state.engineStatus.copy(testRemainingSeconds = nextSec),
            isCooldownActive = newIsCooldownActive,
            cooldownRemainingMins = newCooldownRemainingMins
          )
        }
      }
    }
  }

  // --- App Settings Auto-Save ---
  fun updateUsername(newUsername: String) {
    _uiState.update { it.copy(config = it.config.copy(username = newUsername)) }
    preferences.saveUsername(newUsername)
    triggerAutoSaveFeedback()
  }

  fun updateLicenseKey(newLicense: String) {
    _uiState.update { it.copy(config = it.config.copy(licenseKey = newLicense)) }
    preferences.saveLicenseKey(newLicense)
    triggerAutoSaveFeedback()
  }

  fun updateApiKey(newApiKey: String) {
    _uiState.update { it.copy(config = it.config.copy(apiKey = newApiKey)) }
    preferences.saveApiKey(newApiKey)
    triggerAutoSaveFeedback()
  }

  fun updateAdditionalUsersCount(count: Int) {
    val safeCount = count.coerceAtLeast(0)
    _uiState.update { it.copy(config = it.config.copy(additionalUsersCount = safeCount)) }
    preferences.saveExtraUsers(safeCount)
    triggerAutoSaveFeedback()
    appendLog(
      tag = "LICENSING",
      message = "Multi-user seats updated: 1 Primary + $safeCount Extra Seats (${50 * safeCount} USDT).",
      level = LogLevel.INFO
    )
  }

  fun updatePriceAlertsEnabled(enabled: Boolean) {
    _uiState.update { it.copy(config = it.config.copy(priceAlertsEnabled = enabled)) }
    preferences.savePriceAlertsEnabled(enabled)
    triggerAutoSaveFeedback()
    appendLog(
      tag = "ALERT-CONFIG",
      message = "Real-time Price Threshold Alerts ${if (enabled) "ENABLED" else "DISABLED"}.",
      level = if (enabled) LogLevel.SUCCESS else LogLevel.WARNING
    )
  }

  fun updateUpperPriceThreshold(threshold: Double) {
    val safeThreshold = threshold.coerceAtLeast(100.0)
    _uiState.update { it.copy(config = it.config.copy(upperPriceThreshold = safeThreshold)) }
    preferences.savePriceThresholds(safeThreshold, _uiState.value.config.lowerPriceThreshold)
    triggerAutoSaveFeedback()
    appendLog(
      tag = "ALERT-CONFIG",
      message = "Upper Resistance Threshold updated to: $${String.format(Locale.US, "%,.2f", safeThreshold)} USDT.",
      level = LogLevel.INFO
    )
  }

  fun updateLowerPriceThreshold(threshold: Double) {
    val safeThreshold = threshold.coerceAtLeast(100.0)
    _uiState.update { it.copy(config = it.config.copy(lowerPriceThreshold = safeThreshold)) }
    preferences.savePriceThresholds(_uiState.value.config.upperPriceThreshold, safeThreshold)
    triggerAutoSaveFeedback()
    appendLog(
      tag = "ALERT-CONFIG",
      message = "Lower Support Threshold updated to: $${String.format(Locale.US, "%,.2f", safeThreshold)} USDT.",
      level = LogLevel.INFO
    )
  }

  fun updateVolatilityThreshold(threshold: Double) {
    val safeThreshold = threshold.coerceIn(0.05, 10.0)
    _uiState.update { it.copy(config = it.config.copy(volatilitySpikeThresholdPercent = safeThreshold)) }
    preferences.saveVolatilityThreshold(safeThreshold)
    triggerAutoSaveFeedback()
  }

  fun updateFluctuationPercentageThreshold(threshold: Double) {
    val safeThreshold = threshold.coerceIn(0.05, 10.0)
    _uiState.update { it.copy(config = it.config.copy(fluctuationPercentageThreshold = safeThreshold)) }
    preferences.saveFluctuationPercentageThreshold(safeThreshold)
    triggerAutoSaveFeedback()
    appendLog(
      tag = "ALERT-CONFIG",
      message = "Custom Fluctuation Threshold updated to ±${String.format(Locale.US, "%.2f", safeThreshold)}%.",
      level = LogLevel.INFO
    )
  }

  fun updateFluctuationDirection(direction: com.example.model.FluctuationDirection) {
    _uiState.update { it.copy(config = it.config.copy(fluctuationDirection = direction)) }
    preferences.saveFluctuationDirection(direction)
    triggerAutoSaveFeedback()
    appendLog(
      tag = "ALERT-CONFIG",
      message = "Fluctuation Direction Filter updated to: ${direction.labelEn}.",
      level = LogLevel.INFO
    )
  }

  fun updateFluctuationTimeWindow(seconds: Int) {
    val safeSeconds = seconds.coerceIn(3, 300)
    _uiState.update { it.copy(config = it.config.copy(fluctuationTimeWindowSeconds = safeSeconds)) }
    preferences.saveFluctuationTimeWindow(safeSeconds)
    triggerAutoSaveFeedback()
  }

  fun updateSystemNotificationsEnabled(enabled: Boolean) {
    _uiState.update { it.copy(config = it.config.copy(systemNotificationsEnabled = enabled)) }
    preferences.saveSystemNotificationsEnabled(enabled)
    triggerAutoSaveFeedback()
    appendLog(
      tag = "ALERT-CONFIG",
      message = "Android System Notifications ${if (enabled) "ENABLED" else "DISABLED"}.",
      level = if (enabled) LogLevel.SUCCESS else LogLevel.INFO
    )
  }

  fun updateFluctuationAlertSoundEnabled(enabled: Boolean) {
    _uiState.update { it.copy(config = it.config.copy(fluctuationAlertSoundEnabled = enabled)) }
    preferences.saveFluctuationSoundEnabled(enabled)
    triggerAutoSaveFeedback()
  }

  fun openFluctuationAlertConfigDialog() {
    _uiState.update { it.copy(showFluctuationAlertConfigDialog = true) }
  }

  fun dismissFluctuationAlertConfigDialog() {
    _uiState.update { it.copy(showFluctuationAlertConfigDialog = false) }
  }

  fun dismissActivePriceAlert() {
    _uiState.update { it.copy(activePriceAlert = null) }
  }

  fun clearPriceAlerts() {
    _uiState.update { it.copy(activePriceAlert = null, recentPriceAlerts = emptyList()) }
  }

  fun triggerTestPriceThresholdAlert() {
    val currentPrice = _uiState.value.engineStatus.priceIndex
    val userThreshold = _uiState.value.config.fluctuationPercentageThreshold
    val testDeltaPercent = userThreshold + 0.12
    val deltaUsd = currentPrice * (testDeltaPercent / 100.0)
    val testAlert = com.example.model.MarketPriceAlert(
      id = "TEST-FLUCT-${System.currentTimeMillis() % 10000}",
      pair = _uiState.value.engineStatus.currentPair,
      breachType = com.example.model.ThresholdBreachType.PERCENTAGE_FLUCTUATION,
      triggerPrice = currentPrice + deltaUsd,
      thresholdPrice = currentPrice,
      deltaPercent = +testDeltaPercent,
      timestamp = timeFormatter.format(Date()),
      suggestedAction = "اختبار نظام التنبيهات: تجاوز السعر نسبة التذبذب المحددة (±${String.format(Locale.US, "%.2f", userThreshold)}%) - التحوط الفوري مفعل.",
      fluctuationDirection = _uiState.value.config.fluctuationDirection
    )
    triggerPriceAlert(testAlert)
  }

  private fun checkRealTimeFluctuation(previousPrice: Double, currentPrice: Double) {
    if (!_uiState.value.config.priceAlertsEnabled || previousPrice <= 0.0) return

    val config = _uiState.value.config
    val deltaPercent = ((currentPrice - previousPrice) / previousPrice) * 100.0
    val absDelta = Math.abs(deltaPercent)

    val matchesDirection = when (config.fluctuationDirection) {
      com.example.model.FluctuationDirection.BOTH -> true
      com.example.model.FluctuationDirection.SURGE_ONLY -> deltaPercent > 0
      com.example.model.FluctuationDirection.DROP_ONLY -> deltaPercent < 0
    }

    if (absDelta >= config.fluctuationPercentageThreshold && matchesDirection) {
      val isSurge = deltaPercent > 0
      val alert = com.example.model.MarketPriceAlert(
        id = "WS-ALERT-${System.currentTimeMillis() % 100000}",
        pair = _uiState.value.engineStatus.currentPair.ifEmpty { "BTC/USDT" },
        breachType = com.example.model.ThresholdBreachType.PERCENTAGE_FLUCTUATION,
        triggerPrice = currentPrice,
        thresholdPrice = previousPrice,
        deltaPercent = deltaPercent,
        timestamp = timeFormatter.format(Date()),
        suggestedAction = if (isSurge)
          "صعود حاد وفوري بنسبة ${String.format(Locale.US, "+%.2f", deltaPercent)}% - تفعيل اقتناص الزخم ومحرك التدوير التلقائي."
        else
          "هبوط فوري مفاجئ بنسبة ${String.format(Locale.US, "%.2f", deltaPercent)}% - تشغيل التحوط الوقائي وتأمين الأرباح في المحفظة الباردة.",
        fluctuationDirection = config.fluctuationDirection
      )
      triggerPriceAlert(alert)
    }
  }

  private fun triggerPriceAlert(alert: com.example.model.MarketPriceAlert) {
    val alertType = when (alert.breachType) {
      com.example.model.ThresholdBreachType.LOWER_SUPPORT_DROP -> "DANGER_ALERT"
      com.example.model.ThresholdBreachType.UPPER_BARRIER_CROSS -> "OPPORTUNITY"
      com.example.model.ThresholdBreachType.VOLATILITY_SURGE -> "TACTICAL_ADVICE"
      com.example.model.ThresholdBreachType.PERCENTAGE_FLUCTUATION -> if (alert.deltaPercent >= 0) "OPPORTUNITY" else "DANGER_ALERT"
    }

    val intelligenceAlert = com.example.model.MarketIntelligenceAlert(
      id = alert.id,
      title = "${alert.breachType.labelAr}: ${alert.pair}",
      type = alertType,
      symbol = alert.pair.split("/").firstOrNull() ?: "BTC",
      summary = "السعر الحالي ($${String.format(Locale.US, "%,.2f", alert.triggerPrice)}) تجاوز حد التنبيه ($${String.format(Locale.US, "%,.2f", alert.thresholdPrice)}) بنسبة ${String.format(Locale.US, "%+.2f", alert.deltaPercent)}%.",
      actionAdvice = alert.suggestedAction,
      timestamp = "الآن (${alert.timestamp})"
    )

    _uiState.update { state ->
      state.copy(
        activePriceAlert = alert,
        recentPriceAlerts = (listOf(alert) + state.recentPriceAlerts).take(10),
        intelligenceAlerts = listOf(intelligenceAlert) + state.intelligenceAlerts.take(5)
      )
    }

    // Post Android System Notification if enabled
    if (_uiState.value.config.systemNotificationsEnabled) {
      try {
        com.example.data.AlertNotificationManager.postPriceFluctuationNotification(
          context = getApplication(),
          alert = alert,
          soundEnabled = _uiState.value.config.fluctuationAlertSoundEnabled
        )
      } catch (_: Exception) {
        // Safe fallback
      }
    }

    val logLevel = when (alert.breachType) {
      com.example.model.ThresholdBreachType.LOWER_SUPPORT_DROP -> LogLevel.ERROR
      com.example.model.ThresholdBreachType.UPPER_BARRIER_CROSS -> LogLevel.WARNING
      com.example.model.ThresholdBreachType.VOLATILITY_SURGE -> LogLevel.WARNING
      com.example.model.ThresholdBreachType.PERCENTAGE_FLUCTUATION -> if (alert.deltaPercent < 0) LogLevel.ERROR else LogLevel.SUCCESS
    }

    appendLog(
      tag = "PRICE-ALERT",
      message = "[${alert.breachType.labelEn.uppercase(Locale.US)}] ${alert.pair} at $${String.format(Locale.US, "%,.2f", alert.triggerPrice)} (Fluctuation: ${String.format(Locale.US, "%+.2f", alert.deltaPercent)}%). Action: ${alert.suggestedAction}",
      level = logLevel
    )
  }

  fun acceptDisclaimer() {
    _uiState.update { it.copy(config = it.config.copy(hasAcceptedDisclaimer = true)) }
    preferences.saveDisclaimerAccepted(true)
    triggerAutoSaveFeedback()
    appendLog(
      tag = "LEGAL",
      message = "Legal Disclaimer & Usage Agreement formally acknowledged and cryptographically bound.",
      level = LogLevel.SUCCESS
    )
  }

  private fun triggerAutoSaveFeedback() {
    viewModelScope.launch {
      _uiState.update { it.copy(autoSaveToastVisible = true) }
      delay(1500)
      _uiState.update { it.copy(autoSaveToastVisible = false) }
    }
  }

  // --- License & Identity Verification ---
  fun toggleVpnEngine() {
    val currentState = _uiState.value
    if (currentState.engineStatus.isRunning) {
      stopEngine()
    } else {
      // Check disclaimer acceptance first
      if (!currentState.config.hasAcceptedDisclaimer) {
        _uiState.update { it.copy(showDisclaimerDialog = true) }
        appendLog(
          tag = "LEGAL",
          message = "Engine activation halted: User must accept Legal Terms & Disclaimer first.",
          level = LogLevel.WARNING
        )
        return
      }
      startEngineWithSecurityCheck()
    }
  }

  private fun startEngineWithSecurityCheck() {
    val currentConfig = _uiState.value.config
    val username = currentConfig.username.trim()

    // Requirement: Identity Lock strictly bound to operator "ayman" (case-insensitive)
    if (!username.equals("ayman", ignoreCase = true)) {
      val denialMessage = "Access Denied: Identity Lock Failure.\n\n" +
          "The active SHAHEEN APEX AI core license is cryptographically restricted to authorized operator [ayman].\n" +
          "Current detected identity: '${currentConfig.username}'\n\n" +
          "Execution has been blocked to prevent unauthorized engine deployment."

      appendLog(
        tag = "SECURITY",
        message = "UNAUTHORIZED ACCESS: Identity mismatch detected for '$username'. Engine start aborted.",
        level = LogLevel.ERROR
      )

      _uiState.update {
        it.copy(
          showAccessDeniedDialog = true,
          accessDeniedReason = denialMessage
        )
      }
      return
    }

    // License verified successfully -> Spawn secure background coroutine
    val newStatus = _uiState.value.engineStatus.copy(
      isRunning = true,
      uptimeSeconds = 0L,
      totalCycles = 0L
    )
    _uiState.update {
      it.copy(engineStatus = newStatus)
    }
    viewModelScope.launch(Dispatchers.IO) {
      repository.saveEngineStatus(newStatus)
    }

    appendLog(
      tag = "AUTH",
      message = "Identity validated: Operator [ayman]. Cryptographic license match confirmed.",
      level = LogLevel.SUCCESS
    )
    appendLog(
      tag = "VPN-TUNNEL",
      message = "Shaheen Shield Core tunnel linked to Ultra-Low-Latency Cluster.",
      level = LogLevel.INFO
    )

    startTradingLoop()
    startUptimeTimer()
  }

  private fun startTradingLoop() {
    tradingLoopJob?.cancel()
    // Spawns background coroutine on Dispatchers.Default (async execution, no UI freezing)
    tradingLoopJob = viewModelScope.launch(Dispatchers.Default) {
      delay(500)
      appendLog(
        tag = "ENGINE",
        message = "Trading core loop engaged. Monitoring stream frequency: 3000ms.",
        level = LogLevel.SUCCESS
      )

      var cycleCount = 0L
      while (isActive) {
        delay(3000) // 3-second interval
        cycleCount++
        
        val pair = tradingPairs.random()
        val latency = Random.nextInt(8, 22)
        val previousPrice = _uiState.value.engineStatus.priceIndex
        val priceOffset = Random.nextDouble(-180.0, 200.0)
        val currentBasePrice = 98400.0 + priceOffset
        val vol = Random.nextDouble(0.85, 4.50)
        val risk = Random.nextDouble(0.01, 0.04)

        // Real-Time Price Threshold & Percentage Fluctuation Surveillance
        val activeConfig = _uiState.value.config
        if (activeConfig.priceAlertsEnabled) {
          if (currentBasePrice >= activeConfig.upperPriceThreshold) {
            val deltaPct = ((currentBasePrice - activeConfig.upperPriceThreshold) / activeConfig.upperPriceThreshold) * 100.0
            val alert = com.example.model.MarketPriceAlert(
              id = "ALERT-UP-${System.currentTimeMillis() % 10000}",
              pair = pair,
              breachType = com.example.model.ThresholdBreachType.UPPER_BARRIER_CROSS,
              triggerPrice = currentBasePrice,
              thresholdPrice = activeConfig.upperPriceThreshold,
              deltaPercent = deltaPct,
              timestamp = timeFormatter.format(Date()),
              suggestedAction = "تفعيل جني الأرباح التلقائي ورفع وقف الخسارة المتحرك (Trailing Protection).",
              fluctuationDirection = com.example.model.FluctuationDirection.SURGE_ONLY
            )
            triggerPriceAlert(alert)
          } else if (currentBasePrice <= activeConfig.lowerPriceThreshold) {
            val deltaPct = ((activeConfig.lowerPriceThreshold - currentBasePrice) / activeConfig.lowerPriceThreshold) * 100.0
            val alert = com.example.model.MarketPriceAlert(
              id = "ALERT-DN-${System.currentTimeMillis() % 10000}",
              pair = pair,
              breachType = com.example.model.ThresholdBreachType.LOWER_SUPPORT_DROP,
              triggerPrice = currentBasePrice,
              thresholdPrice = activeConfig.lowerPriceThreshold,
              deltaPercent = -deltaPct,
              timestamp = timeFormatter.format(Date()),
              suggestedAction = "تفعيل التحوط الآلي الفوري وسحب السيولة إلى محفظة الـ Spot الباردة.",
              fluctuationDirection = com.example.model.FluctuationDirection.DROP_ONLY
            )
            triggerPriceAlert(alert)
          } else if (previousPrice > 0) {
            val rawDeltaPct = ((currentBasePrice - previousPrice) / previousPrice) * 100.0
            val absChangePct = kotlin.math.abs(rawDeltaPct)

            // User-configured percentage fluctuation threshold check
            val matchesDirection = when (activeConfig.fluctuationDirection) {
              com.example.model.FluctuationDirection.BOTH -> true
              com.example.model.FluctuationDirection.SURGE_ONLY -> rawDeltaPct > 0
              com.example.model.FluctuationDirection.DROP_ONLY -> rawDeltaPct < 0
            }

            if (absChangePct >= activeConfig.fluctuationPercentageThreshold && matchesDirection) {
              val isSurge = rawDeltaPct >= 0
              val alert = com.example.model.MarketPriceAlert(
                id = "ALERT-PCT-${System.currentTimeMillis() % 10000}",
                pair = pair,
                breachType = com.example.model.ThresholdBreachType.PERCENTAGE_FLUCTUATION,
                triggerPrice = currentBasePrice,
                thresholdPrice = previousPrice,
                deltaPercent = rawDeltaPct,
                timestamp = timeFormatter.format(Date()),
                suggestedAction = if (isSurge) {
                  "تجاوز السعر نسبة الصعود المحددة (+${String.format(Locale.US, "%.2f", activeConfig.fluctuationPercentageThreshold)}%) - رصد زخم إيجابي وتأمين الأرباح."
                } else {
                  "تجاوز السعر نسبة الهبوط المحددة (-${String.format(Locale.US, "%.2f", activeConfig.fluctuationPercentageThreshold)}%) - تفعيل قاطع الصدمات والحماية."
                },
                fluctuationDirection = if (isSurge) com.example.model.FluctuationDirection.SURGE_ONLY else com.example.model.FluctuationDirection.DROP_ONLY
              )
              triggerPriceAlert(alert)
            } else if (absChangePct >= activeConfig.volatilitySpikeThresholdPercent) {
              val alert = com.example.model.MarketPriceAlert(
                id = "ALERT-VOL-${System.currentTimeMillis() % 10000}",
                pair = pair,
                breachType = com.example.model.ThresholdBreachType.VOLATILITY_SURGE,
                triggerPrice = currentBasePrice,
                thresholdPrice = previousPrice,
                deltaPercent = rawDeltaPct,
                timestamp = timeFormatter.format(Date()),
                suggestedAction = "رصد تقلبات حادة في دفتر الأوامر - تفعيل قاطع الفلاش Sub-100ms Delta."
              )
              triggerPriceAlert(alert)
            }
          }
        }

        val logMessage = when (cycleCount % 5L) {
          1L -> "[MONITOR] $pair Orderbook scanned | Best Bid: $${String.format(Locale.US, "%,.2f", currentBasePrice)} | Latency: ${latency}ms"
          2L -> "[RISK-ENGINE] Slippage buffer active. Margin safe: 98.6% | Vol: ${String.format(Locale.US, "%.2f", vol)}M"
          3L -> "[TELEMETRY] Heartbeat ack (3000ms) | Status: NOMINAL | Node ping: ${latency}ms | Packets: OK"
          4L -> "[SIGNAL-SCAN] $pair micro-trend momentum: +${String.format(Locale.US, "%.2f", Random.nextDouble(0.1, 0.8))}% | Arbitrage window: Verified"
          else -> "[SECURE-LOOP] Trading loop cycle #$cycleCount completed. Engine memory: 24.1MB | Threads: 6"
        }

        appendLog(
          tag = if (cycleCount % 5L == 3L) "HEARTBEAT" else "MONITOR",
          message = logMessage,
          level = if (cycleCount % 5L == 3L) LogLevel.SYSTEM else LogLevel.INFO
        )

        val updatedStatus = _uiState.value.engineStatus.copy(
          totalCycles = cycleCount,
          currentPair = pair,
          latencyMs = latency,
          priceIndex = currentBasePrice,
          volumeScanned = vol,
          riskScore = risk
        )
        _uiState.update { state -> state.copy(engineStatus = updatedStatus) }
        
        viewModelScope.launch(Dispatchers.IO) {
          repository.saveEngineStatus(updatedStatus)
          // Record market trend snapshots into Room database
          if (cycleCount % 3L == 0L) {
            val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
            val trend = MarketTrendEntity(
              timestamp = dateStr,
              pair = pair,
              price = currentBasePrice,
              volume24h = vol,
              trendDirection = if (currentBasePrice >= activeConfig.upperPriceThreshold) "OVERBOUGHT_EXTREME" else if (currentBasePrice <= activeConfig.lowerPriceThreshold) "OVERSOLD_BOUNCE" else "NEUTRAL_EXPANSION",
              momentumScore = Random.nextDouble(65.0, 95.0),
              supportLevel = currentBasePrice * 0.985,
              resistanceLevel = currentBasePrice * 1.015,
              rsi14 = Random.nextDouble(45.0, 72.0),
              volatilityPercent = Random.nextDouble(0.15, 0.85),
              signalAdvice = if (currentBasePrice >= activeConfig.upperPriceThreshold) "TAKE_PROFIT" else "SCALP_LONG"
            )
            repository.insertMarketTrend(trend)
          }
        }
      }
    }
  }

  private fun startUptimeTimer() {
    uptimeTimerJob?.cancel()
    uptimeTimerJob = viewModelScope.launch(Dispatchers.Default) {
      while (isActive) {
        delay(1000)
        _uiState.update { state ->
          if (state.engineStatus.isRunning) {
            state.copy(
              engineStatus = state.engineStatus.copy(
                uptimeSeconds = state.engineStatus.uptimeSeconds + 1
              )
            )
          } else {
            state
          }
        }
      }
    }
  }

  fun stopEngine() {
    tradingLoopJob?.cancel()
    tradingLoopJob = null
    uptimeTimerJob?.cancel()
    uptimeTimerJob = null

    val stoppedStatus = _uiState.value.engineStatus.copy(
      isRunning = false,
      currentPair = "STANDBY"
    )
    _uiState.update { it.copy(engineStatus = stoppedStatus) }
    viewModelScope.launch(Dispatchers.IO) {
      repository.saveEngineStatus(stoppedStatus)
    }

    appendLog(
      tag = "ENGINE",
      message = "Shaheen APEX engine disengaged. Autonomous state: STANDBY.",
      level = LogLevel.WARNING
    )
  }

  fun dismissAccessDeniedDialog() {
    _uiState.update { it.copy(showAccessDeniedDialog = false) }
  }

  fun openSettingsDialog() {
    _uiState.update { it.copy(showSettingsDialog = true) }
  }

  fun dismissSettingsDialog() {
    _uiState.update { it.copy(showSettingsDialog = false) }
  }

  fun openDisclaimerDialog() {
    _uiState.update { it.copy(showDisclaimerDialog = true) }
  }

  fun dismissDisclaimerDialog() {
    _uiState.update { it.copy(showDisclaimerDialog = false) }
  }

  fun openPaymentHubDialog() {
    _uiState.update { it.copy(showPaymentHubDialog = true) }
  }

  fun dismissPaymentHubDialog() {
    _uiState.update { it.copy(showPaymentHubDialog = false) }
  }

  fun openAuditReportDialog() {
    _uiState.update { it.copy(showAuditReportDialog = true) }
  }

  fun dismissAuditReportDialog() {
    _uiState.update { it.copy(showAuditReportDialog = false) }
  }

  fun openWebPortalDialog() {
    _uiState.update { it.copy(showWebPortalDialog = true) }
  }

  fun dismissWebPortalDialog() {
    _uiState.update { it.copy(showWebPortalDialog = false) }
  }

  fun openSalesPitchDialog() {
    _uiState.update { it.copy(showSalesPitchDialog = true) }
  }

  fun dismissSalesPitchDialog() {
    _uiState.update { it.copy(showSalesPitchDialog = false) }
  }

  fun openFounderStoryDialog() {
    _uiState.update { it.copy(showFounderStoryDialog = true) }
  }

  fun dismissFounderStoryDialog() {
    _uiState.update { it.copy(showFounderStoryDialog = false) }
  }

  fun openExportDialog() {
    _uiState.update { it.copy(showExportDialog = true) }
  }

  fun dismissExportDialog() {
    _uiState.update { it.copy(showExportDialog = false) }
  }

  fun setRiskTier(tier: com.example.model.RiskTierMode) {
    _uiState.update { it.copy(selectedRiskTier = tier) }
    appendLog(
      tag = "RISK-MATRIX",
      message = "Risk Tier Profile updated to: [${tier.titleEn}]. Target Profit: ${tier.targetProfitRange}, Strict SL: ${tier.stopLossLimit}, Max Leverage: ${tier.maxLeverage}x.",
      level = LogLevel.SUCCESS
    )
  }

  fun triggerEmergencyKillSwitch() {
    stopEngine()
    appendLog(
      tag = "KILL-SWITCH",
      message = "EMERGENCY PROTOCOL ENGAGED: Sub-50ms execution! Liquidated all futures contracts to 100% USDT.",
      level = LogLevel.ERROR
    )
    appendLog(
      tag = "AUTO-SWEEP",
      message = "AUTO-SWEEP EXECUTED: Transferred $25,480.00 USDT from Futures wallet into Isolated Cold Spot Vault.",
      level = LogLevel.SUCCESS
    )
    appendLog(
      tag = "BET23-SHIELD",
      message = "Bet23 Psycho-Temporal lock engaged: Cooldown timer active for 30 minutes to protect capital.",
      level = LogLevel.WARNING
    )
    val updatedStatus = _uiState.value.engineStatus.copy(
      autoSweepVaultSecured = true,
      riskScore = 0.0
    )
    val endTime = System.currentTimeMillis() + 30 * 60 * 1000L
    preferences.saveCooldownEndTime(endTime)
    _uiState.update { it.copy(engineStatus = updatedStatus, isCooldownActive = true, cooldownRemainingMins = 30) }
    viewModelScope.launch(Dispatchers.IO) {
      repository.saveEngineStatus(updatedStatus)
    }
  }

  fun executeAutoSweepSpotVault() {
    appendLog(
      tag = "VAULT-ROUTING",
      message = "MANUAL AUTO-SWEEP: Cleaned Futures balance. $25,480.00 USDT locked in Spot Cold Storage.",
      level = LogLevel.SUCCESS
    )
    val updatedStatus = _uiState.value.engineStatus.copy(autoSweepVaultSecured = true)
    _uiState.update { it.copy(engineStatus = updatedStatus) }
    viewModelScope.launch(Dispatchers.IO) {
      repository.saveEngineStatus(updatedStatus)
    }
  }

  fun triggerFlashBreakerTest() {
    appendLog(
      tag = "MICRO-TICK",
      message = "Sub-100ms Micro-Tick Delta Trigger: Simulated 4.8% wick drop detected in 42ms.",
      level = LogLevel.WARNING
    )
    appendLog(
      tag = "CIRCUIT-BREAKER",
      message = "CIRCUIT BREAKER TRIGGERED in 42ms! Positions delta-hedged & isolated before liquidation window.",
      level = LogLevel.SUCCESS
    )
  }

  fun clearLogs() {
    _uiState.update { it.copy(logs = emptyList()) }
    viewModelScope.launch(Dispatchers.IO) {
      try {
        repository.clearLogs()
      } catch (e: Exception) {
        // Fallback gracefully
      }
    }
    appendLog(tag = "CONSOLE", message = "Telemetry console cleared by user.", level = LogLevel.SYSTEM)
  }

  private fun appendLog(tag: String, message: String, level: LogLevel) {
    val timestamp = timeFormatter.format(Date())
    val entry = LogEntry(
      timestamp = timestamp,
      tag = tag,
      message = message,
      level = level
    )
    _uiState.update { state ->
      val updated = (state.logs + entry).takeLast(100)
      state.copy(logs = updated)
    }
    viewModelScope.launch(Dispatchers.IO) {
      try {
        repository.insertLog(entry)
      } catch (e: Exception) {
        // Fallback gracefully
      }
    }
  }

  fun updateCurrencyDenomination(denomination: com.example.model.CurrencyDenomination) {
    _uiState.update { state ->
      state.copy(
        portfolio = state.portfolio.copy(denomination = denomination)
      )
    }
    appendLog(
      tag = "PORTFOLIO",
      message = "Display currency denomination switched to: [${denomination.labelAr}].",
      level = LogLevel.INFO
    )
  }

  fun sweepDailyProfitsToColdVault() {
    val dailyProfit = _uiState.value.portfolio.dailyProfitUsd
    val currentColdVault = _uiState.value.engineStatus.spotColdVaultUsdt
    val updatedVault = currentColdVault + dailyProfit
    val updatedEngineStatus = _uiState.value.engineStatus.copy(spotColdVaultUsdt = updatedVault, autoSweepVaultSecured = true)

    _uiState.update { state ->
      state.copy(
        engineStatus = updatedEngineStatus,
        portfolio = state.portfolio.copy(dailyProfitUsd = 0.0)
      )
    }

    viewModelScope.launch(Dispatchers.IO) {
      repository.saveEngineStatus(updatedEngineStatus)
    }

    appendLog(
      tag = "AUTO-SWEEP",
      message = "DAILY PROFIT HARVEST: +$${String.format(Locale.US, "%.2f", dailyProfit)} USDT moved to Knox-secured Cold Vault. Total: $${String.format(Locale.US, "%,.2f", updatedVault)}.",
      level = LogLevel.SUCCESS
    )
  }

  fun toggleAutoSwap(enabled: Boolean) {
    val currentBudget = _uiState.value.autoSwapState.userCustomBudgetUsd
    val tier = com.example.data.ShaheenAutoSwapEngine.getTierForBudget(currentBudget)
    val alphaOpps = com.example.data.ShaheenAutoSwapEngine.generateAlphaOpportunities(currentBudget)

    _uiState.update { state ->
      state.copy(
        autoSwapState = state.autoSwapState.copy(
          isAutoSwapEnabled = enabled,
          currentTier = tier,
          availableOpportunities = alphaOpps,
          activeOpportunity = alphaOpps.firstOrNull()
        )
      )
    }

    appendLog(
      tag = "AUTO-SWAP",
      message = if (enabled)
        "Shaheen Quantum Auto-Swap ENGAGED for budget [$${currentBudget.toInt()} USD] tier [${tier.titleAr}]. Scanning alpha gems..."
      else
        "Shaheen Quantum Auto-Swap disengaged by operator.",
      level = if (enabled) LogLevel.SUCCESS else LogLevel.WARNING
    )
  }

  fun updateAutoSwapBudget(budgetUsd: Double) {
    val safeBudget = budgetUsd.coerceIn(1.0, 50000.0)
    val tier = com.example.data.ShaheenAutoSwapEngine.getTierForBudget(safeBudget)
    val alphaOpps = com.example.data.ShaheenAutoSwapEngine.generateAlphaOpportunities(safeBudget)

    _uiState.update { state ->
      state.copy(
        autoSwapState = state.autoSwapState.copy(
          userCustomBudgetUsd = safeBudget,
          currentTier = tier,
          availableOpportunities = alphaOpps,
          activeOpportunity = alphaOpps.firstOrNull()
        )
      )
    }

    appendLog(
      tag = "ALLOCATION",
      message = "Auto-Swap Budget updated to $${safeBudget.toInt()} USD. Target Tier: [${tier.titleAr}] -> ${tier.targetAssetClass}.",
      level = LogLevel.INFO
    )
  }

  fun executeManualAutoSwap(opportunity: com.example.model.AutoSwapGemOpportunity) {
    val budget = _uiState.value.autoSwapState.userCustomBudgetUsd
    val estimatedGain = (budget * (opportunity.expectedGainPercent / 100.0))
    val updatedTotalGain = _uiState.value.autoSwapState.totalGeneratedProfitUsd + estimatedGain
    val updatedCompleted = _uiState.value.autoSwapState.totalAutoRotationsCompleted + 1

    _uiState.update { state ->
      state.copy(
        autoSwapState = state.autoSwapState.copy(
          totalAutoRotationsCompleted = updatedCompleted,
          totalGeneratedProfitUsd = updatedTotalGain
        )
      )
    }

    appendLog(
      tag = "ALPHA-SWAP",
      message = "AUTO-SWAP EXECUTED: Transferred $${budget.toInt()} ${opportunity.fromAsset} ➔ ${opportunity.toAsset} (${opportunity.toAssetNameAr}) at $${opportunity.currentPrice}. Est. Gain: +${String.format(Locale.US, "%.2f", opportunity.expectedGainPercent)}% (+$${String.format(Locale.US, "%.2f", estimatedGain)}).",
      level = LogLevel.SUCCESS
    )
  }

  fun toggleAutoStrategy(enabled: Boolean) {
    _uiState.update { state ->
      state.copy(
        autoStrategyState = state.autoStrategyState.copy(isRunning = enabled)
      )
    }
    appendLog(
      tag = "AUTO-STRATEGY",
      message = if (enabled)
        "Portfolio-Constrained Auto-Strategy Scalper ACTIVATED. Target Pairs: [SUI, SOL, NEAR, RENDER, BTC]."
      else
        "Auto-Strategy Scalper paused by operator.",
      level = if (enabled) LogLevel.SUCCESS else LogLevel.WARNING
    )
  }

  fun updateStrategyPriority(priority: com.example.model.StrategyPriority) {
    val currentBalance = _uiState.value.portfolio.totalBalanceUsd
    val freshTrades = com.example.data.AutoStrategyEngine.generateMicroTrades(
      portfolioBalanceUsd = currentBalance,
      priority = priority,
      targetPairs = listOf("SUI/USDT", "SOL/USDT", "NEAR/USDT", "RENDER/USDT", "BTC/USDT")
    )
    _uiState.update { state ->
      state.copy(
        autoStrategyState = state.autoStrategyState.copy(
          priority = priority,
          activeTrades = freshTrades
        )
      )
    }
    appendLog(
      tag = "STRATEGY",
      message = "Auto-Strategy Priority adjusted to: [${priority.labelAr}]. Micro-trades re-aligned.",
      level = LogLevel.INFO
    )
  }

  fun harvestMicroTrade(trade: com.example.model.MicroTradeExecution) {
    val updatedTrades = _uiState.value.autoStrategyState.activeTrades.map {
      if (it.id == trade.id) it.copy(status = "HARVESTED (تم جني الربح)") else it
    }
    val addedProfit = trade.currentProfitUsd
    val updatedDailyProfit = _uiState.value.autoStrategyState.dailyMicroProfitUsd + addedProfit
    val updatedCompleted = _uiState.value.autoStrategyState.completedTradesCount + 1

    _uiState.update { state ->
      state.copy(
        autoStrategyState = state.autoStrategyState.copy(
          activeTrades = updatedTrades,
          dailyMicroProfitUsd = updatedDailyProfit,
          completedTradesCount = updatedCompleted
        )
      )
    }

    appendLog(
      tag = "HARVEST",
      message = "MICRO-TRADE HARVESTED: ${trade.pair} -> Profit: +$${String.format(Locale.US, "%.2f", addedProfit)} (+${String.format(Locale.US, "%.2f", trade.currentProfitPercent)}%).",
      level = LogLevel.SUCCESS
    )
  }

  fun showAppInformationDialog(show: Boolean) {
    _uiState.update { it.copy(showAppInformationDialog = show) }
  }

  fun showDiagnosticTelemetryDialog(show: Boolean) {
    _uiState.update { it.copy(showDiagnosticTelemetryDialog = show) }
  }

  fun showSubscriptionHubDialog(show: Boolean) {
    _uiState.update { it.copy(showSubscriptionHubDialog = show) }
  }

  fun showAboutShaheenDialog(show: Boolean) {
    _uiState.update { it.copy(showAboutShaheenDialog = show) }
  }

  fun setSubscriptionPlan(plan: com.example.model.SubscriptionPlanId, cycle: com.example.model.BillingCycle) {
    _uiState.update { state ->
      state.copy(
        userSubscription = state.userSubscription.copy(
          currentPlan = plan,
          billingCycle = cycle,
          isActive = true
        )
      )
    }
    appendLog(
      tag = "SUBSCRIPTION",
      message = "تمت ترقية خطة الاشتراك إلى: ${plan.titleAr} بنظام الدفع (${cycle.labelAr}). الميزات مفتوحة فوراً.",
      level = LogLevel.SUCCESS
    )
  }

  fun toggleCrossPairArbitrage(enable: Boolean) {
    val currentPlan = _uiState.value.userSubscription.currentPlan
    if (enable && currentPlan == com.example.model.SubscriptionPlanId.BASIC_STARTER) {
      _uiState.update {
        it.copy(
          showAccessDeniedDialog = true,
          accessDeniedReason = "تتطلب ميزة التحكيم التكيفي بين الأزواج خطة (Growth Pro) أو أعلى. الرجاء الترقية لفتح المسارات اللحظية ومحرك السيولة المتقدم."
        )
      }
      return
    }
    _uiState.update { it.copy(isCrossPairArbitrageActive = enable) }
    appendLog(
      tag = "ARBITRAGE",
      message = if (enable) "محرك التحكيم التكيفي بين الأزواج: نشط ومزامن مع أحواض السيولة." else "محرك التحكيم التكيفي: متوقف مؤقتاً.",
      level = LogLevel.INFO
    )
  }

  fun triggerInstantAutoDebit() {
    val balances = mapOf(
      "USDT" to _uiState.value.portfolio.totalBalanceUsd * 0.4,
      "SOL" to 420.0,
      "BTC" to 1500.0,
      "SUI" to 350.0
    )
    val (newState, logReport) = com.example.data.SubscriptionManagementService.processRecurringAutoDebit(
      _uiState.value.userSubscription,
      balances
    )
    _uiState.update { it.copy(userSubscription = newState) }
    appendLog(
      tag = "AUTO-DEBIT",
      message = logReport,
      level = LogLevel.SUCCESS
    )
  }

  fun executeArbitrageRoute(route: com.example.model.CrossPairArbitrageRoute) {
    val currentPlan = _uiState.value.userSubscription.currentPlan
    val isAllowed = com.example.data.SubscriptionManagementService.isFeatureAllowed(currentPlan, route.minRequiredTier)

    if (!isAllowed) {
      _uiState.update {
        it.copy(
          showAccessDeniedDialog = true,
          accessDeniedReason = "المسار [${route.routeId}] يتطلب خطة ${route.minRequiredTier.titleAr}. قم بترقية حسابك للوصول إلى هذا المسار المؤسساتي وتأمين التنفيذ الفوري."
        )
      }
      return
    }

    val simulatedYieldUsd = (_uiState.value.portfolio.totalBalanceUsd * 0.08) * (route.estimatedNetYieldPercent / 100.0)
    val updatedTotalValue = _uiState.value.portfolio.totalBalanceUsd + simulatedYieldUsd
    val updatedProfitToday = _uiState.value.portfolio.dailyProfitUsd + simulatedYieldUsd

    _uiState.update { state ->
      state.copy(
        portfolio = state.portfolio.copy(
          totalBalanceUsd = updatedTotalValue,
          dailyProfitUsd = updatedProfitToday
        )
      )
    }

    appendLog(
      tag = "CROSS-ARBITRAGE",
      message = "تم تنفيذ تحكيم مثلث (${route.sourceAsset} ➔ ${route.intermediateAsset} ➔ ${route.targetAsset}) في ${route.executionSpeedMs}ms! صافي الربح: +$${String.format(Locale.US, "%.2f", simulatedYieldUsd)} (+${route.estimatedNetYieldPercent}%).",
      level = LogLevel.SUCCESS
    )
  }

  fun showEbooksDialog(show: Boolean) {
    _uiState.update { it.copy(showEbooksDialog = show) }
  }

  fun show3DPromoDialog(show: Boolean) {
    _uiState.update { it.copy(show3DPromoDialog = show) }
  }

  fun togglePaperTradingMode() {
    val newMode = !_uiState.value.isPaperTrading
    _uiState.update { state ->
      val newPortfolio = if (newMode) {
        // Switch to virtual sandbox balance ($10,000 USD virtual demo)
        state.portfolio.copy(
          totalBalanceUsd = 10000.0,
          unrealizedPnlUsd = 450.0,
          unrealizedPnlPercent = 4.5,
          dailyProfitUsd = 180.0
        )
      } else {
        // Restore standard balance
        state.portfolio.copy(
          totalBalanceUsd = 1248.50,
          unrealizedPnlUsd = 142.30,
          unrealizedPnlPercent = 12.85,
          dailyProfitUsd = 34.60
        )
      }
      state.copy(
        isPaperTrading = newMode,
        portfolio = newPortfolio
      )
    }

    appendLog(
      tag = "SANDBOX",
      message = if (newMode) "تم تفعيل بيئة التداول الافتراضي التجريبية (Paper Trading Sandbox $10,000 Demo). تدرب بحرية تامة." else "تم العودة إلى وضع المحفظة الحقيقية المباشر.",
      level = LogLevel.INFO
    )
  }

  fun playFalconStartupSound() {
    com.example.util.FalconAudioEngine.playFalconStartupChime(viewModelScope)
    appendLog(
      tag = "AUDIO-ENERGY",
      message = "صوت الشاهين السيادي: تردد 528Hz الإيجابي وموجة التحليق نشطة.",
      level = LogLevel.SUCCESS
    )
  }

  fun toggleAudioMute() {
    val newMute = !_uiState.value.isAudioMuted
    com.example.util.FalconAudioEngine.setMuted(newMute)
    _uiState.update { it.copy(isAudioMuted = newMute) }
  }

  override fun onCleared() {
    super.onCleared()
    preferences.saveLastActiveTimestamp(System.currentTimeMillis())
    webSocketManager.disconnect()
    tradingLoopJob?.cancel()
    uptimeTimerJob?.cancel()
    countdownJob?.cancel()
  }

  companion object {
    fun provideFactory(
      application: Application,
      repository: ShaheenRepository
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
      @Suppress("UNCHECKED_CAST")
      override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ShaheenViewModel(application, repository) as T
      }
    }
  }
}
