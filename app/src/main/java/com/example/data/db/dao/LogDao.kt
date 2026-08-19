package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.db.entity.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Query("SELECT * FROM market_logs ORDER BY id DESC")
    fun getAllLogs(): Flow<List<LogEntity>>

    @Query("SELECT * FROM market_logs ORDER BY id DESC LIMIT :limit")
    fun getRecentLogs(limit: Int): Flow<List<LogEntity>>

    @Query("SELECT * FROM market_logs WHERE id = :id LIMIT 1")
    suspend fun getLogById(id: Long): LogEntity?

    @Query("SELECT * FROM market_logs WHERE tag = :tag ORDER BY id DESC")
    fun getLogsByTag(tag: String): Flow<List<LogEntity>>

    @Query("SELECT * FROM market_logs WHERE level = :level ORDER BY id DESC")
    fun getLogsByLevel(level: String): Flow<List<LogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<LogEntity>)

    @Update
    suspend fun updateLog(log: LogEntity)

    @Delete
    suspend fun deleteLog(log: LogEntity)

    @Query("DELETE FROM market_logs WHERE id = :id")
    suspend fun deleteLogById(id: Long)

    @Query("DELETE FROM market_logs")
    suspend fun clearAllLogs()

    @Query("SELECT COUNT(*) FROM market_logs")
    fun getLogCount(): Flow<Int>
}
