
package com.example.securenotes.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.securenotes.settings.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

data class SettingsUiState(
    val darkMode: Boolean = false,
    val fontScale: Float = 1.0f,
    val autoSave: Boolean = true
)

class SettingsViewModel(
    private val repo: SettingsRepository
) : ViewModel() {

    val state: StateFlow<SettingsUiState> =
        repo.settingsFlow
            .map { s ->
                SettingsUiState(
                    darkMode = s.darkMode,
                    fontScale = if (s.defaultFontScale == 0f) 1f else s.defaultFontScale,
                    autoSave = s.autoSave
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setDarkMode(enabled: Boolean) = viewModelScope.launch { repo.setDarkMode(enabled) }
    fun setFontScale(scale: Float) = viewModelScope.launch { repo.setFontScale(scale) }
    fun setAutoSave(enabled: Boolean) = viewModelScope.launch { repo.setAutoSave(enabled) }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(SettingsRepository(context.applicationContext)) as T
        }
    }
}
