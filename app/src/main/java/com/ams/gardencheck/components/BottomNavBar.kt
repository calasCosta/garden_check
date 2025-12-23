package com.ams.gardencheck.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ams.gardencheck.R

@Composable
fun MainBottomNavigation(nav: NavController){
    // 1. Get the current navigation back stack entry state
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val defaultColors = NavigationBarItemDefaults.colors(
        // Set unselected elements to a neutral color
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray,
        // Ensure the indicator is transparent for unselected items
        indicatorColor = Color.Transparent
    )

    NavigationBar() {
        val routeFirst = "First"
        NavigationBarItem(
            selected = currentRoute == routeFirst,
            onClick = {nav.navigate(routeFirst)},
            label = {Text(text = "Home")},
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_home_50),
                    contentDescription = "Home"
                )
            },

            colors = defaultColors
        )

        val routeSecond = "Second"
        NavigationBarItem(
            selected = currentRoute == routeSecond,
            onClick = {nav.navigate(routeSecond)},
            label = {Text(text = "Scan")},
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_camera_50),
                    contentDescription = "Camera"
                )
            },
            colors = defaultColors
        )

        val routeThird = "Third"
        NavigationBarItem(
            selected = currentRoute == routeThird,
            onClick = {nav.navigate(routeThird)},
            label = {Text(text = "Profile")},
            icon = {Icon(imageVector = Icons.Default.Person, contentDescription = "")},
            colors = defaultColors
        )
    }
}