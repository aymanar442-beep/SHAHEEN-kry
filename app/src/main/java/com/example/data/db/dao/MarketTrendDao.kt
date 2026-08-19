package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.db.entity.MarketTrendEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketTrendDao {
    @Query("SELECT * FROM market_trends ORDER BY id DESC")
    fun getAllTrends(): Flow<List<MarketTrendEntity>>

    @Query("SELECT * FROM market_trends ORDER BY id DESC LIMIT :limit")
    fun getRecentTrends(limit: Int = 100): Flow<List<MarketTrendEntity>>

    @Query("SELECT * FROM market_trends ORDER BY id DESC")
    suspend fun getAllTrendsList(): List<MarketTrendEntity>

    @Query("SELECT * FROM market_trends WHERE pair = :pair ORDER BY id DESC")
    fun getTrendsByPair(pair: String): Flow<List<MarketTrendEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrend(trend: MarketTrendEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrends(trends: List<MarketTrendEntity>)

    @Update
    suspend fun updateTrend(trend: MarketTrendEntity)

    @Delete
    suspend fun deleteTrend(trend: MarketTrendEntity)

    @Query("DELETE FROM market_trends WHERE id = :id")
    suspend fun deleteTrendById(id: Long)

    @Query("DELETE FROM market_trends")
    suspend fun clearAllTrends()

    @Query("SELECT COUNT(*) FROM market_trends")
    fun getTrendCount(): Flow<Int>
}
