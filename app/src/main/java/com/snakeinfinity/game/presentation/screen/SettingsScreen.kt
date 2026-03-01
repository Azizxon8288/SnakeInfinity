package com.snakeinfinity.game.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakeinfinity.game.domain.model.GameSettings
import com.snakeinfinity.game.domain.model.GameSpeed
import com.snakeinfinity.game.domain.model.Theme
import com.snakeinfinity.game.presentation.component.BannerAd
import com.snakeinfinity.game.presentation.theme.DarkBackground
import com.snakeinfinity.game.presentation.theme.DarkCard
import com.snakeinfinity.game.presentation.theme.NeonGreen
import com.snakeinfinity.game.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚙️ Settings", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Game Speed Section
            SettingsSection(title = "Game Speed") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GameSpeed.entries.forEach { speed ->
                        SpeedChip(
                            label = speed.label,
                            isSelected = settings.speed == speed,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.updateSettings(settings.copy(speed = speed)) }
                        )
                    }
                }
            }

            // Theme Section
            SettingsSection(title = "Theme") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Theme.entries.forEach { theme ->
                        SpeedChip(
                            label = theme.label,
                            isSelected = settings.theme == theme,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.updateSettings(settings.copy(theme = theme)) }
                        )
                    }
                }
            }

            // Sound & Haptics Section
            SettingsSection(title = "Audio & Haptics") {
                SettingsToggle(
                    label = "Sound Effects",
                    checked = settings.soundEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(soundEnabled = it)) }
                )
                Spacer(Modifier.height(4.dp))
                SettingsToggle(
                    label = "Vibration",
                    checked = settings.vibrationEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(vibrationEnabled = it)) }
                )
            }

            // About Section
            SettingsSection(title = "About") {
                InfoRow("Version", "1.0.0")
                InfoRow("Developer", "Snake Infinity Game Studio")
                InfoRow("AdMob", "Enabled for monetization")
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .padding(16.dp)
    ) {
        Text(
            text = title.uppercase(),
            color = NeonGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun SpeedChip(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) NeonGreen.copy(alpha = 0.2f) else Color.Transparent)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) NeonGreen else Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) NeonGreen else Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun SettingsToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 16.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = NeonGreen)
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
