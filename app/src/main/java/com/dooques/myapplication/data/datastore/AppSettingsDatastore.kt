package com.dooques.myapplication.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.dooques.myapplication.data.datastore.AppSettingsRepository.Companion.TAG
import com.dooques.myapplication.data.datastore.AppSettingsRepository.Companion.offlineMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

interface AppSettingsRepository {

    suspend fun updateOfflineMode(boolean: Boolean)

    fun getOfflineModeState(): Flow<Boolean>

    companion object {
        const val TAG = "AppSettingsRepository"
        val offlineMode = booleanPreferencesKey("offline_mode")
    }
}

class AppSettingsDatastore(private val dataStore: DataStore<Preferences>): AppSettingsRepository {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

    override suspend fun updateOfflineMode(offlineModeState: Boolean) {
        dataStore.edit { preference ->
            preference[offlineMode] = offlineModeState
        }
    }

    override fun getOfflineModeState(): Flow<Boolean> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    Log.e(TAG, "Error reading offlineMode value in datastore...", it)
                } else {
                    throw it
                }
            }
            .map { preference ->
                preference[offlineMode] != false
            }

}