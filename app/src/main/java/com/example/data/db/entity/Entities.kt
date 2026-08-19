package com.example.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.EngineStatus
import com.example.model.LogEntry
import com.example.model.LogLevel

@Entity(tableName = "market_logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val timestamp: String,
    val tag: String,
    val message: String,
    val level: String = LogLevel.INFO.name
) {
    fun toDomain(): LogEntry {
        val parsedLevel = try {
            LogLevel.valueOf(level)
        } catch (e: Exception) {
            LogLevel.INFO
        }
        return LogEntry(
            id = id,
            timestamp = timestamp,
            tag = tag,
            message = message,
            level = parsedLevel
        )
    }

    companion object {
        fun fromDomain(entry: LogEntry): LogEntity {
            return LogEntity(
                id = if (entry.id < 0) 0L else entry.id,
                timestamp = entry.timestamp,
                tag = entry.tag,
                message = entry.message,
                level = entry.level.name
            )
        }
    }
}

@Entity(tableName = "engine_status")
data class EngineStatusEntity(
    @PrimaryKey
    val id: Int = 1,
    val isRunning: Boolean = false,
    val uptimeSeconds: Long = 0L,
    val totalCycles: Long = 0L,
    val currentPair: String = "BTC/USDT",
    val latencyMs: Int = 14,
    val priceIndex: Double = 98450.20,
    val volumeScanned: Double = 1.42,
    val riskScore: Double = 0.02,
    val testRemainingSeconds: Long = 86400L * 3L + 14320L,
    val behavioralStability: Double = 99.8,
    val preEmptiveOrdersArmed: Int = 8,
    val isOfflineImmune: Boolean = true,
    val sub100msFlashBreakerActive: Boolean = true,
    val autoSweepVaultSecured: Boolean = true,
    val knoxBiometricArmed: Boolean = true,
    val spotColdVaultUsdt: Double = 25480.00,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toDomain(): EngineStatus {
        return EngineStatus(
            isRunning = isRunning,
            uptimeSeconds = uptimeSeconds,
            totalCycles = totalCycles,
            currentPair = currentPair,
            latencyMs = latencyMs,
            priceIndex = priceIndex,
            volumeScanned = volumeScanned,
            riskScore = riskScore,
            testRemainingSeconds = testRemainingSeconds,
            behavioralStability = behavioralStability,
            preEmptiveOrdersArmed = preEmptiveOrdersArmed,
            isOfflineImmune = isOfflineImmune,
            sub100msFlashBreakerActive = sub100msFlashBreakerActive,
            autoSweepVaultSecured = autoSweepVaultSecured,
            knoxBiometricArmed = knoxBiometricArmed,
            spotColdVaultUsdt = spotColdVaultUsdt
        )
    }

    companion object {
        fun fromDomain(domain: EngineStatus, id: Int = 1): EngineStatusEntity {
            return EngineStatusEntity(
                id = id,
                isRunning = domain.isRunning,
                uptimeSeconds = domain.uptimeSeconds,
                totalCycles = domain.totalCycles,
                currentPair = domain.currentPair,
                latencyMs = domain.latencyMs,
                priceIndex = domain.priceIndex,
                volumeScanned = domain.volumeScanned,
                riskScore = domain.riskScore,
                testRemainingSeconds = domain.testRemainingSeconds,
                behavioralStability = domain.behavioralStability,
                preEmptiveOrdersArmed = domain.preEmptiveOrdersArmed,
                isOfflineImmune = domain.isOfflineImmune,
                sub100msFlashBreakerActive = domain.sub100msFlashBreakerActive,
                autoSweepVaultSecured = domain.autoSweepVaultSecured,
                knoxBiometricArmed = domain.knoxBiometricArmed,
                spotColdVaultUsdt = domain.spotColdVaultUsdt,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }
}
