package com.example.securenotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.securenotes.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory(context))
    val state by vm.state.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark mode", modifier = Modifier.weight(1f))
            Switch(checked = state.darkMode, onCheckedChange = { vm.setDarkMode(it) })
        }
        Column {
            Text("Font scale: ${"%.2f".format(state.fontScale)}")
            Slider(
                value = state.fontScale,
                onValueChange = { vm.setFontScale(it) },
                valueRange = 0.8f..1.6f,
                steps = 8
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Auto save", modifier = Modifier.weight(1f))
            Switch(checked = state.autoSave, onCheckedChange = { vm.setAutoSave(it) })
        }

        TextButton(onClick = onNavigateBack) { Text("Back") }
    }
}
