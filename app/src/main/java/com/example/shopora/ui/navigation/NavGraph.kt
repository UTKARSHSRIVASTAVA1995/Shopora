package com.example.shopora.ui.navigation

/**
 * @author Utkarsh Srivastava
 */

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Categories : Screen("categories", "Categories", Icons.Default.Category)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object ProductDetail : Screen("product_detail/{productId}", "Product Detail") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Categories,
    Screen.Search
)
