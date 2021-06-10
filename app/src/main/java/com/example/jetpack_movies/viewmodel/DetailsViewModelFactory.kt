package com.example.jetpack_movies.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetpack_movies.di.NetworkModule
import com.example.jetpack_movies.di.PersistenceModule
import com.example.jetpack_movies.repository.MovieRepository

class DetailsViewModelFactory (private val context : Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            return DetailsViewModel(MovieRepository(NetworkModule.createApiService(), PersistenceModule.provideAppDatabase(context = context))) as T
        }
        throw IllegalArgumentException("Unknown details view model class")
    }
}