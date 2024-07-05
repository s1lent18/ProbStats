package com.example.kotlin_learning.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val darkmodebackground = Color(0xFFFDC500)
val lightmodebackground = Color(0xFF0077B6)
val darkmodefontcolor = Color.Black
val lightmodefontcolor = Color.White

val ColorScheme.focusedTextFieldText
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else Color.White

val ColorScheme.unfocusedTextFieldText
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFFDC500) else Color(0xFF0077B6)

val ColorScheme.textFieldContainer
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFFDC500) else Color(0xFF0077B6)

