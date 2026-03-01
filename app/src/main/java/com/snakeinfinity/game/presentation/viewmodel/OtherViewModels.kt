package com.snakeinfinity.game.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snakeinfinity.game.domain.model.GameSettings
import com.snakeinfinity.game.domain.model.LeaderboardEntry
import com.snakeinfinity.game.domain.usecase.ClearLeaderboardUseCase
import com.snakeinfinity.game.domain.usecase.GetLeaderboardUseCase
import com.snakeinfinity.game.domain.usecase.GetSettingsUseCase
import com.snakeinfinity.game.domain.usecase.SaveSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ── Leaderboard ──────────────────────────────────────────────────────────────

data class LeaderboardUiState(
    val entries: List<LeaderboardEntry> = emptyList(),
    val isLoading: Boolean = true
)

class LeaderboardViewModel(
    private val getLeaderboardUseCase: GetLeaderboardUseCase,
    private val clearLeaderboardUseCase: ClearLeaderboardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardUiState())
    val state: StateFlow<LeaderboardUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getLeaderboardUseCase().collect { entries ->
                _state.value = LeaderboardUiState(entries = entries, isLoading = false)
            }
        }
    }

    fun clearAll() {
        viewModelScope.launch { clearLeaderboardUseCase() }
    }
}

// ── Settings ─────────────────────────────────────────────────────────────────

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : ViewModel() {

    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            getSettingsUseCase().collect { _settings.value = it }
        }
    }

    fun updateSettings(settings: GameSettings) {
        viewModelScope.launch {
            saveSettingsUseCase(settings)
            _settings.value = settings
        }
    }
}
