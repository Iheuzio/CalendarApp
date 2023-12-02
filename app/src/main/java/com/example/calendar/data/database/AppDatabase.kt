package com.example.calendar.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import android.content.Context

@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        /**
         * Singleton pattern to ensure only one instance of the database is created
         * @param context Context of the application
         * @return AppDatabase
         */
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "event-database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = INSTANCE
            }
            return INSTANCE!!
        }
    }
}