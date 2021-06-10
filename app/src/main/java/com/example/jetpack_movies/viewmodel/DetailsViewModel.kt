package com.example.jetpack_movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_movies.models.Movie
import com.example.jetpack_movies.repository.MovieRepository
import kotlinx.coroutines.launch

class DetailsViewModel (private val movieRepository : MovieRepository) : ViewModel() {

    private val _movieDetailLiveData : MutableLiveData<Movie> = MutableLiveData()
    val movieDetailLiveData : LiveData<Movie> = _movieDetailLiveData

    /**
     * Method to fetch movie based in Id
     *
     * @param movieId : Int
     */
    fun fetchMovieById(movieId : Int) {
        if (_movieDetailLiveData.value != null) {
            return
        }

        viewModelScope.launch {
            val movie = movieRepository.fetchMovieById(movieId)
            _movieDetailLiveData.postValue(movie)
        }
    }
}