package com.example.jetpack_movies.repository

import com.example.jetpack_movies.models.Movie

interface IMovieRepository {
    suspend fun fetchMoviesFromApi(): List<Movie>
}