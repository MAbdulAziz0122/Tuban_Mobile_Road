package com.example.jalan_tuban_mobile.Application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jalan_tuban_mobile.dao.RoadDao
import com.example.jalan_tuban_mobile.model.Road

@Database(entities = [Road::class], version = 1, exportSchema = false)
abstract class RoadDatabase: RoomDatabase() {
    abstract fun roadDao(): RoadDao

    companion object{
        private var INSTANCE: RoadDatabase? = null

        fun getDatabase(context: Context): RoadDatabase{
            return  INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoadDatabase::class.java,
                    "road_database"
                )
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}