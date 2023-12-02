package fr.wonderfulappstudio.notifymehere.presentation.manager

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

// Create a property delegate for the Preferences DataStore
val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val NOTIFICATION_DISTANCE_KEY = floatPreferencesKey("notification_distance_key")
    }

    // Function to get the float value from DataStore
    val readNotificationDistance: Flow<Float> = dataStore.data
        .map { preferences ->
            // Return the float value or null if it doesn't exist
            preferences[NOTIFICATION_DISTANCE_KEY] ?: 500.0f
        }

    // Suspend function to write a float value to DataStore
    suspend fun writeNotificationDistance(value: Float) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_DISTANCE_KEY] = value
        }
    }
}
