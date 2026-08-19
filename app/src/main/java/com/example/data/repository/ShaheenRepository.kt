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
 * Repository class implementing the logic to bridge Room DAOs ([LogDao], [EngineStatusDao], [MarketTrendDao])
 * and the [ShaheenViewModel].
 *
 * Exposes cached [LogEntity], [EngineStatusEntity], and [MarketTrendEntity] data from Room as reactive [Flow]s,
 * keeping the UI synchronized with the on-device database.
 */
class ShaheenRepository(
    private val logDao: LogDao,
    private val engineStatusDao: EngineStatusDao,
    private val marketTrendDao: MarketTrendDao? = null,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppRepository(logDao, engineStatusDao, marketTrendDao, ioDispatcher)
