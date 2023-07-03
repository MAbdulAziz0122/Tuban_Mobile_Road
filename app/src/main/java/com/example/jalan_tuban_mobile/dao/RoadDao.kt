package com.example.jalan_tuban_mobile.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.jalan_tuban_mobile.model.Road


@Dao
interface RoadDao {
    @Query("SELECT * FROM road_table ORDER BY name ASC")
    fun getAllRoad(): kotlinx.coroutines.flow.Flow<List<Road>>

    @Insert
    suspend fun insertRoad(road: Road)

    @Delete
    suspend fun deleteRoad(road: Road)

    @Update fun updateRoad(road: Road)
}