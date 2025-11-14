package com.example.supletanes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.supletanes.data.db.dao.SuplementoDao
import com.example.supletanes.data.db.entities.Suplemento

@Database(entities = [Suplemento::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun suplementoDao(): SuplementoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "suplementos_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}