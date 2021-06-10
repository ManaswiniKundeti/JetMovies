package com.example.jetpack_movies.repository

import com.example.jetpack_movies.models.Movie

interface IMovieRepository {
    suspend fun fetchMovies(forceRefresh: Boolean): List<Movie>
    suspend fun fetchMovieById(movieId : Int) : Movie
}