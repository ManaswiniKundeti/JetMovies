package com.example.jetpack_movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack_movies.models.Movie
import com.example.jetpack_movies.ui.theme.Jetpack_MoviesTheme
import com.example.jetpack_movies.viewmodel.DetailsViewModel
import com.example.jetpack_movies.viewmodel.DetailsViewModelFactory
import com.google.accompanist.coil.rememberCoilPainter

class DetailActivity : ComponentActivity() {

    private val detailsViewmodelFactory by lazy { DetailsViewModelFactory(this) }
    private val detailsViewModel: DetailsViewModel by viewModels {
        detailsViewmodelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieId = intent.extras?.getString("movieId")
        movieId?.let { detailsViewModel.fetchMovieById(movieId = it.toInt()) }

        setContent {
            val movieDetailData by detailsViewModel.movieDetailLiveData.observeAsState()
            Jetpack_MoviesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    movieDetailData?.let { MovieDetailContent(it) }
                }
            }
        }
    }

    @Composable
    fun MovieDetailContent(movieDetails: Movie) {
        Column(modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())) {
            Image(
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                painter = rememberCoilPainter(
                    request = movieDetails.getImageUri()
                ),
                contentDescription = "Movie Image - ${movieDetails.movieName}"
            )
            Spacer(Modifier.height(20.dp))
            Text(
                maxLines = 1,
                text = movieDetails.movieName,
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "ID : ${movieDetails.movieId}",
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End
            )

            Spacer(Modifier.height(20.dp))
            Text(
                text = "MOVIE OVERVIEW ",
                color = Color.DarkGray,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = movieDetails.movieOverview,
                color = Color.Gray,
                fontSize = 20.sp,
                maxLines = 7,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(20.dp))
            Row() {
                Text(
                    text = "RELEASE DATE : ",
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = movieDetails.movieReleaseDate,
                    color = Color.Gray,
                    fontSize = 20.sp
                )
            }
            Spacer(Modifier.height(20.dp))
            Row() {
                Text(
                    text = "PRICE : ",
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = movieDetails.moviePrice.toString(),
                    color = Color.Gray,
                    fontSize = 20.sp
                )
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
//        Jetpack_MoviesTheme {
//            MovieDetailContent("Android")
//        }
    }
}