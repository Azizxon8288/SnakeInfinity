package com.snakeinfinity.game.domain.repository

import com.snakeinfinity.game.domain.model.GameSettings
import com.snakeinfinity.game.domain.model.LeaderboardEntry
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getHighScore(): Flow<Int>
    suspend fun saveHighScore(score: Int)
    fun getSettings(): Flow<GameSettings>
    suspend fun saveSettings(settings: GameSettings)
}

/** Fully offline — stores top 10 scores locally via DataStore. */
interface LeaderboardRepository {
    fun getLeaderboard(): Flow<List<LeaderboardEntry>>
    suspend fun addEntry(playerName: String, score: Int, level: Int)
    suspend fun clearLeaderboard()
}
