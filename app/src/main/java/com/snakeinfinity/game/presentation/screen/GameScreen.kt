package com.snakeinfinity.game.presentation.screen

import android.app.Activity
import androidx.compose.ui.draw.scale
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakeinfinity.game.domain.model.Direction
import com.snakeinfinity.game.domain.model.Position
import com.snakeinfinity.game.presentation.component.BannerAd
import com.snakeinfinity.game.presentation.component.loadAndShowInterstitial
import com.snakeinfinity.game.presentation.theme.DarkBackground
import com.snakeinfinity.game.presentation.theme.FoodColor
import com.snakeinfinity.game.presentation.theme.GridColor
import com.snakeinfinity.game.presentation.theme.NeonGreen
import com.snakeinfinity.game.presentation.theme.NeonPurple
import com.snakeinfinity.game.presentation.viewmodel.BOARD_SIZE
import com.snakeinfinity.game.presentation.viewmodel.GameViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs

@Composable
fun GameScreen(
    onBack: () -> Unit,
    viewModel: GameViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val showAd by viewModel.showAd.collectAsState()
    val context = LocalContext.current
    var hasStarted by remember { mutableStateOf(false) }
    
    // We can't easily trigger targetValue change without another state, 
    // but we can use LaunchedEffect on state.score
    var lastScore by remember { mutableStateOf(0) }
    val scoreAnimationTriggerState = remember { mutableStateOf(1f) }
    val animatedScoreScale by animateFloatAsState(
        targetValue = scoreAnimationTriggerState.value,
        animationSpec = tween(200),
        finishedListener = { scoreAnimationTriggerState.value = 1f },
        label = "score_scale"
    )

    LaunchedEffect(state.score) {
        if (state.score > lastScore) {
            scoreAnimationTriggerState.value = 1.3f
        }
        lastScore = state.score
    }

    // Show interstitial ad when triggered
    LaunchedEffect(showAd) {
        if (showAd) {
            loadAndShowInterstitial(context as Activity) {
                viewModel.onAdShown()
            }
        }
    }

    // Auto-start game
    LaunchedEffect(Unit) {
        if (!hasStarted) {
            hasStarted = true
            viewModel.startGame()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NeonGreen
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "SCORE",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        letterSpacing = 2.sp
                    )
// ...
                    Text(
                        "${state.score}",
                        color = NeonGreen,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.scale(animatedScoreScale),
                        style = TextStyle(
                            shadow = Shadow(color = NeonGreen, blurRadius = 12f)
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "BEST",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        letterSpacing = 2.sp
                    )
                    Text(
                        "${state.highScore}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "LVL",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        letterSpacing = 2.sp
                    )
                    Text(
                        "${state.level}",
                        color = NeonPurple,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { viewModel.togglePause() }) {
                    Icon(
                        if (state.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (state.isPaused) "Resume" else "Pause",
                        tint = NeonGreen
                    )
                }
            }

            // Game Board with swipe gesture
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            val (dx, dy) = dragAmount
                            if (abs(dx) > abs(dy)) {
                                viewModel.changeDirection(if (dx > 0) Direction.RIGHT else Direction.LEFT)
                            } else {
                                viewModel.changeDirection(if (dy > 0) Direction.DOWN else Direction.UP)
                            }
                        }
                    }
            ) {
                GameBoard(
                    snake = state.snake,
                    food = state.food,
                    extraFood = state.extraFood,
                    extraFoodTicksLeft = state.extraFoodTicksLeft,
                    maxExtraFoodTicks = state.maxExtraFoodTicks
                )

                // Pause overlay
                this@Column.AnimatedVisibility(
                    visible = state.isPaused && !state.isGameOver,
                    enter = fadeIn(),
                    exit = fadeOut()
                )
            //                if (state.isPaused && !state.isGameOver)
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⏸", fontSize = 48.sp)
                            Text(
                                "PAUSED",
                                color = NeonGreen,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 6.sp
                            )
                        }
                    }
                }

                // Game Over overlay
                this@Column.AnimatedVisibility(
                    visible = state.isGameOver,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut()
                )
//                if (state.isGameOver)
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text("💀", fontSize = 52.sp)
                            Text(
                                "GAME OVER",
                                color = FoodColor,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 4.sp
                            )
                            Text(
                                "Score: ${state.score}",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (state.score >= state.highScore && state.score > 0) {
                                Text(
                                    "🏆 New High Score!",
                                    color = NeonGreen,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = { viewModel.startGame() },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(0.7f)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                                Text(
                                    "  PLAY AGAIN",
                                    color = Color.Black,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // D-Pad Controls
            DPad(onDirection = { viewModel.changeDirection(it) })

            Spacer(Modifier.weight(1f))

            // Banner Ad
            BannerAd()
            Spacer(Modifier.height(8.dp))
        }
    }
}

// ... existing imports ...
@Composable
private fun GameBoard(
    snake: List<Position>, 
    food: Position,
    extraFood: Position? = null,
    extraFoodTicksLeft: Int = 0,
    maxExtraFoodTicks: Int = 0
) {
    val infiniteTransition = rememberInfiniteTransition(label = "food_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val extraPulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(300),
            repeatMode = RepeatMode.Reverse
        ),
        label = "extra_pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellSize = size.width / BOARD_SIZE
        val padding = cellSize * 0.08f
        val cellInner = cellSize - padding * 2

        // Grid
        for (x in 0 until BOARD_SIZE) {
            for (y in 0 until BOARD_SIZE) {
                drawRoundRect(
                    color = GridColor,
                    topLeft = Offset(x * cellSize + padding, y * cellSize + padding),
                    size = Size(cellInner, cellInner),
                    cornerRadius = CornerRadius(2f)
                )
            }
        }

        // Snake
        snake.forEachIndexed { index, pos ->
            val alpha = if (index == 0) 1f else 1f - (index.toFloat() / snake.size * 0.5f)
            val color = if (index == 0) NeonGreen else NeonGreen.copy(alpha = alpha)
            drawRoundRect(
                color = color,
                topLeft = Offset(pos.x * cellSize + padding, pos.y * cellSize + padding),
                size = Size(cellInner, cellInner),
                cornerRadius = CornerRadius(if (index == 0) cellSize * 0.3f else cellSize * 0.15f)
            )
            // Glow effect on head
            if (index == 0) {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        asFrameworkPaint().apply {
                            isAntiAlias = true
//                            color = android.graphics.Color.TRANSPARENT
                            setShadowLayer(
                                cellSize * 0.5f,
                                0f,
                                0f,
                                NeonGreen.copy(alpha = 0.6f).toArgb()
                            )
                        }
                    }
                    canvas.drawRoundRect(
                        left = pos.x * cellSize + padding,
                        top = pos.y * cellSize + padding,
                        right = pos.x * cellSize + padding + cellInner,
                        bottom = pos.y * cellSize + padding + cellInner,
                        radiusX = cellSize * 0.3f,
                        radiusY = cellSize * 0.3f,
                        paint = paint
                    )
                }
            }
        }

        // Food with glow
        val foodX = food.x * cellSize
        val foodY = food.y * cellSize
        val foodCenter = Offset(foodX + cellSize / 2, foodY + cellSize / 2)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(FoodColor, FoodColor.copy(alpha = 0f)),
                center = foodCenter,
                radius = cellSize * 0.8f * pulseScale
            ),
            center = foodCenter,
            radius = cellSize * 0.8f * pulseScale
        )
        drawCircle(
            color = FoodColor,
            center = foodCenter,
            radius = cellSize * 0.38f * pulseScale
        )

        // Extra Food
        extraFood?.let { pos ->
            val exX = pos.x * cellSize
            val exY = pos.y * cellSize
            val exCenter = Offset(exX + cellSize / 2, exY + cellSize / 2)
            
            // Outer glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonPurple, Color.Transparent),
                    center = exCenter,
                    radius = cellSize * 1.2f * extraPulseScale
                ),
                center = exCenter,
                radius = cellSize * 1.2f * extraPulseScale
            )
            
            // Special food item
            drawCircle(
                color = NeonPurple,
                center = exCenter,
                radius = cellSize * 0.45f * extraPulseScale
            )
            
            // Timer indicator (arc around the extra food)
            val sweepAngle = if (maxExtraFoodTicks > 0) {
                (extraFoodTicksLeft.toFloat() / maxExtraFoodTicks.toFloat()) * 360f
            } else 0f
            
            drawArc(
                color = Color.White.copy(alpha = 0.7f),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(exCenter.x - cellSize*0.6f, exCenter.y - cellSize*0.6f),
                size = Size(cellSize*1.2f, cellSize*1.2f),
                style = Stroke(width = 4f)
            )
        }
    }
}

@Composable
private fun DPad(onDirection: (Direction) -> Unit) {
    val btnSize = 52.dp
    val btnColor = Color(0xFF1C1C2E)
    val iconColor = NeonGreen

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = { onDirection(Direction.UP) },
            modifier = Modifier
                .size(btnSize)
                .clip(CircleShape)
                .background(btnColor)
        ) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = iconColor)
        }
        Row {
            IconButton(
                onClick = { onDirection(Direction.LEFT) },
                modifier = Modifier
                    .size(btnSize)
                    .clip(CircleShape)
                    .background(btnColor)
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Left", tint = iconColor)
            }
            Spacer(Modifier.size(btnSize))
            IconButton(
                onClick = { onDirection(Direction.RIGHT) },
                modifier = Modifier
                    .size(btnSize)
                    .clip(CircleShape)
                    .background(btnColor)
            ) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Right",
                    tint = iconColor
                )
            }
        }
        IconButton(
            onClick = { onDirection(Direction.DOWN) },
            modifier = Modifier
                .size(btnSize)
                .clip(CircleShape)
                .background(btnColor)
        ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = iconColor)
        }
    }
}
