package com.snakeinfinity.game.presentation.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakeinfinity.game.presentation.component.BannerAd
import com.snakeinfinity.game.presentation.theme.DarkBackground
import com.snakeinfinity.game.presentation.theme.NeonGreen
import com.snakeinfinity.game.presentation.theme.NeonGreenDim
import com.snakeinfinity.game.presentation.theme.NeonPurple
import com.snakeinfinity.game.presentation.viewmodel.GameViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onLeaderboard: () -> Unit,
    onSettings: () -> Unit,
    viewModel: GameViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Animated background grid
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSpacing = 40f
            val cols = (size.width / gridSpacing).toInt() + 1
            val rows = (size.height / gridSpacing).toInt() + 1
            for (i in 0..cols) {
                drawLine(
                    color = Color(0xFF1A1A2E),
                    start = Offset(i * gridSpacing, 0f),
                    end = Offset(i * gridSpacing, size.height),
                    strokeWidth = 1f
                )
            }
            for (j in 0..rows) {
                drawLine(
                    color = Color(0xFF1A1A2E),
                    start = Offset(0f, j * gridSpacing),
                    end = Offset(size.width, j * gridSpacing),
                    strokeWidth = 1f
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(48.dp))

            // Logo / Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(modifier = Modifier.size(120.dp)) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.width / 2 - 8f
                    drawCircle(
                        color = NeonGreen.copy(alpha = glowAlpha * 0.3f),
                        radius = radius + 16f,
                        center = center
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(NeonGreen.copy(alpha = 0.3f), Color.Transparent),
                            center = center,
                            radius = radius
                        ),
                        center = center
                    )
                    drawCircle(
                        color = NeonGreen.copy(alpha = glowAlpha),
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

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "SNAKE",
                    color = NeonGreen.copy(alpha = glowAlpha),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 8.sp
                )
                Text(
                    text = "INFINITY",
                    color = NeonPurple.copy(alpha = glowAlpha),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 12.sp
                )
                Text(
                    text = "Game Studio",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    letterSpacing = 4.sp
                )
            }

            // High Score
            if (state.highScore > 0) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonGreen.copy(alpha = 0.1f))
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "🏆  BEST: ${state.highScore}",
                        color = NeonGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onStartGame,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                    Text(
                        "  PLAY NOW",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onLeaderboard,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonGreenDim)
                    ) {
                        Icon(Icons.Default.Leaderboard, contentDescription = null)
                        Text("  Ranks", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = onSettings,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonGreenDim)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                        Text("  Settings", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Banner Ad at bottom
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Version 1.0.0",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                BannerAd()
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
