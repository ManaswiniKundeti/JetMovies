package com.example.jetpack_movies.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetpack_movies.di.NetworkModule
import com.example.jetpack_movies.repository.MovieRepository

class DetailsViewModelFactory (private val context : Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return DetailsViewModel(MovieRepository(NetworkModule.createApiService())) as T
        }
        throw IllegalArgumentException("Unknown details view model class")
    }
}