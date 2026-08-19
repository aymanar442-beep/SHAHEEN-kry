package com.example.data.export

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.data.db.entity.MarketTrendEntity
import com.example.model.EngineStatus
import com.example.model.LogEntry
import com.example.model.ShaheenConfig
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ExportFormat(val extension: String, val mimeType: String, val label: String) {
  CSV("csv", "text/csv", "CSV (Excel / Data Science)"),
  JSON("json", "application/json", "JSON (Structured Analytics / API)")
}

enum class ExportScope(val labelAr: String, val labelEn: String) {
  ALL_TELEMETRY("حزمة التحليل الشاملة (سجلات + اتجاهات + المحرك)", "Full Telemetry & Trends Package"),
  LOGS_ONLY("سجلات التيليميتري فقط (Telemetry Logs)", "Telemetry Logs Only"),
  MARKET_TRENDS_ONLY("اتجاهات السوق وبيانات الأسعار (Market Trends)", "Market Trends Only")
}

data class ExportResult(
  val success: Boolean,
  val fileName: String,
  val filePath: String? = null,
  val fileUriString: String? = null,
  val totalItemsExported: Int = 0,
  val fileSizeKb: Double = 0.0,
  val contentPreview: String = "",
  val errorMessage: String? = null
)

object DataExportManager {

  private val timestampFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)

  fun exportLogsToCsv(logs: List<LogEntry>): String {
    val sb = StringBuilder()
    sb.append("LogID,Timestamp,Tag,Severity,Message\n")
    logs.forEach { log ->
      val escapedMessage = log.message.replace("\"", "\"\"")
      val escapedTag = log.tag.replace("\"", "\"\"")
      sb.append("${log.id},\"${log.timestamp}\",\"$escapedTag\",${log.level.name},\"$escapedMessage\"\n")
    }
    return sb.toString()
  }

  fun exportLogsToJson(logs: List<LogEntry>): String {
    val sb = StringBuilder()
    sb.append("[\n")
    logs.forEachIndexed { index, log ->
      val escapedMsg = escapeJson(log.message)
      val escapedTag = escapeJson(log.tag)
      sb.append("  {\n")
      sb.append("    \"id\": ${log.id},\n")
      sb.append("    \"timestamp\": \"${log.timestamp}\",\n")
      sb.append("    \"tag\": \"$escapedTag\",\n")
      sb.append("    \"level\": \"${log.level.name}\",\n")
      sb.append("    \"message\": \"$escapedMsg\"\n")
      sb.append("  }")
      if (index < logs.size - 1) sb.append(",")
      sb.append("\n")
    }
    sb.append("]")
    return sb.toString()
  }

  fun exportMarketTrendsToCsv(trends: List<MarketTrendEntity>): String {
    val sb = StringBuilder()
    sb.append("ID,Timestamp,Pair,Price_USDT,Volume_24h,Trend_Direction,Momentum_Score,Support_USDT,Resistance_USDT,RSI_14,Volatility_Pct,Signal_Advice\n")
    trends.forEach { t ->
      sb.append("${t.id},\"${t.timestamp}\",\"${t.pair}\",${t.price},${t.volume24h},\"${t.trendDirection}\",${t.momentumScore},${t.supportLevel},${t.resistanceLevel},${t.rsi14},${t.volatilityPercent},\"${t.signalAdvice}\"\n")
    }
    return sb.toString()
  }

  fun exportMarketTrendsToJson(trends: List<MarketTrendEntity>): String {
    val sb = StringBuilder()
    sb.append("[\n")
    trends.forEachIndexed { index, t ->
      sb.append("  {\n")
      sb.append("    \"id\": ${t.id},\n")
      sb.append("    \"timestamp\": \"${t.timestamp}\",\n")
      sb.append("    \"pair\": \"${t.pair}\",\n")
      sb.append("    \"price_usdt\": ${t.price},\n")
      sb.append("    \"volume_24h_m\": ${t.volume24h},\n")
      sb.append("    \"trend_direction\": \"${t.trendDirection}\",\n")
      sb.append("    \"momentum_score\": ${t.momentumScore},\n")
      sb.append("    \"support_level\": ${t.supportLevel},\n")
      sb.append("    \"resistance_level\": ${t.resistanceLevel},\n")
      sb.append("    \"rsi_14\": ${t.rsi14},\n")
      sb.append("    \"volatility_percent\": ${t.volatilityPercent},\n")
      sb.append("    \"signal_advice\": \"${t.signalAdvice}\"\n")
      sb.append("  }")
      if (index < trends.size - 1) sb.append(",")
      sb.append("\n")
    }
    sb.append("]")
    return sb.toString()
  }

  fun exportFullTelemetryPackageJson(
    logs: List<LogEntry>,
    trends: List<MarketTrendEntity>,
    status: EngineStatus,
    config: ShaheenConfig
  ): String {
    val exportDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date())
    val sb = StringBuilder()
    sb.append("{\n")
    sb.append("  \"export_meta\": {\n")
    sb.append("    \"app_name\": \"SHAHEEN APEX AI\",\n")
    sb.append("    \"export_timestamp\": \"$exportDate\",\n")
    sb.append("    \"version\": \"v3.0.0-PRO-SOVEREIGN\",\n")
    sb.append("    \"operator\": \"${escapeJson(config.username)}\",\n")
    sb.append("    \"logs_count\": ${logs.size},\n")
    sb.append("    \"trends_count\": ${trends.size}\n")
    sb.append("  },\n")
    sb.append("  \"engine_status\": {\n")
    sb.append("    \"is_running\": ${status.isRunning},\n")
    sb.append("    \"active_pair\": \"${status.currentPair}\",\n")
    sb.append("    \"price_index\": ${status.priceIndex},\n")
    sb.append("    \"latency_ms\": ${status.latencyMs},\n")
    sb.append("    \"risk_score\": ${status.riskScore},\n")
    sb.append("    \"behavioral_stability\": ${status.behavioralStability},\n")
    sb.append("    \"spot_cold_vault_usdt\": ${status.spotColdVaultUsdt},\n")
    sb.append("    \"total_cycles\": ${status.totalCycles},\n")
    sb.append("    \"offline_immune\": ${status.isOfflineImmune},\n")
    sb.append("    \"flash_breaker_active\": ${status.sub100msFlashBreakerActive}\n")
    sb.append("  },\n")
    sb.append("  \"market_trends\": ")
    sb.append(exportMarketTrendsToJson(trends))
    sb.append(",\n")
    sb.append("  \"telemetry_logs\": ")
    sb.append(exportLogsToJson(logs))
    sb.append("\n}")
    return sb.toString()
  }

  fun exportFullTelemetryPackageCsv(
    logs: List<LogEntry>,
    trends: List<MarketTrendEntity>
  ): String {
    val sb = StringBuilder()
    sb.append("# === SHAHEEN APEX AI TELEMETRY EXPORT ===\n")
    sb.append("# Generated on: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())}\n\n")
    sb.append("# --- SECTION 1: MARKET TRENDS ---\n")
    sb.append(exportMarketTrendsToCsv(trends))
    sb.append("\n# --- SECTION 2: TELEMETRY LOGS ---\n")
    sb.append(exportLogsToCsv(logs))
    return sb.toString()
  }

  fun saveExportToFile(
    context: Context,
    fileName: String,
    content: String
  ): File {
    val exportDir = File(context.filesDir, "exports").apply {
      if (!exists()) mkdirs()
    }
    val file = File(exportDir, fileName)
    FileWriter(file).use { writer ->
      writer.write(content)
    }
    return file
  }

  fun generateExportFileName(scope: ExportScope, format: ExportFormat): String {
    val timestamp = timestampFormatter.format(Date())
    val prefix = when (scope) {
      ExportScope.ALL_TELEMETRY -> "shaheen_full_telemetry"
      ExportScope.LOGS_ONLY -> "shaheen_logs"
      ExportScope.MARKET_TRENDS_ONLY -> "shaheen_market_trends"
    }
    return "${prefix}_$timestamp.${format.extension}"
  }

  private fun escapeJson(value: String): String {
    return value
      .replace("\\", "\\\\")
      .replace("\"", "\\\"")
      .replace("\b", "\\b")
      .replace("\n", "\\n")
      .replace("\r", "\\r")
      .replace("\t", "\\t")
  }
}
