package com.example.jetpack_movies

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.jetpack_movies.models.Movie
import com.example.jetpack_movies.ui.theme.Jetpack_MoviesTheme
import com.example.jetpack_movies.viewmodel.MainActivityViewModel
import com.example.jetpack_movies.viewmodel.MainActivityViewModelFactory
import com.google.accompanist.coil.rememberCoilPainter

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val viewModel: MainActivityViewModel by viewModels()

    private val viewmodelFactory by lazy { MainActivityViewModelFactory(this) }
    private val viewModel: MainActivityViewModel by viewModels {
        viewmodelFactory
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchMovies()

        setContent {
            val itemListData by viewModel.itemListLiveData.observeAsState()

            Jetpack_MoviesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    itemListData?.let { MainAppContent(it) }
                }
            }

        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun MainAppContent(itemList: List<Movie>) {
    //default screen is always in Home
    val homeScreenState = rememberSaveable { mutableStateOf(BottomNavType.HOME) }
    val bottomBarContentDescription = stringResource(id = R.string.bottom_navigation_bar)

    Column {
        HomeScreenContent(
            itemList = itemList,
            homeScreenNavType = homeScreenState.value,
            modifier = Modifier.weight(1f)
        )
//        BottomNavigationContent(
//            homeScreenState = homeScreenState,
//            modifier = Modifier
//                .semantics { contentDescription = bottomBarContentDescription }
//        )
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun HomeScreenContent(
    itemList: List<Movie>,
    homeScreenNavType: BottomNavType,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        //cross fade animation when switching between layouts
        Crossfade(homeScreenNavType) { screen->
            Surface(color = MaterialTheme.colors.background) {
                when(screen) {
                    BottomNavType.HOME -> { HomeScreenDisplay(itemList = itemList) }
                    BottomNavType.WIDGETS -> { WidgetScreenDisplay(name = "Widget Screen") }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationContent(
    homeScreenState: MutableState<BottomNavType>,
    modifier: Modifier = Modifier,
) {
    var animate by remember { mutableStateOf(false) }

    BottomNavigation(modifier = Modifier.background(MaterialTheme.colors.onBackground)) {
        BottomNavigationItem(
            icon = { R.drawable.ic_baseline_home_24 },
//            icon = {
//                Icon(painter = painterResource(id = R.drawable.ic_baseline_favorite_24),
//                    contentDescription = "Home")
//            },
//            icon = {
//                Icon(
//                    imageVector = Icons.Filled.Favorite,
//                    contentDescription = "Fav",
//                    modifier = Modifier.padding(2.dp),
//                    tint = MaterialTheme.colors.background)
//            },
            selected = homeScreenState.value == BottomNavType.HOME,
            onClick = {
                homeScreenState.value = BottomNavType.HOME
                animate = false
            },
            label = { Text(text = stringResource(id = R.string.navigation_item_home)) }
        )
        BottomNavigationItem(
            icon = { R.drawable.ic_baseline_favorite_24 },
            selected = homeScreenState.value == BottomNavType.WIDGETS,
            onClick = {
                homeScreenState.value = BottomNavType.WIDGETS
                animate = false
            },
            label = { Text(text = stringResource(id = R.string.navigation_item_widgets)) }
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun HomeScreenDisplay(itemList: List<Movie>) {
    Column {
        TopAppBar(title = { Text(text = "Jet Movies") } )
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier.padding(10.dp)
        ) {
            items(items = itemList, itemContent = { eachItem->
                MovieListItem(eachItem)
            })
        }
    }
}

@Composable
fun WidgetScreenDisplay(name: String) {
    Text(text = name, modifier = Modifier.padding(20.dp, 20.dp), style = TextStyle(
        color = Color.DarkGray,
        fontSize = 30.sp,
        textAlign = TextAlign.Center
    ))
}

@ExperimentalMaterialApi
@Composable
fun MovieListItem(item : Movie) {
    val context = LocalContext.current
    val intent = Intent(context, DetailActivity::class.java)

    Box(modifier = Modifier
        .clickable(onClick = {
            startActivity(
                context,
                intent.putExtra("movieId", item.movieId.toString()),
                null
            )
        })
        .padding(10.dp) )
    {
        Image(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            painter = rememberCoilPainter(
                request = item.getImageUri()
            ),
            contentDescription = "Movie Image - ${item.movieName}"
        )
        Text(
            maxLines = 2,
            text = item.movieName,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black)
                    )
                ),
            style = TextStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}

