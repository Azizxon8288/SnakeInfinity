package com.snakeinfinity.game.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.snakeinfinity.game.domain.model.GameSettings
import com.snakeinfinity.game.domain.model.GameSpeed
import com.snakeinfinity.game.domain.model.LeaderboardEntry
import com.snakeinfinity.game.domain.model.Theme
import com.snakeinfinity.game.domain.repository.GameRepository
import com.snakeinfinity.game.domain.repository.LeaderboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// ── Game Repository (high score + settings) ──────────────────────────────────

class GameRepositoryImpl(private val dataStore: DataStore<Preferences>) : GameRepository {

    companion object {
        val HIGH_SCORE_KEY    = intPreferencesKey("high_score")
        val SPEED_KEY         = stringPreferencesKey("game_speed")
        val THEME_KEY         = stringPreferencesKey("theme")
        val SOUND_KEY         = booleanPreferencesKey("sound_enabled")
        val VIBRATION_KEY     = booleanPreferencesKey("vibration_enabled")
        val PLAYER_NAME_KEY   = stringPreferencesKey("player_name")
    }

    override fun getHighScore(): Flow<Int> = dataStore.data.map { it[HIGH_SCORE_KEY] ?: 0 }

    override suspend fun saveHighScore(score: Int) {
        dataStore.edit { prefs ->
            val current = prefs[HIGH_SCORE_KEY] ?: 0
            if (score > current) prefs[HIGH_SCORE_KEY] = score
        }
    }

    override fun getSettings(): Flow<GameSettings> = dataStore.data.map { prefs ->
        GameSettings(
            speed         = runCatching { GameSpeed.valueOf(prefs[SPEED_KEY] ?: "") }.getOrDefault(GameSpeed.NORMAL),
            theme         = runCatching { Theme.valueOf(prefs[THEME_KEY] ?: "") }.getOrDefault(Theme.NEON),
            soundEnabled      = prefs[SOUND_KEY] ?: true,
            vibrationEnabled  = prefs[VIBRATION_KEY] ?: true,
            playerName        = prefs[PLAYER_NAME_KEY] ?: "Player"
        )
    }

    override suspend fun saveSettings(settings: GameSettings) {
        dataStore.edit { prefs ->
            prefs[SPEED_KEY]       = settings.speed.name
            prefs[THEME_KEY]       = settings.theme.name
            prefs[SOUND_KEY]       = settings.soundEnabled
            prefs[VIBRATION_KEY]   = settings.vibrationEnabled
            prefs[PLAYER_NAME_KEY] = settings.playerName
        }
    }
}

// ── Local Leaderboard Repository (top-10 stored as JSON in DataStore) ─────────

private val json = Json { ignoreUnknownKeys = true }

class LeaderboardRepositoryImpl(private val dataStore: DataStore<Preferences>) : LeaderboardRepository {

    companion object {
        val LEADERBOARD_KEY = stringPreferencesKey("leaderboard_json")
        const val MAX_ENTRIES = 10
    }

    override fun getLeaderboard(): Flow<List<LeaderboardEntry>> =
        dataStore.data.map { prefs ->
            val raw = prefs[LEADERBOARD_KEY] ?: return@map emptyList()
            runCatching {
                json.decodeFromString<List<LeaderboardEntry>>(raw)
            }.getOrDefault(emptyList())
        }

    override suspend fun addEntry(playerName: String, score: Int, level: Int) {
        dataStore.edit { prefs ->
            val raw = prefs[LEADERBOARD_KEY] ?: "[]"
            val current = runCatching {
                json.decodeFromString<List<LeaderboardEntry>>(raw)
            }.getOrDefault(emptyList()).toMutableList()

            current.add(LeaderboardEntry(playerName = playerName, score = score, level = level))

            // Keep top MAX_ENTRIES sorted by score desc, re-rank
            val ranked = current
                .sortedByDescending { it.score }
                .take(MAX_ENTRIES)
                .mapIndexed { i, e -> e.copy(rank = i + 1) }

            prefs[LEADERBOARD_KEY] = json.encodeToString(ranked)
        }
    }

    override suspend fun clearLeaderboard() {
        dataStore.edit { it.remove(LEADERBOARD_KEY) }
    }
}
