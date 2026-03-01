package com.snakeinfinity.game.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.snakeinfinity.game.data.repository.GameRepositoryImpl
import com.snakeinfinity.game.data.repository.LeaderboardRepositoryImpl
import com.snakeinfinity.game.domain.repository.GameRepository
import com.snakeinfinity.game.domain.repository.LeaderboardRepository
import com.snakeinfinity.game.domain.usecase.AddLeaderboardEntryUseCase
import com.snakeinfinity.game.domain.usecase.ClearLeaderboardUseCase
import com.snakeinfinity.game.domain.usecase.GetHighScoreUseCase
import com.snakeinfinity.game.domain.usecase.GetLeaderboardUseCase
import com.snakeinfinity.game.domain.usecase.GetSettingsUseCase
import com.snakeinfinity.game.domain.usecase.SaveHighScoreUseCase
import com.snakeinfinity.game.domain.usecase.SaveSettingsUseCase
import com.snakeinfinity.game.presentation.viewmodel.GameViewModel
import com.snakeinfinity.game.presentation.viewmodel.LeaderboardViewModel
import com.snakeinfinity.game.presentation.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "snake_prefs")

val appModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }
}

val repositoryModule = module {
    single<GameRepository>        { GameRepositoryImpl(get()) }
    single<LeaderboardRepository> { LeaderboardRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { GetHighScoreUseCase(get()) }
    factory { SaveHighScoreUseCase(get()) }
    factory { GetSettingsUseCase(get()) }
    factory { SaveSettingsUseCase(get()) }
    factory { GetLeaderboardUseCase(get()) }
    factory { AddLeaderboardEntryUseCase(get()) }
    factory { ClearLeaderboardUseCase(get()) }
}

val viewModelModule = module {
    viewModel { GameViewModel(get(), get(), get(), get()) }
    viewModel { LeaderboardViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
}
