package com.example.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "market_trends")
data class MarketTrendEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val timestamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()),
    val pair: String = "BTC/USDT",
    val price: Double = 98450.0,
    val volume24h: Double = 1.42,
    val trendDirection: String = "BULLISH",
    val momentumScore: Double = 84.5,
    val supportLevel: Double = 98100.0,
    val resistanceLevel: Double = 98900.0,
    val rsi14: Double = 62.4,
    val volatilityPercent: Double = 0.28,
    val signalAdvice: String = "ACCUMULATE"
)
