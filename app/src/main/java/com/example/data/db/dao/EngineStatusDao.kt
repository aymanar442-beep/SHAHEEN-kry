package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.db.entity.EngineStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EngineStatusDao {
    @Query("SELECT * FROM engine_status WHERE id = :id LIMIT 1")
    fun getEngineStatusFlow(id: Int = 1): Flow<EngineStatusEntity?>

    @Query("SELECT * FROM engine_status WHERE id = :id LIMIT 1")
    suspend fun getEngineStatus(id: Int = 1): EngineStatusEntity?

    @Query("SELECT * FROM engine_status ORDER BY lastUpdated DESC")
    fun getAllStatuses(): Flow<List<EngineStatusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(status: EngineStatusEntity)

    @Update
    suspend fun updateEngineStatus(status: EngineStatusEntity)

    @Delete
    suspend fun deleteEngineStatus(status: EngineStatusEntity)

    @Query("DELETE FROM engine_status WHERE id = :id")
    suspend fun deleteEngineStatusById(id: Int = 1)

    @Query("DELETE FROM engine_status")
    suspend fun clearAllEngineStatus()
}
