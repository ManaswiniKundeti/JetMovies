package com.example.jetpack_movies.di

import android.content.Context
import androidx.room.Room
import com.example.jetpack_movies.persistence.AppDatabase

object PersistenceModule {
    private var INSTANCE : AppDatabase? = null

//    @Singleton
//    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        if(INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,"movie_db")
                    .build()
            }
        }
        return INSTANCE!!
    }
}