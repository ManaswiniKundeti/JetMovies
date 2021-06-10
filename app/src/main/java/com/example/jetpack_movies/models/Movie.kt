package com.example.jetpack_movies.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Movie(
    @field:Json(name = "id") @PrimaryKey val movieId : Int,
    @field:Json(name = "title") val movieName: String,
    @field:Json(name = "backdrop_path") val movieImageUri : String,
    @field:Json(name = "overview") val movieOverview : String,
    @field:Json(name = "vote_average") val movieRating : Float,
    @field:Json(name = "release_date") val movieReleaseDate : String,
    @field:Json(name = "vote_count") val movieVoteCount : Int,
    var moviePrice: Float
) {
    /**
     * Helper extension method to build image url
     */
    fun getImageUri(): String {
        return "https://image.tmdb.org/t/p/w500${movieImageUri}"
    }
}
