package com.snakeinfinity.game.domain.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

data class Position(val x: Int, val y: Int)

enum class Direction { UP, DOWN, LEFT, RIGHT }

data class SnakeState(
    val snake: List<Position> = listOf(Position(10, 10), Position(9, 10), Position(8, 10)),
    val food: Position = Position(5, 5),
    val direction: Direction = Direction.RIGHT,
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val isPaused: Boolean = false,
    val level: Int = 1,
    val highScore: Int = 0
)

/** A single entry in the local leaderboard (top 10 stored on-device). */
@Serializable
data class LeaderboardEntry(
    val rank: Int = 0,
    val playerName: String,
    val score: Int,
    val level: Int,
    val timestamp: Long = System.currentTimeMillis()
)

enum class GameSpeed(val intervalMs: Long, val label: String) {
    SLOW(220L, "Slow"),
    NORMAL(150L, "Normal"),
    FAST(100L, "Fast"),
    INSANE(60L, "Insane")
}

enum class Theme(val label: String) {
    NEON("Neon"),
    CLASSIC("Classic"),
    FOREST("Forest"),
    OCEAN("Ocean")
}

data class GameSettings(
    val speed: GameSpeed = GameSpeed.NORMAL,
    val theme: Theme = Theme.NEON,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val playerName: String = "Player"
)
