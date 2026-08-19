package com.example.data.repository

import com.example.data.db.dao.EngineStatusDao
import com.example.data.db.dao.LogDao
import com.example.data.db.dao.MarketTrendDao
import com.example.data.db.entity.EngineStatusEntity
import com.example.data.db.entity.LogEntity
import com.example.data.db.entity.MarketTrendEntity
import com.example.model.EngineStatus
import com.example.model.LogEntry
import com.example.model.LogLevel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Interface defining repository operations for LogDao, EngineStatusDao, and MarketTrendDao.
 */
interface Repository {
    // Raw Entity Flows directly from Room
    val logEntitiesFlow: Flow<List<LogEntity>>
    val engineStatusEntityFlow: Flow<EngineStatusEntity?>
    val marketTrendEntitiesFlow: Flow<List<MarketTrendEntity>>

    // Domain Model Flows
    val allLogs: Flow<List<LogEntry>>
    fun getRecentLogs(limit: Int = 100): Flow<List<LogEntry>>
    fun getLogsByTag(tag: String): Flow<List<LogEntry>>
    fun getLogsByLevel(level: LogLevel): Flow<List<LogEntry>>
    fun getLogCount(): Flow<Int>

    // Log suspend CRUD
    suspend fun getLogById(id: Long): LogEntry?
    suspend fun insertLog(log: LogEntry): Long
    suspend fun insertLogEntity(entity: LogEntity): Long
    suspend fun insertLogs(logs: List<LogEntry>)
    suspend fun updateLog(log: LogEntry)
    suspend fun deleteLog(log: LogEntry)
    suspend fun deleteLogById(id: Long)
    suspend fun clearLogs()

    // Engine status operations (Flow & Suspend CRUD)
    val engineStatusFlow: Flow<EngineStatus?>
    suspend fun getEngineStatus(id: Int = 1): EngineStatus?
    suspend fun getEngineStatusEntity(id: Int = 1): EngineStatusEntity?
    suspend fun saveEngineStatus(status: EngineStatus, id: Int = 1)
    suspend fun saveEngineStatusEntity(entity: EngineStatusEntity)
    suspend fun updateEngineStatus(status: EngineStatus, id: Int = 1)
    suspend fun deleteEngineStatusById(id: Int = 1)
    suspend fun clearEngineStatus()

    // Market trend operations
    val allMarketTrends: Flow<List<MarketTrendEntity>>
    fun getRecentMarketTrends(limit: Int = 100): Flow<List<MarketTrendEntity>>
    suspend fun getAllMarketTrendsList(): List<MarketTrendEntity>
    suspend fun insertMarketTrend(trend: MarketTrendEntity): Long
    suspend fun insertMarketTrends(trends: List<MarketTrendEntity>)
    suspend fun clearMarketTrends()
}

/**
 * Default implementation of [Repository] abstracting Room DAOs
 * and handling asynchronous data flows with coroutine dispatchers.
 */
open class AppRepository(
    private val logDao: LogDao,
    private val engineStatusDao: EngineStatusDao,
    private val marketTrendDao: MarketTrendDao? = null,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    override val marketTrendEntitiesFlow: Flow<List<MarketTrendEntity>> =
        marketTrendDao?.getAllTrends() ?: kotlinx.coroutines.flow.flowOf(emptyList())

    override val allMarketTrends: Flow<List<MarketTrendEntity>> =
        marketTrendDao?.getAllTrends() ?: kotlinx.coroutines.flow.flowOf(emptyList())

    override fun getRecentMarketTrends(limit: Int): Flow<List<MarketTrendEntity>> =
        marketTrendDao?.getRecentTrends(limit) ?: kotlinx.coroutines.flow.flowOf(emptyList())

    override suspend fun getAllMarketTrendsList(): List<MarketTrendEntity> = withContext(ioDispatcher) {
        marketTrendDao?.getAllTrendsList() ?: emptyList()
    }

    override suspend fun insertMarketTrend(trend: MarketTrendEntity): Long = withContext(ioDispatcher) {
        marketTrendDao?.insertTrend(trend) ?: 0L
    }

    override suspend fun insertMarketTrends(trends: List<MarketTrendEntity>) = withContext(ioDispatcher) {
        marketTrendDao?.insertTrends(trends) ?: Unit
    }

    override suspend fun clearMarketTrends() = withContext(ioDispatcher) {
        marketTrendDao?.clearAllTrends() ?: Unit
    }

    override val logEntitiesFlow: Flow<List<LogEntity>> = logDao.getAllLogs()
        .flowOn(ioDispatcher)

    override val engineStatusEntityFlow: Flow<EngineStatusEntity?> = engineStatusDao.getEngineStatusFlow(1)
        .flowOn(ioDispatcher)

    override val allLogs: Flow<List<LogEntry>> = logDao.getAllLogs()
        .map { entities -> entities.map { it.toDomain() } }
        .flowOn(ioDispatcher)

    override fun getRecentLogs(limit: Int): Flow<List<LogEntry>> = logDao.getRecentLogs(limit)
        .map { entities -> entities.map { it.toDomain() } }
        .flowOn(ioDispatcher)

    override fun getLogsByTag(tag: String): Flow<List<LogEntry>> = logDao.getLogsByTag(tag)
        .map { entities -> entities.map { it.toDomain() } }
        .flowOn(ioDispatcher)

    override fun getLogsByLevel(level: LogLevel): Flow<List<LogEntry>> = logDao.getLogsByLevel(level.name)
        .map { entities -> entities.map { it.toDomain() } }
        .flowOn(ioDispatcher)

    override fun getLogCount(): Flow<Int> = logDao.getLogCount()
        .flowOn(ioDispatcher)

    override suspend fun getLogById(id: Long): LogEntry? = withContext(ioDispatcher) {
        logDao.getLogById(id)?.toDomain()
    }

    override suspend fun insertLog(log: LogEntry): Long = withContext(ioDispatcher) {
        logDao.insertLog(LogEntity.fromDomain(log))
    }

    override suspend fun insertLogEntity(entity: LogEntity): Long = withContext(ioDispatcher) {
        logDao.insertLog(entity)
    }

    override suspend fun insertLogs(logs: List<LogEntry>) = withContext(ioDispatcher) {
        logDao.insertLogs(logs.map { LogEntity.fromDomain(it) })
    }

    override suspend fun updateLog(log: LogEntry) = withContext(ioDispatcher) {
        logDao.updateLog(LogEntity.fromDomain(log))
    }

    override suspend fun deleteLog(log: LogEntry) = withContext(ioDispatcher) {
        logDao.deleteLog(LogEntity.fromDomain(log))
    }

    override suspend fun deleteLogById(id: Long) = withContext(ioDispatcher) {
        logDao.deleteLogById(id)
    }

    override suspend fun clearLogs() = withContext(ioDispatcher) {
        logDao.clearAllLogs()
    }

    override val engineStatusFlow: Flow<EngineStatus?> = engineStatusDao.getEngineStatusFlow(1)
        .map { it?.toDomain() }
        .flowOn(ioDispatcher)

    override suspend fun getEngineStatus(id: Int): EngineStatus? = withContext(ioDispatcher) {
        engineStatusDao.getEngineStatus(id)?.toDomain()
    }

    override suspend fun getEngineStatusEntity(id: Int): EngineStatusEntity? = withContext(ioDispatcher) {
        engineStatusDao.getEngineStatus(id)
    }

    override suspend fun saveEngineStatus(status: EngineStatus, id: Int) = withContext(ioDispatcher) {
        engineStatusDao.insertOrUpdate(EngineStatusEntity.fromDomain(status, id))
    }

    override suspend fun saveEngineStatusEntity(entity: EngineStatusEntity) = withContext(ioDispatcher) {
        engineStatusDao.insertOrUpdate(entity)
    }

    override suspend fun updateEngineStatus(status: EngineStatus, id: Int) = withContext(ioDispatcher) {
        engineStatusDao.updateEngineStatus(EngineStatusEntity.fromDomain(status, id))
    }

    override suspend fun deleteEngineStatusById(id: Int) = withContext(ioDispatcher) {
        engineStatusDao.deleteEngineStatusById(id)
    }

    override suspend fun clearEngineStatus() = withContext(ioDispatcher) {
        engineStatusDao.clearAllEngineStatus()
    }
}
