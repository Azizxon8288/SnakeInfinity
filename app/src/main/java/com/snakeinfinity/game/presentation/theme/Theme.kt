package com.snakeinfinity.game.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Neon Snake color palette
val NeonGreen = Color(0xFF00FF87)
val NeonGreenDim = Color(0xFF00C96D)
val NeonPurple = Color(0xFFBF00FF)
val NeonBlue = Color(0xFF00D4FF)
val DarkBackground = Color(0xFF0A0A0F)
val DarkSurface = Color(0xFF13131F)
val DarkCard = Color(0xFF1C1C2E)
val GridColor = Color(0xFF1A1A2E)
val FoodColor = Color(0xFFFF4757)
val FoodGlow = Color(0xFFFF6B81)
val GoldColor = Color(0xFFFFD700)
val SilverColor = Color(0xFFC0C0C0)
val BronzeColor = Color(0xFFCD7F32)

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Color.Black,
    secondary = NeonPurple,
    onSecondary = Color.White,
    tertiary = NeonBlue,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = DarkCard,
    error = FoodColor
)

@Composable
fun SnakeInfinityTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
