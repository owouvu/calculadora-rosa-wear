package com.example.aureliop2.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

val PinkBackground = Color(0xFFFFE4EC)
val PinkLight = Color(0xFFFFC1D6)
val PinkMedium = Color(0xFFFF9EBF)
val PinkOperator = Color(0xFFFF5C93)
val PinkDark = Color(0xFFB0215B)

private val PinkColors = Colors(
    primary = PinkOperator,
    primaryVariant = PinkDark,
    secondary = PinkMedium,
    secondaryVariant = PinkLight,
    background = PinkBackground,
    surface = PinkLight,
    onPrimary = Color.White,
    onSecondary = PinkDark,
    onBackground = PinkDark,
    onSurface = PinkDark,
    onSurfaceVariant = PinkDark,
)

@Composable
fun Aureliop2Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = PinkColors,
        content = content
    )
}
