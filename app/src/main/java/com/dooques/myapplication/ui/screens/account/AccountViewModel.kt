package com.dooques.myapplication.ui.screens.account

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dooques.myapplication.data.datastore.UserDsRepository
import com.dooques.myapplication.data.network.FakeStoreRepository
import com.dooques.myapplication.data.offline.OfflineRepository
import com.dooques.myapplication.model.AuthToken
import com.dooques.myapplication.model.UserLoginAuth
import com.dooques.myapplication.model.UserProfile
import com.dooques.myapplication.model.UserProfileDetails
import com.dooques.myapplication.util.ACCOUNT_VM_TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID

class AccountViewModel(
    private val userDsRepository: UserDsRepository,
    private val fakeStoreRepository: FakeStoreRepository,
    private val offlineRepository: OfflineRepository
): ViewModel() {

    private val _user = MutableStateFlow<UserProfileNetworkState>(UserProfileNetworkState.Loading)
    val user: StateFlow<UserProfileNetworkState> = _user

    private val _userList = MutableStateFlow<UserProfileListNetworkState>(UserProfileListNetworkState.Loading)
    val userList: StateFlow<UserProfileListNetworkState> = _userList

    private val _loginAuthState = MutableStateFlow<LoginAuthState>(LoginAuthState.Loading)
    val loginAuthState: StateFlow<LoginAuthState> = _loginAuthState

    private val _loginAuthStateOffline = MutableStateFlow<LoginAuthState>(LoginAuthState.Loading)
    val loginAuthStateOffline: StateFlow<LoginAuthState> = _loginAuthState

    val userDetails = MutableStateFlow<UserProfileDetails>(
        UserProfileDetails(
            username = userDsRepository.getUsername()
                .mapNotNull { it }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000L),
                    initialValue = String()
                ).value,
            email = userDsRepository.getEmail()
                .mapNotNull { it }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000L),
                    initialValue = String()
                ).value
        )
    )

    val signedInState = userDsRepository.getSignedInState()
        .mapNotNull { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = false
        )

    init {
        getUserListFromNetwork()
    }

    fun updateUserDetailsInDs(userProfile: UserProfileNetworkState) {
        when (userProfile) {
            is UserProfileNetworkState.Success ->
                viewModelScope.launch {
                    userDsRepository.updateUser(userProfile.user)
                }
            is UserProfileNetworkState.Error ->
                Log.d(ACCOUNT_VM_TAG, "Error getting user data from network...", userProfile.e)
            else -> null
        }
    }

    private fun getUserListFromNetwork() {
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
                _user.value = UserProfileNetworkState.Success(fakeStoreRepository.getUserProfile(id))
            } catch(e: Exception) {
                _user.value = UserProfileNetworkState.Error(e)
            }
        }
    }

    fun getOfflineUsers(context: Context) =
        viewModelScope.launch {
            try {
                UserProfileListOfflineState.Success(offlineRepository.returnUsers(context))
            } catch (e: Exception) {
                UserProfileListOfflineState.Error(e)
            }
        }

    suspend fun signInOfflineUser(context: Context, userLoginAuth: UserLoginAuth) {
        val userProfile = offlineRepository.returnUsers(context).first { it.username == userLoginAuth.username }
        try {
            if (userProfile.password == userLoginAuth.password) {
                val randomUUID = randomUUID()
                val authToken = AuthToken(token = randomUUID.toString())
                _loginAuthState.value = LoginAuthState.Success(authToken)
            } else {
                _loginAuthState.value = LoginAuthState.Success(AuthToken())
            }
        } catch (e: Exception) {
            _loginAuthState.value = LoginAuthState.Error(e)
        }
    }

    suspend fun loginUser(userLoginAuth: UserLoginAuth) {
        Log.d(ACCOUNT_VM_TAG, "\nMaking request to fake store api")
        try {
            _loginAuthState.value = LoginAuthState.Success(fakeStoreRepository.loginUser(userLoginAuth))
        } catch (e: Exception) {
            _loginAuthState.value = LoginAuthState.Error(e)
        }
    }

    suspend fun createUserProfile(user: UserProfile) {
        Log.d(ACCOUNT_VM_TAG, "\nMaking request to fake store api")
        fakeStoreRepository.postUserProfile(user)
    }
}

sealed interface UserProfileListNetworkState {
    data class Success(val userList: List<UserProfile>): UserProfileListNetworkState
    data class Error(val e: Exception): UserProfileListNetworkState
    object Loading: UserProfileListNetworkState
}

sealed interface UserProfileNetworkState {
    data class Success(val user: UserProfile): UserProfileNetworkState
    data class Error(val e: Exception): UserProfileNetworkState
    object Loading: UserProfileNetworkState
}

sealed interface LoginAuthState {
    data class Success(val authToken: AuthToken): LoginAuthState
    data class Error(val e: Exception): LoginAuthState
    object Loading: LoginAuthState
}

sealed interface UserProfileListOfflineState {
    data class Success(val userList: List<UserProfile>): UserProfileListOfflineState
    data class Error(val e: Exception): UserProfileListOfflineState
    object Loading: UserProfileListOfflineState
}