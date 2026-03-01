package com.snakeinfinity.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakeinfinity.game.presentation.navigation.SnakeNavGraph
import com.snakeinfinity.game.presentation.theme.DarkBackground
import com.snakeinfinity.game.presentation.theme.NeonGreen
import com.snakeinfinity.game.presentation.theme.NeonPurple
import com.snakeinfinity.game.presentation.theme.SnakeInfinityTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnakeInfinityTheme {
                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(2500)
                    showSplash = false
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    // Main content — appears when splash hides
                    AnimatedVisibility(
                        visible = !showSplash,
                        enter = fadeIn(animationSpec = tween(500))
                    ) {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            SnakeNavGraph()
                        }
                    }

                    // Splash screen — disappears after delay
                    AnimatedVisibility(
                        visible = showSplash,
                        enter = fadeIn(),
                        exit = fadeOut(animationSpec = tween(400))
                    ) {
                        SplashScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun SplashScreen() {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Logo scale + fade in
        scale.animateTo(1f, animationSpec = tween(800))
    }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(800))
    }
    LaunchedEffect(Unit) {
        // Text appears after logo
        delay(500)
        textAlpha.animateTo(1f, animationSpec = tween(600))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated snake logo
            Canvas(
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale.value)
                    .alpha(alpha.value)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2 - 8f

                // Glow
                drawCircle(
                    color = NeonGreen.copy(alpha = 0.3f),
                    radius = radius + 16f,
                    center = center
                )
                // Gradient fill
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonGreen.copy(alpha = 0.3f), Color.Transparent),
                        center = center,
                        radius = radius
                    ),
                    center = center
                )
                // Ring
                drawCircle(
                    color = NeonGreen,
                    radius = radius,
                    center = center,
                    style = Stroke(width = 3f)
                )
                // Snake body segments
                val segments = listOf(
                    Offset(center.x - 24f, center.y),
                    Offset(center.x, center.y),
                    Offset(center.x + 24f, center.y),
                    Offset(center.x + 24f, center.y - 24f)
                )
                segments.forEachIndexed { i, pos ->
                    drawCircle(
                        color = NeonGreen.copy(alpha = 1f - i * 0.15f),
                        radius = 10f,
                        center = pos
                    )
                }
                // Food
                drawCircle(
                    color = Color(0xFFFF4757),
                    radius = 8f,
                    center = Offset(center.x - 24f, center.y - 32f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Title text
            Text(
                text = "SNAKE",
                color = NeonGreen,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 8.sp,
                modifier = Modifier.alpha(textAlpha.value)
            )
            Text(
                text = "INFINITY",
                color = NeonPurple,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 12.sp,
                modifier = Modifier.alpha(textAlpha.value)
            )
        }
    }
}
