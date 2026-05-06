package com.example.savageexcuse.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExcuseDao {
    @Query("SELECT * FROM excuses")
    fun getAllExcuses(): Flow<List<Excuse>>

    @Query("SELECT * FROM excuses WHERE category IN (:categories)")
    fun getExcusesByCategories(categories: List<String>): Flow<List<Excuse>>

    @Query("SELECT * FROM excuses WHERE isFavorite = 1")
    fun getFavoriteExcuses(): Flow<List<Excuse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(excuses: List<Excuse>)

    @Update
    suspend fun updateExcuse(excuse: Excuse)

    @Query("SELECT COUNT(*) FROM excuses")
    suspend fun getCount(): Int
}
