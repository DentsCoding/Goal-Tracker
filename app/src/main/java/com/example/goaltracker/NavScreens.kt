package com.example.goaltracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material.icons.sharp.Place
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavScreens(val route: String, val title: String, val icon: ImageVector) {
    object Goals : NavScreens("goals", "Goals", Icons.Sharp.DateRange) // rewarded ads material3 icon
    object Journey : NavScreens("journey", "Journey", Icons.Sharp.Place) // footprint material3 icon filled in
    object Reflection : NavScreens("reflection", "Reflection", Icons.Sharp.Info) // self improvement material3 icon
}