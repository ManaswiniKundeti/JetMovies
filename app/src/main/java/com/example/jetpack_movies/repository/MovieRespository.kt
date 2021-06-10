package com.example.jetpack_movies.repository

import com.example.jetpack_movies.models.Movie
import com.example.jetpack_movies.network.IApiService
import com.example.jetpack_movies.persistence.AppDatabase

class MovieRepository(private val apiService : IApiService, private val appDatabase: AppDatabase) : IMovieRepository {

    /**
     * Method to fetch movies
     *
     * @param forceRefresh Flag to force refresh from API
     */
    override suspend fun fetchMovies(forceRefresh: Boolean): List<Movie> {
        return if (forceRefresh) {
            fetchMoviesFromApiAndCacheLocally()
        } else {
            // First try to fetch movies from database
            val localMovies = appDatabase.movieDao().getMovies()
            return if (localMovies.isNullOrEmpty()) {
                fetchMoviesFromApiAndCacheLocally()
            } else {
                localMovies
            }
        }
    }

    /**
     * Helper method to fetch movies from the TMBD Api Service
     *
     * @return List of Movies
     *
     * @see Movie
     */
     private suspend fun fetchMoviesFromApi(): List<Movie> {
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
     * Helper method to fetch movies from the API, cache them locally and return cached list of movies
     *
     * @return List of Movies
     *
     * @see Movie
     */
    private suspend fun fetchMoviesFromApiAndCacheLocally(): List<Movie> {
        val apiMovies = fetchMoviesFromApi()

        appDatabase.movieDao().insertMovies(apiMovies)
        return appDatabase.movieDao().getMovies()
    }

    /**
     * Method to fetch movie by id from the local database
     *
     * @param movieId Integer Id of the required movie
     *
     * @return Movie
     *
     * @see Movie
     */
    override suspend fun fetchMovieById(movieId : Int) : Movie {
        return appDatabase.movieDao().getMovieById(movieId)
    }

    /**
     * Helper method to generate a random price for a movie
     */
    private fun getRandomPrice() = ((5..9).random() + 0.99).toFloat()
}
