package com.example.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextSecondary

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show bottom nav on main screens
    val currentRoute = currentDestination?.route
    val showBottomNav = currentRoute?.contains("HomeRoute") == true ||
            currentRoute?.contains("BrainLibraryRoute") == true ||
            currentRoute?.contains("AiAssistantRoute") == true ||
            currentRoute?.contains("ProfileRoute") == true

    if (showBottomNav) {
        NavigationBar(
            containerColor = SurfaceDark,
            contentColor = Color.White
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = currentDestination?.hierarchy?.any { it.route?.contains("HomeRoute") == true } == true,
                onClick = {
                    navController.navigate(HomeRoute) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonBlue,
                    selectedTextColor = NeonBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = SurfaceVariantDark
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Folder, contentDescription = "Brain") },
                label = { Text("Brain") },
                selected = currentDestination?.hierarchy?.any { it.route?.contains("BrainLibraryRoute") == true } == true,
                onClick = {
                    navController.navigate(BrainLibraryRoute) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonBlue,
                    selectedTextColor = NeonBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = SurfaceVariantDark
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                label = { Text("Add") },
                selected = false,
                onClick = {
                    navController.navigate(CreateIdeaRoute)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonBlue,
                    selectedTextColor = NeonBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = SurfaceVariantDark
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI") },
                label = { Text("AI") },
                selected = currentDestination?.hierarchy?.any { it.route?.contains("AiAssistantRoute") == true } == true,
                onClick = {
                    navController.navigate(AiAssistantRoute) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonBlue,
                    selectedTextColor = NeonBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = SurfaceVariantDark
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = currentDestination?.hierarchy?.any { it.route?.contains("ProfileRoute") == true } == true,
                onClick = {
                    navController.navigate(ProfileRoute) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonBlue,
                    selectedTextColor = NeonBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = SurfaceVariantDark
                )
            )
        }
    }
}
