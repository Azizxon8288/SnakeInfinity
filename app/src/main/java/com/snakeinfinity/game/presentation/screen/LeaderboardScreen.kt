package com.snakeinfinity.game.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakeinfinity.game.domain.model.LeaderboardEntry
import com.snakeinfinity.game.presentation.component.BannerAd
import com.snakeinfinity.game.presentation.theme.BronzeColor
import com.snakeinfinity.game.presentation.theme.DarkBackground
import com.snakeinfinity.game.presentation.theme.DarkCard
import com.snakeinfinity.game.presentation.theme.FoodColor
import com.snakeinfinity.game.presentation.theme.GoldColor
import com.snakeinfinity.game.presentation.theme.NeonGreen
import com.snakeinfinity.game.presentation.theme.NeonPurple
import com.snakeinfinity.game.presentation.theme.SilverColor
import com.snakeinfinity.game.presentation.viewmodel.LeaderboardViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    viewModel: LeaderboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Leaderboard?", color = Color.White) },
            text  = { Text("All local scores will be deleted. This cannot be undone.", color = Color.White.copy(alpha = 0.7f)) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearAll(); showClearDialog = false }) {
                    Text("Clear", color = FoodColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = NeonGreen)
                }
            },
            containerColor = DarkCard
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏆  Best Scores", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                    }
                },
                actions = {
                    if (state.entries.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear", tint = FoodColor.copy(alpha = 0.8f))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        bottomBar = {
            Column {
                BannerAd()
                Spacer(Modifier.height(8.dp))
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.entries.isEmpty() && !state.isLoading) {
                // Empty state
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("🐍", fontSize = 64.sp)
                    Text(
                        "No scores yet!",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Play a game to appear\non the leaderboard.",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(Modifier.height(8.dp)) }
                    itemsIndexed(state.entries) { index, entry ->
                        LeaderboardRow(entry = entry, index = index)
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(entry: LeaderboardEntry, index: Int) {
    val rankColor = when (index) {
        0 -> GoldColor
        1 -> SilverColor
        2 -> BronzeColor
        else -> Color.White.copy(alpha = 0.6f)
    }
    val rankLabel = when (index) {
        0 -> "🥇"; 1 -> "🥈"; 2 -> "🥉"
        else -> "#${entry.rank}"
    }
    val dateStr = remember(entry.timestamp) {
        SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(entry.timestamp))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(rankColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rankLabel,
                fontSize = if (index < 3) 20.sp else 13.sp,
                color = rankColor,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
        ) {
            Text(text = entry.playerName, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Lv ${entry.level}", color = NeonPurple, fontSize = 12.sp)
                Text(text = "·", color = Color.White.copy(alpha = 0.3f), fontSize = 12.sp)
                Text(text = dateStr, color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
            }
        }

        Text(
            text = "${entry.score}",
            color = NeonGreen,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
        )
    }
}
