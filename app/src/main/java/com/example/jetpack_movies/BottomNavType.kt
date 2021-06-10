package com.example.jetpack_movies

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.*

enum class BottomNavType {
    HOME,
    WIDGETS
}

sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : BottomNavigationScreens("Home", R.string.navigation_item_home, Icons.Filled.Terrain)
    object Widgets : BottomNavigationScreens("Widgets", R.string.navigation_item_widgets, Icons.Filled.FoodBank)
}