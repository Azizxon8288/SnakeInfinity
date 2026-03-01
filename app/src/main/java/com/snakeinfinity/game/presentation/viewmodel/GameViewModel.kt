package com.snakeinfinity.game.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snakeinfinity.game.domain.model.Direction
import com.snakeinfinity.game.domain.model.GameSettings
import com.snakeinfinity.game.domain.model.Position
import com.snakeinfinity.game.domain.model.SnakeState
import com.snakeinfinity.game.domain.usecase.AddLeaderboardEntryUseCase
import com.snakeinfinity.game.domain.usecase.GetHighScoreUseCase
import com.snakeinfinity.game.domain.usecase.GetSettingsUseCase
import com.snakeinfinity.game.domain.usecase.SaveHighScoreUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

const val BOARD_SIZE = 20

class GameViewModel(
    private val getHighScoreUseCase: GetHighScoreUseCase,
    private val saveHighScoreUseCase: SaveHighScoreUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val addLeaderboardEntryUseCase: AddLeaderboardEntryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SnakeState())
    val state: StateFlow<SnakeState> = _state.asStateFlow()

    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    private val _showAd = MutableStateFlow(false)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    private var gameJob: Job? = null
    private var pendingDirection: Direction? = null
    private var gameCount = 0

    init {
        viewModelScope.launch {
            val highScore = getHighScoreUseCase().first()
            val settings  = getSettingsUseCase().first()
            _settings.value = settings
            _state.update { it.copy(highScore = highScore) }
        }
    }

    fun startGame() {
        gameJob?.cancel()
        val highScore = _state.value.highScore
        _state.value = SnakeState(
            food = randomFood(listOf(Position(10, 10), Position(9, 10), Position(8, 10))),
            highScore = highScore
        )
        pendingDirection = null
        gameJob = viewModelScope.launch { runGameLoop() }
    }

    private suspend fun runGameLoop() {
        while (!_state.value.isGameOver) {
            delay(_settings.value.speed.intervalMs)
            if (!_state.value.isPaused) tick()
        }
    }

    private fun tick() {
        val current   = _state.value
        if (current.isGameOver || current.isPaused) return

        val direction = pendingDirection ?: current.direction
        pendingDirection = null

        val head    = current.snake.first()
        val newHead = when (direction) {
            Direction.UP    -> Position(head.x, head.y - 1)
            Direction.DOWN  -> Position(head.x, head.y + 1)
            Direction.LEFT  -> Position(head.x - 1, head.y)
            Direction.RIGHT -> Position(head.x + 1, head.y)
        }

        // Wall collision
        if (newHead.x < 0 || newHead.x >= BOARD_SIZE || newHead.y < 0 || newHead.y >= BOARD_SIZE) {
            endGame(); return
        }
        // Self collision
        if (current.snake.drop(1).contains(newHead)) {
            endGame(); return
        }

        val ateFood  = newHead == current.food
        val newSnake = if (ateFood) listOf(newHead) + current.snake
                       else        listOf(newHead) + current.snake.dropLast(1)
        val newScore = if (ateFood) current.score + (10 * current.level) else current.score
        val newLevel = (newScore / 100) + 1
        val newFood  = if (ateFood) randomFood(newSnake) else current.food

        _state.update {
            it.copy(snake = newSnake, food = newFood, direction = direction,
                    score = newScore, level = newLevel)
        }
    }

    private fun endGame() {
        gameJob?.cancel()
        val current = _state.value
        viewModelScope.launch {
            saveHighScoreUseCase(current.score)
            // Save to local leaderboard
            addLeaderboardEntryUseCase(
                playerName = _settings.value.playerName,
                score      = current.score,
                level      = current.level
            )
            val newHigh = maxOf(current.score, current.highScore)
            _state.update { it.copy(isGameOver = true, highScore = newHigh) }
        }
        gameCount++
        // Show interstitial every 3 games (Play Store compliant frequency)
        if (gameCount % 3 == 0) _showAd.value = true
    }

    fun changeDirection(newDir: Direction) {
        val current = _state.value.direction
        val invalid = when (newDir) {
            Direction.UP    -> current == Direction.DOWN
            Direction.DOWN  -> current == Direction.UP
            Direction.LEFT  -> current == Direction.RIGHT
            Direction.RIGHT -> current == Direction.LEFT
        }
        if (!invalid) pendingDirection = newDir
    }

    fun togglePause() { _state.update { it.copy(isPaused = !it.isPaused) } }

    fun onAdShown() { _showAd.value = false }

    private fun randomFood(snake: List<Position>): Position {
        var pos: Position
        do { pos = Position(Random.nextInt(BOARD_SIZE), Random.nextInt(BOARD_SIZE)) }
        while (snake.contains(pos))
        return pos
    }

    override fun onCleared() { super.onCleared(); gameJob?.cancel() }
}
