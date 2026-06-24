package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.models.Idea

@Database(entities = [Idea::class], version = 2, exportSchema = false)
abstract class BrainDropDatabase : RoomDatabase() {
    abstract fun ideaDao(): IdeaDao

    companion object {
        @Volatile
        private var Instance: BrainDropDatabase? = null

        fun getDatabase(context: Context): BrainDropDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BrainDropDatabase::class.java, "braindrop_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
