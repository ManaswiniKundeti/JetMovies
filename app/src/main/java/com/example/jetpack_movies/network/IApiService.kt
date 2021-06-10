package com.example.jetpack_movies.network

import com.example.jetpack_movies.BuildConfig
import com.example.jetpack_movies.models.MovieResult
import retrofit2.Response
import retrofit2.http.GET

interface IApiService {
    @GET("popular?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun fetchMoviesList() : Response<MovieResult>
}