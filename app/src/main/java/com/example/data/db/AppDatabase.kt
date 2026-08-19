package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.db.dao.EngineStatusDao
import com.example.data.db.dao.LogDao
import com.example.data.db.dao.MarketTrendDao
import com.example.data.db.entity.EngineStatusEntity
import com.example.data.db.entity.LogEntity
import com.example.data.db.entity.MarketTrendEntity

@Database(
    entities = [LogEntity::class, EngineStatusEntity::class, MarketTrendEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun logDao(): LogDao
    abstract fun engineStatusDao(): EngineStatusDao
    abstract fun marketTrendDao(): MarketTrendDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shaheen_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
