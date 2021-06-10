package com.example.jetpack_movies.repository

import com.example.jetpack_movies.models.Movie
import com.example.jetpack_movies.network.IApiService

class MovieRepository(private val apiService : IApiService) : IMovieRepository {

    /**
     * Helper method to fetch movies from the TMBD Api Service
     *
     * @return List of Movies
     *
     * @see Movie
     */
    override suspend fun fetchMoviesFromApi(): List<Movie> {
        return try {
            val apiResponse = apiService.fetchMoviesList()
            if (apiResponse.isSuccessful && apiResponse.body() != null) {
                val apiMovies = apiResponse.body()!!.movieList
                apiMovies.forEach { movie ->
                    movie.moviePrice = getRandomPrice()
                }
                apiMovies
            } else {
                mutableListOf()
            }
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    /**
     * Helper method to generate a random price for a movie
     */
    private fun getRandomPrice() = ((5..9).random() + 0.99).toFloat()
}
