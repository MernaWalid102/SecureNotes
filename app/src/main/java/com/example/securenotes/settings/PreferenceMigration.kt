package com.example.securenotes.settings

import android.content.Context
import androidx.datastore.migrations.SharedPreferencesView
import androidx.datastore.migrations.SharedPreferencesMigration

object PreferenceMigration {
    fun fromLegacy(context: Context): SharedPreferencesMigration<Settings> {
        return SharedPreferencesMigration(context, "legacy_prefs") { sp: SharedPreferencesView, current: Settings ->
            val dark = sp.getBoolean("darkMode", false)
            val scale = sp.getFloat("defaultFontScale", 1.0f)
            val autoSave = sp.getBoolean("autoSave", true)
            current.toBuilder()
                .setDarkMode(dark)
                .setDefaultFontScale(scale)
                .setAutoSave(autoSave)
                .build()
        }
    }
}
