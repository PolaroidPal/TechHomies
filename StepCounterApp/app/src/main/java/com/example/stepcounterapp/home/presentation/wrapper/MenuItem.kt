package com.example.stepcounterapp.home.presentation.wrapper

import androidx.compose.ui.graphics.vector.ImageVector
import javax.annotation.concurrent.Immutable

@Immutable
data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
