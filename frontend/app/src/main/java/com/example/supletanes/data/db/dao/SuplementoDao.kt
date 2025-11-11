package com.example.supletanes.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.supletanes.data.db.entities.Suplemento
import kotlinx.coroutines.flow.Flow

@Dao
interface SuplementoDao {
    @Query("SELECT * FROM suplemento ORDER BY id DESC")
    fun getAllSupplements(): Flow<List<Suplemento>> // Room soporta directamente Flow

    @Query("SELECT * FROM suplemento WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): Suplemento?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplement(suplemento: Suplemento)

    @Update
    suspend fun update(suplemento: Suplemento)

    @Query("DELETE FROM suplemento WHERE id = :id")
    suspend fun deleteSupplementById(id: Int)
}