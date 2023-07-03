package com.example.jalan_tuban_mobile.Application

import android.app.Application
import com.example.jalan_tuban_mobile.repository.RoadRepository

class RoadApp: Application() {
    val database by lazy {RoadDatabase.getDatabase(this)}
    val repository by lazy { RoadRepository(database.roadDao()) }
}