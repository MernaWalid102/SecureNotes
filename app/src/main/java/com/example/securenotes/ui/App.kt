package com.example.securenotes.ui

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.securenotes.ui.screens.NoteDetailScreen
import com.example.securenotes.ui.screens.NotesListScreen
import com.example.securenotes.ui.screens.SettingsScreen
import com.example.securenotes.ui.theme.SecureNotesTheme
import com.example.securenotes.ui.viewmodels.SettingsViewModel

// App.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Seed BEFORE DataStore is first created (no LaunchedEffect)
    seedLegacyPrefsIfNeeded(context)

    val settingsVm: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory(context))
    val settings by settingsVm.state.collectAsStateWithLifecycle()

    SecureNotesTheme(darkTheme = settings.darkMode, fontScale = settings.fontScale) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Secure Notes") },
                    actions = {
                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            }
        ) { inner ->
            NavHost(
                navController = navController,
                startDestination = "notes",
                modifier = Modifier.padding(inner)
            ) {
                composable("notes") {
                    NotesListScreen(
                        onAdd = { navController.navigate("detail/-1") },
                        onOpen = { id -> navController.navigate("detail/$id") }
                    )
                }
                composable(
                    route = "detail/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.LongType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getLong("id") ?: -1L
                    NoteDetailScreen(
                        noteId = if (id == -1L) null else id,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable("settings") {
                    SettingsScreen(onNavigateBack = { navController.popBackStack() })
                }
            }
        }
    }
}

private fun seedLegacyPrefsIfNeeded(context: Context) {
    val sp = context.getSharedPreferences("legacy_prefs", Context.MODE_PRIVATE)
    if (!sp.contains("seeded")) {
        // Use commit() so it's written before DataStore migration runs
        sp.edit()
            .putBoolean("darkMode", false)
            .putFloat("defaultFontScale", 1.0f)
            .putBoolean("autoSave", true)
            .putBoolean("seeded", true)
            .commit()
    }
}
