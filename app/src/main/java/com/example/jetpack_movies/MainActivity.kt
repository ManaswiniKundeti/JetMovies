package com.example.jetpack_movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import java.io.Serializable

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val viewModel: MainActivityViewModel by viewModels()

    private val viewmodelFactory by lazy { MainActivityViewModelFactory(this) }
    private val viewModel: MainActivityViewModel by viewModels {
        viewmodelFactory
    }

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

@Composable
fun Greeting(name: String) {
    Text(text = name, modifier = Modifier.padding(20.dp, 20.dp), style = TextStyle(
        color = Color.DarkGray,
        fontSize = 30.sp,
        textAlign = TextAlign.Center
    ))
}

@ExperimentalMaterialApi
@Composable
fun HomeScreenDisplay(itemList: List<Movie>) {
    val context = LocalContext.current
    val intent = Intent(context, DetailActivity::class.java)

    Column() {
        Text(
            text = "Jet Movies",
            modifier = Modifier.padding(20.dp, 20.dp),
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 30.sp,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn() {
            items(items = itemList, itemContent = { eachItem->
                val index = itemList.indexOf(eachItem)
                Row(modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable(onClick = {
                        startActivity(context, intent.putExtra("movieId", eachItem.movieId.toString()), null)
                    }),
                    content = {
                        Card(
                            shape = RoundedCornerShape(4.dp),
                            backgroundColor = Color.LightGray,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(16.dp),
                        ) {
                            MoviePoster(eachItem)
                        }
                    })
            })
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MoviePoster(item : Movie, modifier: Modifier = Modifier) {
    ListItem(text = {
        Text(
            text = item.movieName,
            modifier = Modifier.padding(16.dp, 16.dp),
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )
        )}, icon = {
        Image(
            painter = rememberCoilPainter(
                request = item.getImageUri()
            ),
            contentDescription = "Movie Image - ${item.movieName}"
        )
    })
}

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

@Composable
fun BottomNavigationContent(
    homeScreenState: MutableState<BottomNavType>,
    modifier: Modifier = Modifier
) {
    var animate by remember { mutableStateOf(false) }

    BottomNavigation(modifier = modifier) {
        BottomNavigationItem(
            icon =
            { Icon(imageVector = Icons.Filled.Home, stringResource(id = R.string.navigation_item_home)) },
            selected = homeScreenState.value == BottomNavType.HOME,
            onClick = {
                homeScreenState.value = BottomNavType.HOME
                animate = false
            },
            label = { Text(text = stringResource(id = R.string.navigation_item_home)) }
        )
        BottomNavigationItem(
            icon = { Icon(imageVector = Icons.Filled.Menu, stringResource(id = R.string.navigation_item_widgets)) },
            selected = homeScreenState.value == BottomNavType.WIDGETS,
            onClick = {
                homeScreenState.value = BottomNavType.WIDGETS
                animate = false
            },
            label = { Text(text = stringResource(id = R.string.navigation_item_widgets)) }
        )
    }
}

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
                    BottomNavType.WIDGETS -> { Greeting(name = "Widget Screen") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    val sampleItemList = List<Movie>(
//        "January",
//        "February",
//        "March",
//    )
//    ComposeTheme {
//        MainAppContent(sampleItemList)
//    }
}

