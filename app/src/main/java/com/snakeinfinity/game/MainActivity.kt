package com.snakeinfinity.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.snakeinfinity.game.presentation.navigation.SnakeNavGraph
import com.snakeinfinity.game.presentation.theme.SnakeInfinityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnakeInfinityTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SnakeNavGraph()
                }
            }
        }
    }
}
