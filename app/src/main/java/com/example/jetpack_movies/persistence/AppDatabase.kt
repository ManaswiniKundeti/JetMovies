package com.example.jetpack_movies.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetpack_movies.models.Movie

@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao() : MovieDao
}