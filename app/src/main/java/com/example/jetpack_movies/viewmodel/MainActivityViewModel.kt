package com.example.jetpack_movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_movies.models.Movie
import com.example.jetpack_movies.repository.MovieRepository
import kotlinx.coroutines.launch

class MainActivityViewModel (private val movieRepository : MovieRepository) : ViewModel() {

    private val _itemListLiveData : MutableLiveData<List<Movie>> = MutableLiveData()
    val itemListLiveData : LiveData<List<Movie>> = _itemListLiveData


    fun fetchMovies() {
//        val itemList = listOf("aaa",
//            "January",
//            "February",
//            "March",
//            "April",
//            "May",
//            "June",
//            "July",
//            "August",
//            "September",
//            "October",
//            "November",
//            "December",
//        )
//        _itemListLiveData.postValue(itemList)

        viewModelScope.launch {
            val movieList = movieRepository.fetchMovies(false)
            if (movieList.isNullOrEmpty()) {
                _itemListLiveData.postValue(null)
            } else {
                _itemListLiveData.postValue(movieList)
            }
        }

    }
}