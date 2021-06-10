package com.example.jetpack_movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpack_movies.models.Movie
import com.example.jetpack_movies.repository.MovieRepository

class DetailsViewModel  (private val movieRepository : MovieRepository) : ViewModel() {

    private val _movieDetailLiveData : MutableLiveData<Movie> = MutableLiveData()
    val movieDetailLiveData : LiveData<Movie> = _movieDetailLiveData
}