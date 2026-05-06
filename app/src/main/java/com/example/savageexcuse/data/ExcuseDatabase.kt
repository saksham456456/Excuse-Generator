package com.example.savageexcuse.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Excuse::class], version = 1, exportSchema = false)
abstract class ExcuseDatabase : RoomDatabase() {
    abstract fun excuseDao(): ExcuseDao

    companion object {
        @Volatile
        private var INSTANCE: ExcuseDatabase? = null

        fun getDatabase(context: Context): ExcuseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExcuseDatabase::class.java,
                    "excuse_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
