package com.example.jetpack_movies.models

import com.squareup.moshi.Json

data class MovieResult(
    @field:Json(name = "results") val movieList : List<Movie>
)
