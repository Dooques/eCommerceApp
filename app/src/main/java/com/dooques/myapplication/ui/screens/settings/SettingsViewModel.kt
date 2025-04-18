package com.dooques.myapplication.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dooques.myapplication.data.datastore.AppSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(private val appSettingsRepository: AppSettingsRepository): ViewModel() {

    val offlineMode = appSettingsRepository.getOfflineModeState()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = false
        )

    suspend fun updateOfflineState(offlineState: Boolean) =
        appSettingsRepository.updateOfflineMode(offlineState)
}