package com.example.jalan_tuban_mobile.repository

import com.example.jalan_tuban_mobile.dao.RoadDao
import com.example.jalan_tuban_mobile.model.Road
import kotlinx.coroutines.flow.Flow

class RoadRepository(private val roadDao: RoadDao) {
    val allroads: Flow<List<Road>> = roadDao.getAllRoad()

    suspend fun insertroad(road: Road){
        roadDao.insertRoad(road)
    }

    suspend fun deleteroad(road: Road){
        roadDao.deleteRoad(road)
    }

    suspend fun updateroad(road: Road){
        roadDao.updateRoad(road)
    }
}