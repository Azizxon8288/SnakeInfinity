package com.snakeinfinity.game.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.snakeinfinity.game.presentation.screen.GameScreen
import com.snakeinfinity.game.presentation.screen.HomeScreen
import com.snakeinfinity.game.presentation.screen.LeaderboardScreen
import com.snakeinfinity.game.presentation.screen.SettingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Game : Screen("game")
    object Leaderboard : Screen("leaderboard")
    object Settings : Screen("settings")
}

@Composable
fun SnakeNavGraph() {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding()
            .imePadding(),
        navController = navController, startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartGame = { navController.navigate(Screen.Game.route) },
                onLeaderboard = { navController.navigate(Screen.Leaderboard.route) },
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Game.route) {
            GameScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
