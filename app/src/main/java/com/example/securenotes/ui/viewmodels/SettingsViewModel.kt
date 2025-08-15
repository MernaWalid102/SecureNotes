package com.example.securenotes.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.securenotes.settings.Settings
import com.example.securenotes.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val darkMode: Boolean = false,
    val fontScale: Float = 1.0f,
    val autoSave: Boolean = true
)

class SettingsViewModel(
    private val repo: SettingsRepository
) : ViewModel() {

    val state: StateFlow<SettingsUiState> = repo.settingsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, Settings.getDefaultInstance())
        .let { flow ->
            MutableStateFlow(
                SettingsUiState(
                    darkMode = flow.value.darkMode,
                    fontScale = if (flow.value.defaultFontScale == 0f) 1f else flow.value.defaultFontScale,
                    autoSave = flow.value.autoSave
                )
            )
        }

    fun refreshFromFlow() {
        viewModelScope.launch {
            repo.settingsFlow.collect { s ->
                val scale = if (s.defaultFontScale == 0f) 1f else s.defaultFontScale
                (state as MutableStateFlow).value = SettingsUiState(s.darkMode, scale, s.autoSave)
            }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { repo.setDarkMode(enabled) }
    }

    fun setFontScale(scale: Float) {
        viewModelScope.launch { repo.setFontScale(scale) }
    }

    fun setAutoSave(enabled: Boolean) {
        viewModelScope.launch { repo.setAutoSave(enabled) }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(SettingsRepository(context.applicationContext)).also {
                it.refreshFromFlow()
            } as T
        }
    }
}
