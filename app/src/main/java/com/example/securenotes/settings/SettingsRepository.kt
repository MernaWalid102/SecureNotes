package com.example.securenotes.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer,
    produceMigrations = { context -> listOf(PreferenceMigration.fromLegacy(context)) }
)

class SettingsRepository(private val context: Context) {

    val settingsFlow: Flow<Settings> = context.settingsDataStore.data

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.updateData { it.toBuilder().setDarkMode(enabled).build() }
    }

    suspend fun setFontScale(scale: Float) {
        context.settingsDataStore.updateData { it.toBuilder().setDefaultFontScale(scale).build() }
    }

    suspend fun setAutoSave(enabled: Boolean) {
        context.settingsDataStore.updateData { it.toBuilder().setAutoSave(enabled).build() }
    }

    fun uiPrefsFlow(): Flow<Pair<Boolean, Float>> =
        settingsFlow.map { it.darkMode to (if (it.defaultFontScale == 0f) 1f else it.defaultFontScale) }
}
