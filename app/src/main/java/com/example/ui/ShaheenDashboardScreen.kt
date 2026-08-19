package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.AccessDeniedDialog
import com.example.ui.components.AppInformationDialog
import com.example.ui.components.AuditReportDialog
import com.example.ui.components.AutoStrategySection
import com.example.ui.components.AutoSwapOpportunitySection
import com.example.ui.components.DiagnosticTelemetryDialog
import com.example.ui.components.EngineStatusHealthPanel
import com.example.ui.components.FastSalesPitchDialog
import com.example.ui.components.FluctuationAlertConfigDialog
import com.example.ui.components.LegalDisclaimerDialog
import com.example.ui.components.LiveOrderBookComponent
import com.example.ui.components.MarketFluctuationBanner
import com.example.ui.components.MetricsGrid
import com.example.ui.components.MomentumPredictionBanner
import com.example.ui.components.NetworkLatencyOverlay
import com.example.ui.components.PaymentHubDialog
import com.example.ui.components.PortfolioWidget
import com.example.ui.components.SettingsDialog
import com.example.ui.components.ShaheenGlobalInnovationHub
import com.example.ui.components.TelemetryConsole
import com.example.ui.components.VpnToggleButton
import com.example.ui.components.WebPortalBlueprintDialog
import com.example.ui.theme.*
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenMetallicBorderLight
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.ShaheenSurfaceDark
import com.example.ui.theme.ShaheenSurfaceElevated
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.util.Locale

@Composable
fun ShaheenDashboardScreen(
  viewModel: ShaheenViewModel
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val cachedEngineStatus by viewModel.cachedEngineStatus.collectAsStateWithLifecycle()
  val isAyman = uiState.config.username.trim().equals("ayman", ignoreCase = true)

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(ShaheenBackground),
    containerColor = ShaheenBackground,
    contentWindowInsets = WindowInsets.safeDrawing
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              ShaheenSurfaceDark,
              ShaheenBackground,
              ShaheenBackground
            )
          )
        )
    ) {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 28.dp)
      ) {
        // --- BET23 Psycho-Temporal Cooldown Lock ---
        if (uiState.isCooldownActive) {
          item {
            Bet23CooldownBanner(remainingMins = uiState.cooldownRemainingMins)
            Spacer(modifier = Modifier.height(16.dp))
          }
        }

        // 1. Top Tactical Bar with Official Falcon Logo
        item {
          TopTacticalBar(
            username = uiState.config.username,
            isAyman = isAyman,
            isRunning = uiState.engineStatus.isRunning,
            alertsEnabled = uiState.config.priceAlertsEnabled,
            onOpenSalesPitch = { viewModel.openSalesPitchDialog() },
            onOpenFounderStory = { viewModel.openFounderStoryDialog() },
            onOpenFluctuationConfig = { viewModel.openFluctuationAlertConfigDialog() },
            onOpenAppInfo = { viewModel.showAppInformationDialog(true) },
            onOpenSubscriptionHub = { viewModel.showSubscriptionHubDialog(true) },
            onOpenAboutShaheen = { viewModel.showAboutShaheenDialog(true) },
            onOpenSettings = { viewModel.openSettingsDialog() },
            onOpenAudit = { viewModel.openAuditReportDialog() }
          )
        }

        // 1.2 Real-Time Network Latency Dashboard Overlay (WebSocket Ping & Packet Delivery Monitor)
        item {
          Spacer(modifier = Modifier.height(4.dp))
          NetworkLatencyOverlay(
            wsStatus = uiState.wsStatus,
            metrics = uiState.latencyMetrics,
            onOpenTelemetryDialog = { viewModel.showDiagnosticTelemetryDialog(true) },
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 1.5 Real-Time Price Threshold & Fluctuation Breach Alert Banner
        item {
          MarketFluctuationBanner(
            alert = uiState.activePriceAlert,
            onDismiss = { viewModel.dismissActivePriceAlert() },
            onOpenSettings = { viewModel.openFluctuationAlertConfigDialog() }
          )
        }

        // 1.8 Intelligent Momentum-Shift Price Prediction Heuristics
        item {
          uiState.momentumPrediction?.let { prediction ->
            Spacer(modifier = Modifier.height(6.dp))
            MomentumPredictionBanner(
              prediction = prediction,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }

        // 2. Beta Testing Countdown & Commercial Readiness Ribbon
        item {
          TestingCountdownBanner(
            remainingSeconds = uiState.engineStatus.testRemainingSeconds,
            onOpenAudit = { viewModel.openAuditReportDialog() }
          )
        }

        // 2.2 Master Books & Positive Falcon Audio Energy Bar
        item {
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            // 3D Promo Cinema Showcase Button
            Button(
              onClick = { viewModel.show3DPromoDialog(true) },
              modifier = Modifier
                .weight(1.3f)
                .height(42.dp)
                .testTag("open_3d_promo_cinema_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF261908),
                contentColor = FalconGold
              ),
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(1.2.dp, FalconGold),
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Diamond,
                  contentDescription = null,
                  modifier = Modifier.size(15.dp),
                  tint = FalconGold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "💎 سينما 3D والامتياز",
                  fontWeight = FontWeight.Black,
                  fontSize = 10.5.sp
                )
              }
            }

            // E-Books Button
            Button(
              onClick = { viewModel.showEbooksDialog(true) },
              modifier = Modifier
                .weight(1.1f)
                .height(42.dp)
                .testTag("open_master_ebooks_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF132230),
                contentColor = FalconCyan
              ),
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, FalconCyan.copy(alpha = 0.6f)),
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.MenuBook,
                  contentDescription = null,
                  modifier = Modifier.size(15.dp),
                  tint = FalconCyan
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "📚 الكتابان",
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                )
              }
            }

            // Positive Falcon Audio Screech Chime
            Button(
              onClick = { viewModel.playFalconStartupSound() },
              modifier = Modifier
                .weight(0.9f)
                .height(42.dp)
                .testTag("play_falcon_energy_sound_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E293B),
                contentColor = FalconGold
              ),
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, FalconGold.copy(alpha = 0.4f)),
              contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = null,
                  modifier = Modifier.size(15.dp),
                  tint = FalconGold
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "🦅 528Hz",
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.5.sp
                )
              }
            }

            // Sandbox Mode Toggle Button
            Button(
              onClick = { viewModel.togglePaperTradingMode() },
              modifier = Modifier
                .weight(1.0f)
                .height(42.dp)
                .testTag("toggle_paper_trading_sandbox_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.isPaperTrading) ActiveEmerald.copy(alpha = 0.25f) else Color(0xFF1E293B),
                contentColor = if (uiState.isPaperTrading) ActiveEmerald else TextMuted
              ),
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (uiState.isPaperTrading) ActiveEmerald else ShaheenMetallicBorder
              ),
              contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Science,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp),
                  tint = if (uiState.isPaperTrading) ActiveEmerald else TextMuted
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = if (uiState.isPaperTrading) "🧪 نشط" else "🧪 تجريبي",
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.5.sp
                )
              }
            }
          }
        }

        // 2.9 Security Banner (Free)
        item {
          Spacer(modifier = Modifier.height(4.dp))
          SecurityZeroDollarBanner()
        }

        // 3. Identity Status Chip & Operator Binding
        item {
          Spacer(modifier = Modifier.height(6.dp))
          IdentityStatusBanner(
            username = uiState.config.username,
            isAyman = isAyman,
            hasAcceptedDisclaimer = uiState.config.hasAcceptedDisclaimer,
            onConfigure = { viewModel.openSettingsDialog() },
            onOpenDisclaimer = { viewModel.openDisclaimerDialog() }
          )
        }

        // 3.1 Cloud Resilience Offline Panel
        item {
          CloudResiliencePanel()
        }

        // 3.5. Live Asset Portfolio & Denomination Tracker
        item {
          Spacer(modifier = Modifier.height(6.dp))
          PortfolioWidget(
            portfolio = uiState.portfolio,
            onDenominationChange = { viewModel.updateCurrencyDenomination(it) },
            onSweepToVault = { viewModel.sweepDailyProfitsToColdVault() },
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 3.5.5 Real-Time Price History Chart
        item {
          Spacer(modifier = Modifier.height(10.dp))
          com.example.ui.components.PriceHistoryChartComponent(
            assetName = "BTC/USDT",
            priceDataPoints = uiState.btcPriceHistory,
            currentPrice = uiState.btcPriceHistory.lastOrNull() ?: 0.0,
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 3.6. Auto-Strategy Engine (Constrained by Portfolio Balance & Micro-Trade Scalper)
        item {
          Spacer(modifier = Modifier.height(10.dp))
          AutoStrategySection(
            strategyState = uiState.autoStrategyState,
            onToggleStrategy = { viewModel.toggleAutoStrategy(it) },
            onPriorityChanged = { viewModel.updateStrategyPriority(it) },
            onHarvestTrade = { viewModel.harvestMicroTrade(it) },
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 3.8. Quantum Auto-Swap Engine (Alpha Opportunity Scanner & Budget Tiered Rotation)
        item {
          Spacer(modifier = Modifier.height(10.dp))
          AutoSwapOpportunitySection(
            autoSwapState = uiState.autoSwapState,
            onToggleAutoSwap = { viewModel.toggleAutoSwap(it) },
            onBudgetChange = { viewModel.updateAutoSwapBudget(it) },
            onExecuteManualSwap = { viewModel.executeManualAutoSwap(it) },
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 3.9. Adaptive Cross-Pair Triangular Arbitrage Engine (Premium Tier Enforced)
        item {
          Spacer(modifier = Modifier.height(10.dp))
          com.example.ui.components.AdaptiveCrossPairArbitrageSection(
            userPlan = uiState.userSubscription.currentPlan,
            isArbitrageEnabled = uiState.isCrossPairArbitrageActive,
            onToggleArbitrage = { viewModel.toggleCrossPairArbitrage(it) },
            onOpenUpgradeDialog = { viewModel.showSubscriptionHubDialog(true) },
            onExecuteRoute = { viewModel.executeArbitrageRoute(it) },
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 4. Central VPN Toggle Button
        item {
          Spacer(modifier = Modifier.height(8.dp))
          VpnToggleButton(
            isRunning = uiState.engineStatus.isRunning,
            onToggle = { viewModel.toggleVpnEngine() },
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 4.5. Room Database Engine Status & Operational Health Panel
        item {
          Spacer(modifier = Modifier.height(4.dp))
          EngineStatusHealthPanel(
            statusEntity = cachedEngineStatus,
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 4.8. Live Millisecond WebSocket Order Book & Market Depth
        item {
          Spacer(modifier = Modifier.height(10.dp))
          LiveOrderBookComponent(
            orderBook = uiState.orderBook,
            wsStatus = uiState.wsStatus,
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 5. Commercial Licensing & Crypto Payment Hub Card
        item {
          CommercialLicensingBanner(
            onOpenPaymentHub = { viewModel.openPaymentHubDialog() }
          )
        }

        // 6. Metrics Grid
        item {
          Spacer(modifier = Modifier.height(10.dp))
          MetricsGrid(
            engineStatus = uiState.engineStatus,
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 6.5. Premium Micro-Scalping, Cross-Exchange Rotation & AI Whale Radar Feed
        item {
          Spacer(modifier = Modifier.height(10.dp))
          com.example.ui.components.PremiumIntelligenceSection(
            trades = uiState.microScalpTrades,
            rotationSignal = uiState.activeRotationSignal,
            alerts = uiState.intelligenceAlerts,
            isScalpActive = uiState.isMicroScalpingActive,
            onToggleScalp = {
              // toggle micro-scalping engine state
            },
            onOpenUpgradeHub = { viewModel.openPaymentHubDialog() },
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 7. Global Innovation Hub (Bet23 Behavioral Lock, Pre-Emptive Shield & Ecosystem Roadmap)
        item {
          Spacer(modifier = Modifier.height(10.dp))
          ShaheenGlobalInnovationHub(
            engineStatus = uiState.engineStatus,
            preEmptiveActive = uiState.config.preEmptiveShieldActive,
            bet23LockActive = uiState.config.bet23BehavioralLock,
            onTriggerEmergencyKillSwitch = { viewModel.triggerEmergencyKillSwitch() },
            onOpenPortalWeb = { viewModel.openWebPortalDialog() },
            modifier = Modifier.fillMaxWidth()
          )
        }

        // 8. Live Telemetry Console
        item {
          Spacer(modifier = Modifier.height(12.dp))
          TelemetryConsole(
            logs = uiState.logs,
            isRunning = uiState.engineStatus.isRunning,
            onClearLogs = { viewModel.clearLogs() },
            onOpenExport = { viewModel.openExportDialog() }
          )
        }

        // 9. Legal Disclaimer Footer Link & Attribution
        item {
          Spacer(modifier = Modifier.height(10.dp))
          LegalFooterBar(
            hasAccepted = uiState.config.hasAcceptedDisclaimer,
            onOpenDisclaimer = { viewModel.openDisclaimerDialog() }
          )
        }
      }

      // Auto-save Floating Toast
      AnimatedVisibility(
        visible = uiState.autoSaveToastVisible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 16.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = ShaheenSurfaceCard,
          border = androidx.compose.foundation.BorderStroke(1.dp, FalconBlue),
          shadowElevation = 8.dp
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = ActiveEmerald,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "تم الحفظ التلقائي في الخزينة المحلية المشفرة",
              style = MaterialTheme.typography.labelSmall,
              color = TextWhite
            )
          }
        }
      }
    }
  }

  // Legal Disclaimer Dialog
  if (uiState.showDisclaimerDialog) {
    LegalDisclaimerDialog(
      isAccepted = uiState.config.hasAcceptedDisclaimer,
      onAccept = {
        viewModel.acceptDisclaimer()
      },
      onDismiss = { viewModel.dismissDisclaimerDialog() }
    )
  }

  // Payment & Multi-User Hub Dialog
  if (uiState.showPaymentHubDialog) {
    PaymentHubDialog(
      extraUsersCount = uiState.config.additionalUsersCount,
      onExtraUsersChange = { viewModel.updateAdditionalUsersCount(it) },
      onDismiss = { viewModel.dismissPaymentHubDialog() }
    )
  }

  // Audit Report Dialog
  if (uiState.showAuditReportDialog) {
    AuditReportDialog(
      config = uiState.config,
      engineStatus = uiState.engineStatus,
      onDismiss = { viewModel.dismissAuditReportDialog() }
    )
  }

  // Access Denied Dialog
  if (uiState.showAccessDeniedDialog) {
    AccessDeniedDialog(
      currentUsername = uiState.config.username,
      reason = uiState.accessDeniedReason,
      onDismiss = { viewModel.dismissAccessDeniedDialog() },
      onQuickFixToAyman = {
        viewModel.updateUsername("ayman")
      }
    )
  }

  // Web Portal Blueprint Dialog
  if (uiState.showWebPortalDialog) {
    WebPortalBlueprintDialog(
      onDismiss = { viewModel.dismissWebPortalDialog() }
    )
  }

  // Fast Sales Pitch & Direct Crypto Licensing Dialog
  if (uiState.showSalesPitchDialog) {
    FastSalesPitchDialog(
      onDismiss = { viewModel.dismissSalesPitchDialog() }
    )
  }

  // Founder Story, Philosophy & Ecosystem Vision Dialog
  if (uiState.showFounderStoryDialog) {
    com.example.ui.components.FounderStoryDialog(
      onDismiss = { viewModel.dismissFounderStoryDialog() },
      onOpenPaymentHub = { viewModel.openPaymentHubDialog() }
    )
  }

  // Settings Dialog
  if (uiState.showSettingsDialog) {
    SettingsDialog(
      config = uiState.config,
      onUsernameChange = { viewModel.updateUsername(it) },
      onLicenseKeyChange = { viewModel.updateLicenseKey(it) },
      onApiKeyChange = { viewModel.updateApiKey(it) },
      onPriceAlertsEnabledChange = { viewModel.updatePriceAlertsEnabled(it) },
      onUpperThresholdChange = { viewModel.updateUpperPriceThreshold(it) },
      onLowerThresholdChange = { viewModel.updateLowerPriceThreshold(it) },
      onVolatilityThresholdChange = { viewModel.updateVolatilityThreshold(it) },
      onFluctuationPercentageThresholdChange = { viewModel.updateFluctuationPercentageThreshold(it) },
      onFluctuationDirectionChange = { viewModel.updateFluctuationDirection(it) },
      onOpenFluctuationAlertConfig = { viewModel.openFluctuationAlertConfigDialog() },
      onTriggerTestAlert = { viewModel.triggerTestPriceThresholdAlert() },
      onOpenExport = { viewModel.openExportDialog() },
      onDismiss = { viewModel.dismissSettingsDialog() },
      autoSaveActive = uiState.autoSaveToastVisible
    )
  }

  // User-Configurable Fluctuation Alert System Suite Dialog
  if (uiState.showFluctuationAlertConfigDialog) {
    FluctuationAlertConfigDialog(
      config = uiState.config,
      currentPrice = uiState.engineStatus.priceIndex,
      recentAlerts = uiState.recentPriceAlerts,
      onPriceAlertsToggle = { viewModel.updatePriceAlertsEnabled(it) },
      onFluctuationThresholdChange = { viewModel.updateFluctuationPercentageThreshold(it) },
      onDirectionChange = { viewModel.updateFluctuationDirection(it) },
      onTimeWindowChange = { viewModel.updateFluctuationTimeWindow(it) },
      onSystemNotificationsToggle = { viewModel.updateSystemNotificationsEnabled(it) },
      onAlertSoundToggle = { viewModel.updateFluctuationAlertSoundEnabled(it) },
      onTriggerTestAlert = { viewModel.triggerTestPriceThresholdAlert() },
      onDismiss = { viewModel.dismissFluctuationAlertConfigDialog() },
      autoSaveActive = uiState.autoSaveToastVisible
    )
  }

  // App Specification & 3-Tier Premium Information Dialog
  if (uiState.showAppInformationDialog) {
    AppInformationDialog(
      onSelectPlanToPay = { plan ->
        viewModel.showAppInformationDialog(false)
        viewModel.showSubscriptionHubDialog(true)
      },
      onDismiss = { viewModel.showAppInformationDialog(false) }
    )
  }

  // Shaheen Subscription & VIP Sovereign Whale Hub Dialog
  if (uiState.showSubscriptionHubDialog) {
    com.example.ui.components.SubscriptionHubDialog(
      subscriptionState = uiState.userSubscription,
      onSelectPlan = { plan, cycle ->
        viewModel.setSubscriptionPlan(plan, cycle)
        viewModel.showSubscriptionHubDialog(false)
      },
      onTriggerAutoDebit = { viewModel.triggerInstantAutoDebit() },
      onToggleAutoRenew = { /* toggled */ },
      onDismiss = { viewModel.showSubscriptionHubDialog(false) }
    )
  }

  // About SHAHEEN Architecture & Proposition Dialog
  if (uiState.showAboutShaheenDialog) {
    com.example.ui.components.AboutShaheenDialog(
      onOpenSubscriptionHub = { viewModel.showSubscriptionHubDialog(true) },
      onDismiss = { viewModel.showAboutShaheenDialog(false) }
    )
  }

  // SHAHEEN Master E-Books & Global Standards Dialog
  if (uiState.showEbooksDialog) {
    com.example.ui.components.ShaheenEbooksDialog(
      onDismiss = { viewModel.showEbooksDialog(false) },
      onOpenSubscriptionHub = {
        viewModel.showEbooksDialog(false)
        viewModel.showSubscriptionHubDialog(true)
      }
    )
  }

  // SHAHEEN 3D Promo Cinema & Sovereign Genesis Showcase Dialog
  if (uiState.show3DPromoDialog) {
    com.example.ui.components.Shaheen3DPromoCinemaDialog(
      onDismiss = { viewModel.show3DPromoDialog(false) },
      onOpenPricingHub = {
        viewModel.show3DPromoDialog(false)
        viewModel.showSubscriptionHubDialog(true)
      }
    )
  }

  // Diagnostic Performance Report & HFT Telemetry Dialog
  if (uiState.showDiagnosticTelemetryDialog) {
    DiagnosticTelemetryDialog(
      report = uiState.diagnosticReport,
      onDismiss = { viewModel.showDiagnosticTelemetryDialog(false) }
    )
  }

  // Room Database Data Export Dialog (CSV / JSON)
  if (uiState.showExportDialog) {
    com.example.ui.components.DataExportDialog(
      logs = uiState.logs,
      marketTrends = uiState.marketTrends,
      engineStatus = uiState.engineStatus,
      config = uiState.config,
      onDismiss = { viewModel.dismissExportDialog() }
    )
  }
}

@Composable
private fun TopTacticalBar(
  username: String,
  isAyman: Boolean,
  isRunning: Boolean,
  alertsEnabled: Boolean = true,
  onOpenSalesPitch: () -> Unit,
  onOpenFounderStory: () -> Unit,
  onOpenFluctuationConfig: () -> Unit,
  onOpenAppInfo: () -> Unit,
  onOpenSubscriptionHub: () -> Unit,
  onOpenAboutShaheen: () -> Unit,
  onOpenSettings: () -> Unit,
  onOpenAudit: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Official Falcon Logo & Title
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.clickable { onOpenFounderStory() }
    ) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(ShaheenSurfaceElevated)
          .border(1.5.dp, FalconCyan.copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.shaheen_logo),
          contentDescription = "SHAHEEN APEX AI Falcon",
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp)),
          contentScale = ContentScale.Crop
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "SHAHEEN",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              letterSpacing = 2.sp
            ),
            color = TextWhite
          )
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = FalconBlue.copy(alpha = 0.25f),
            border = androidx.compose.foundation.BorderStroke(1.dp, FalconCyan)
          ) {
            Text(
              text = "APEX AI",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              ),
              color = FalconCyan,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
            )
          }
        }

        Text(
          text = "Autonomous Intelligence • Sovereign Invention",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
          color = TextMuted
        )
      }
    }

    // Header Actions
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
      // Subscription & VIP Hub Button
      IconButton(
        onClick = onOpenSubscriptionHub,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              listOf(Color(0xFFFFD700).copy(alpha = 0.3f), Color(0xFFFF3366).copy(alpha = 0.3f))
            )
          )
          .border(1.dp, Color(0xFFFFD700), CircleShape)
          .testTag("open_subscription_hub_top_button")
      ) {
        Icon(
          imageVector = Icons.Default.Diamond,
          contentDescription = "Shaheen Subscription & VIP Whale Hub",
          tint = Color(0xFFFFD700),
          modifier = Modifier.size(20.dp)
        )
      }

      // About SHAHEEN Architecture Button
      IconButton(
        onClick = onOpenAboutShaheen,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(FalconBlue.copy(alpha = 0.25f))
          .border(1.dp, FalconCyan, CircleShape)
          .testTag("open_about_shaheen_top_button")
      ) {
        Icon(
          imageVector = Icons.Default.Memory,
          contentDescription = "About Shaheen HFT Architecture",
          tint = FalconCyan,
          modifier = Modifier.size(20.dp)
        )
      }

      IconButton(
        onClick = onOpenAppInfo,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(FalconBlue.copy(alpha = 0.25f))
          .border(1.dp, FalconCyan, CircleShape)
          .testTag("open_app_info_guide_button")
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = "Shaheen Specification & Pricing Guide",
          tint = FalconCyan,
          modifier = Modifier.size(20.dp)
        )
      }

      IconButton(
        onClick = onOpenFluctuationConfig,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(if (alertsEnabled) FalconBlue.copy(alpha = 0.25f) else ShaheenSurfaceCard)
          .border(1.dp, if (alertsEnabled) FalconCyan else ShaheenMetallicBorder, CircleShape)
          .testTag("open_fluctuation_alert_suite_button")
      ) {
        Icon(
          imageVector = Icons.Default.NotificationsActive,
          contentDescription = "Price Fluctuation Alerts",
          tint = if (alertsEnabled) FalconCyan else TextMuted,
          modifier = Modifier.size(20.dp)
        )
      }

      IconButton(
        onClick = onOpenSalesPitch,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(ActiveEmerald.copy(alpha = 0.2f))
          .border(1.dp, ActiveEmerald, CircleShape)
          .testTag("open_sales_pitch_button")
      ) {
        Icon(
          imageVector = Icons.Default.CurrencyExchange,
          contentDescription = "Fast Sales Pitch",
          tint = ActiveEmerald,
          modifier = Modifier.size(20.dp)
        )
      }

      IconButton(
        onClick = onOpenAudit,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(ShaheenSurfaceCard)
          .border(1.dp, ShaheenMetallicBorder, CircleShape)
          .testTag("open_audit_top_button")
      ) {
        Icon(
          imageVector = Icons.Default.Assessment,
          contentDescription = "Audit Report",
          tint = FalconCyan,
          modifier = Modifier.size(20.dp)
        )
      }

      IconButton(
        onClick = onOpenSettings,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(ShaheenSurfaceCard)
          .border(1.dp, ShaheenMetallicBorder, CircleShape)
          .testTag("settings_button")
      ) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = "Settings",
          tint = FalconBlue,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
private fun TestingCountdownBanner(
  remainingSeconds: Long,
  onOpenAudit: () -> Unit
) {
  val formatted = formatCountdownShort(remainingSeconds)

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = ShaheenSurfaceDark,
    border = androidx.compose.foundation.BorderStroke(1.dp, FalconBlue.copy(alpha = 0.4f)),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp)
      .clickable(onClick = onOpenAudit)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.HourglassTop,
          contentDescription = null,
          tint = ConsoleYellow,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "انتهاء مرحلة الاختبار التجريبي:",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = TextWhite
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = formatted,
          style = MaterialTheme.typography.labelSmall.copy(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black
          ),
          color = FalconCyan
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "تقرير الفحص ↗",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = FalconBlue
        )
      }
    }
  }
}

@Composable
private fun IdentityStatusBanner(
  username: String,
  isAyman: Boolean,
  hasAcceptedDisclaimer: Boolean,
  onConfigure: () -> Unit,
  onOpenDisclaimer: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = ShaheenSurfaceCard,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isAyman) ShaheenMetallicBorder else InactiveCrimson.copy(alpha = 0.5f)
    ),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.clickable(onClick = onConfigure),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = if (isAyman) Icons.Default.Fingerprint else Icons.Default.Lock,
          contentDescription = null,
          tint = if (isAyman) ActiveEmerald else InactiveCrimson,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "المشغل المرخص: ${if (username.isBlank()) "[غير محدد]" else username}",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = TextWhite
          )
          Text(
            text = if (isAyman) "مرخص للمطور أيمن العرايشي" else "مقفل أمنياً ومخصص لمستخدم واحد فقط",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = if (isAyman) ActiveEmerald else InactiveCrimson
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(6.dp),
        color = (if (hasAcceptedDisclaimer) ActiveEmerald else FalconBlue).copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          (if (hasAcceptedDisclaimer) ActiveEmerald else FalconBlue).copy(alpha = 0.4f)
        ),
        modifier = Modifier.clickable(onClick = onOpenDisclaimer)
      ) {
        Text(
          text = if (hasAcceptedDisclaimer) "إخلاء المسؤولية ✔" else "مطلوب الإقرار ⚠",
          style = MaterialTheme.typography.labelSmall.copy(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp
          ),
          color = if (hasAcceptedDisclaimer) ActiveEmerald else FalconCyan,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
      }
    }
  }
}

@Composable
private fun CommercialLicensingBanner(
  onOpenPaymentHub: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = ShaheenSurfaceCard,
    border = androidx.compose.foundation.BorderStroke(1.dp, FalconBlue.copy(alpha = 0.5f)),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clickable(onClick = onOpenPaymentHub)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(FalconBlue.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CurrencyExchange,
            contentDescription = null,
            tint = FalconCyan,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "الترخيص الأساسي: 100$ | سنوي: 25$",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
            color = TextWhite
          )
          Text(
            text = "3 باقات بريميوم • تداول آلي واقتناص الفرص",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = FalconCyan
          )
        }
      }

      Button(
        onClick = onOpenPaymentHub,
        colors = ButtonDefaults.buttonColors(containerColor = FalconBlue),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        modifier = Modifier.height(32.dp)
      ) {
        Text(text = "ترقية / دفع", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
      }
    }
  }
}

@Composable
private fun SecurityZeroDollarBanner() {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = ActiveEmerald.copy(alpha = 0.1f),
    border = androidx.compose.foundation.BorderStroke(1.dp, ActiveEmerald.copy(alpha = 0.5f)),
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(Icons.Default.Security, contentDescription = null, tint = ActiveEmerald, modifier = Modifier.size(24.dp))
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text("الأمان غير مدفوع!", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black), color = ActiveEmerald)
        Text("حماية أموالك وبياناتك قيمتها ثابتة مدى الحياة: 0 دولار.", style = MaterialTheme.typography.labelSmall, color = TextWhite)
      }
    }
  }
}

@Composable
private fun CloudResiliencePanel() {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = ShaheenSurfaceCard,
    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text("حماية انقطاع الاتصال (Cloud Fallback)", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = NeonCyan)
        Text("يستمر عمل الخوارزميات ووقف الخسارة حتى وإن انقطع الإنترنت أو أغلق الهاتف.", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = TextMuted)
      }
    }
  }
}

@Composable
private fun LegalFooterBar(
  hasAccepted: Boolean,
  onOpenDisclaimer: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.clickable(onClick = onOpenDisclaimer)
    ) {
      Icon(
        imageVector = Icons.Default.Gavel,
        contentDescription = null,
        tint = FalconCyan,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "عرض بنود إخلاء المسؤولية وإقرار الاستخدام القانوني الكامل",
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        ),
        color = FalconCyan
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      text = "SHAHEEN APEX AI © • المطور: أيمن العرايشي (Ayman Al-Araishi)",
      style = MaterialTheme.typography.labelSmall.copy(
        fontSize = 10.sp,
        color = TextDim
      ),
      textAlign = TextAlign.Center
    )
  }
}

private fun formatCountdownShort(seconds: Long): String {
  val days = seconds / 86400
  val hours = (seconds % 86400) / 3600
  val minutes = (seconds % 3600) / 60
  val secs = seconds % 60
  return String.format(Locale.US, "%dd %02dh:%02dm:%02ds", days, hours, minutes, secs)
}

@Composable
private fun RealTimePriceThresholdAlertBanner(
  alert: com.example.model.MarketPriceAlert,
  onDismiss: () -> Unit,
  onOpenSettings: () -> Unit
) {
  val borderColor = when (alert.breachType) {
    com.example.model.ThresholdBreachType.LOWER_SUPPORT_DROP -> InactiveCrimson
    com.example.model.ThresholdBreachType.UPPER_BARRIER_CROSS -> ActiveEmerald
    com.example.model.ThresholdBreachType.VOLATILITY_SURGE -> FalconCyan
    com.example.model.ThresholdBreachType.PERCENTAGE_FLUCTUATION -> if (alert.deltaPercent >= 0) ActiveEmerald else InactiveCrimson
  }

  val iconVector = when (alert.breachType) {
    com.example.model.ThresholdBreachType.LOWER_SUPPORT_DROP -> Icons.Default.TrendingDown
    com.example.model.ThresholdBreachType.UPPER_BARRIER_CROSS -> Icons.Default.TrendingUp
    com.example.model.ThresholdBreachType.VOLATILITY_SURGE -> Icons.Default.ShowChart
    com.example.model.ThresholdBreachType.PERCENTAGE_FLUCTUATION -> if (alert.deltaPercent >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown
  }

  Surface(
    shape = RoundedCornerShape(14.dp),
    color = ShaheenSurfaceCard,
    border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag("realtime_price_threshold_alert_banner")
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(borderColor.copy(alpha = 0.15f))
              .border(1.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = iconVector,
              contentDescription = null,
              tint = borderColor,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "🔔 تنبيه سعري لحظي: ${alert.pair}",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = TextWhite
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = borderColor.copy(alpha = 0.2f)
              ) {
                Text(
                  text = alert.breachType.labelAr,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                  ),
                  color = borderColor,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = "${alert.timestamp} • السعر: $${String.format(Locale.US, "%,.2f", alert.triggerPrice)} (حد الأمان: $${String.format(Locale.US, "%,.2f", alert.thresholdPrice)})",
              style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              ),
              color = FalconCyan
            )
          }
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.size(28.dp).testTag("dismiss_price_alert_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "إغلاق التنبيه",
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = alert.suggestedAction,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
        color = TextMuted
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        TextButton(
          onClick = onOpenSettings,
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
          modifier = Modifier.height(28.dp)
        ) {
          Text("تعديل الحواجز السعرية ⚙", color = FalconBlue, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.width(6.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = borderColor),
          shape = RoundedCornerShape(6.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
          modifier = Modifier.height(28.dp)
        ) {
          Text("إقرار واستجابة ✔", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        }
      }
    }
  }
}

@Composable
fun Bet23CooldownBanner(remainingMins: Int) {
  androidx.compose.material3.Surface(
    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    color = com.example.ui.theme.InactiveCrimson.copy(alpha = 0.15f),
    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.InactiveCrimson),
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(androidx.compose.material.icons.Icons.Default.Lock, contentDescription = null, tint = com.example.ui.theme.InactiveCrimson, modifier = Modifier.size(32.dp))
      Spacer(modifier = Modifier.width(16.dp))
      Column {
        Text("Bet23 Psycho-Temporal Lock", style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Black), color = com.example.ui.theme.InactiveCrimson)
        Text("الدرع السلوكي نشط: تم تجميد التداول لتبريد الانفعالات (Revenge Trading) وحماية الأرباح.", style = MaterialTheme.typography.labelSmall, color = com.example.ui.theme.TextWhite)
        Spacer(modifier = Modifier.height(4.dp))
        Text("يفتح النظام بعد: \$remainingMins دقيقة", style = MaterialTheme.typography.labelMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold), color = com.example.ui.theme.FalconCyan)
      }
    }
  }
}
