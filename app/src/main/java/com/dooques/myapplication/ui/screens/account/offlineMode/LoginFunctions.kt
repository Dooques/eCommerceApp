package com.dooques.myapplication.ui.screens.account.offlineMode

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import com.dooques.myapplication.model.UserLoginAuth
import com.dooques.myapplication.ui.screens.account.LoginAuthState
import com.dooques.myapplication.util.ACCOUNT_SCREEN_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun loginOnline(
    username: String,
    password: String,
    loginAuthState: LoginAuthState,
    onLogin: (UserLoginAuth) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit
) {
    Log.d(ACCOUNT_SCREEN_TAG, "\nAttempting to login user...")
    scope.launch {
        onLogin(UserLoginAuth(username, password))
        when (loginAuthState) {
            is LoginAuthState.Success -> {
                Log.d(
                    ACCOUNT_SCREEN_TAG,
                    "Login Successful, token returned: ${loginAuthState.authToken}"
                )
                snackbarHostState.showSnackbar(
                    "Login Successful:" +
                            loginAuthState.authToken.toString(),
                    duration = SnackbarDuration.Short
                )
                navigateUp()
            }

            is LoginAuthState.Error -> {
                Log.e(
                    ACCOUNT_SCREEN_TAG,
                    "Login encountered an error",
                    loginAuthState.e
                )
                snackbarHostState.showSnackbar(
                    "Login Error, please check your username and password",
                    duration = SnackbarDuration.Short
                )
                Log.d(
                    ACCOUNT_SCREEN_TAG,
                    "Checking offline data for user profiles..."
                )
            }
            else -> null
        }
    }
}

fun loginOffline(
    username: String,
    password: String,
    loginAuthStateOffline: LoginAuthState,
    onLoginOffline: (UserLoginAuth) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit
) {
    scope.launch {
        Log.d(ACCOUNT_SCREEN_TAG, "\nAttempting to login user in offline mode...")
        onLoginOffline(UserLoginAuth(username, password))
        when (loginAuthStateOffline) {
            is LoginAuthState.Success -> {
                Log.d(
                    ACCOUNT_SCREEN_TAG,
                    "Offline login successful, returning to account screen."
                )
                snackbarHostState.showSnackbar(
                    "Offline login successful, returning to account screen.",
                    duration = SnackbarDuration.Short
                )
                navigateUp()
            }

            is LoginAuthState.Error -> {
                Log.e(
                    ACCOUNT_SCREEN_TAG,
                    "Offline login unsuccessful, username or password do not match",
                    loginAuthStateOffline.e
                )
                snackbarHostState.showSnackbar(
                    "Offline login unsuccessful, please check username or password."
                )
            }

            else -> null
        }
    }
}