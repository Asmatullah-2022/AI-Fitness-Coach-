package com.aifitnesscoach.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aifitnesscoach.app.data.local.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightEntryDao {

    @Insert
    suspend fun insert(entry: WeightEntryEntity): Long

    @Query("SELECT * FROM weight_entries ORDER BY epochDay ASC, id ASC")
    fun observeAll(): Flow<List<WeightEntryEntity>>

    @Query("DELETE FROM weight_entries")
    suspend fun clear()
}
