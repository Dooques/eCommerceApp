package com.dooques.myapplication.ui.screens.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dooques.myapplication.data.datastore.UserDsRepository
import com.dooques.myapplication.data.network.FakeStoreRepository
import com.dooques.myapplication.model.UserProfile
import com.dooques.myapplication.util.ACCOUNT_VM_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userDsRepository: UserDsRepository,
    private val fakeStoreRepository: FakeStoreRepository
): ViewModel() {

    private val _currentUser = MutableStateFlow<UserProfileNetworkState>(
        UserProfileNetworkState.Success(UserProfile())
    )
    val currentUser: StateFlow<UserProfileNetworkState> = _currentUser

    private val _userList = MutableStateFlow<UserProfileListNetworkState>(
        UserProfileListNetworkState.Success(emptyList())
    )
    val userList: StateFlow<UserProfileListNetworkState> = _userList

    val username = userDsRepository.getUsername()
        .mapNotNull { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = String()
        )
    val email = userDsRepository.getEmail()
        .mapNotNull { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = String()
        )

    val signedInState = userDsRepository.getSignedInState()
        .mapNotNull { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = false
        )

    fun updateUserDetailsInDs(userProfile: UserProfileNetworkState) {
        when (userProfile) {
            is UserProfileNetworkState.Success ->
                viewModelScope.launch {
                    userDsRepository.updateUser(userProfile.user)
                }
            is UserProfileNetworkState.Error ->
                Log.d(ACCOUNT_VM_TAG, "Error getting user data from network...", userProfile.e)
        }
    }

    fun getUserListFromNetwork() {
        viewModelScope.launch {
            try {
                _userList.value = UserProfileListNetworkState.Success(fakeStoreRepository.getUserProfiles())
            } catch(e: Exception) {
                _userList.value = UserProfileListNetworkState.Error(e)
            }
        }
    }

    fun getUserProfileFromNetwork(id: Int) {
        viewModelScope.launch {
            try {
                _currentUser.value = UserProfileNetworkState.Success(fakeStoreRepository.getUserProfile(id))
            } catch(e: Exception) {
                _currentUser.value = UserProfileNetworkState.Error(e)
            }
        }
    }
}

sealed interface UserProfileListNetworkState {
    data class Success(val userList: List<UserProfile>): UserProfileListNetworkState
    data class Error(val e: Exception): UserProfileListNetworkState
}

sealed interface UserProfileNetworkState {
    data class Success(val user: UserProfile): UserProfileNetworkState
    data class Error(val e: Exception): UserProfileNetworkState
}