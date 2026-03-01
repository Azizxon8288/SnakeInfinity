package com.snakeinfinity.game.domain.usecase

import com.snakeinfinity.game.domain.model.GameSettings
import com.snakeinfinity.game.domain.model.LeaderboardEntry
import com.snakeinfinity.game.domain.repository.GameRepository
import com.snakeinfinity.game.domain.repository.LeaderboardRepository
import kotlinx.coroutines.flow.Flow

class GetHighScoreUseCase(private val repo: GameRepository) {
    operator fun invoke(): Flow<Int> = repo.getHighScore()
}

class SaveHighScoreUseCase(private val repo: GameRepository) {
    suspend operator fun invoke(score: Int) = repo.saveHighScore(score)
}

class GetSettingsUseCase(private val repo: GameRepository) {
    operator fun invoke(): Flow<GameSettings> = repo.getSettings()
}

class SaveSettingsUseCase(private val repo: GameRepository) {
    suspend operator fun invoke(settings: GameSettings) = repo.saveSettings(settings)
}

class GetLeaderboardUseCase(private val repo: LeaderboardRepository) {
    operator fun invoke(): Flow<List<LeaderboardEntry>> = repo.getLeaderboard()
}

class AddLeaderboardEntryUseCase(private val repo: LeaderboardRepository) {
    suspend operator fun invoke(playerName: String, score: Int, level: Int) =
        repo.addEntry(playerName, score, level)
}

class ClearLeaderboardUseCase(private val repo: LeaderboardRepository) {
    suspend operator fun invoke() = repo.clearLeaderboard()
}
