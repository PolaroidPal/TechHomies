package com.example.stepcounterapp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val PureBlack = Color(0xFF000000)
val PureWhite = Color(0xFFFFFFFF)
val NavyBlue = Color(0xFF29248A)
val DrawerColor = Color(0xFF3E1661)

val HomeGradient = listOf(PureBlack, NavyBlue, PureWhite)
val HomeColBackground = Brush.verticalGradient(colors = HomeGradient)
val HomeRowBackground = Brush.horizontalGradient(colors = HomeGradient)