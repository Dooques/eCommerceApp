package com.dooques.myapplication.ui.screens.account

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.dooques.myapplication.R
import com.dooques.myapplication.model.UserLoginAuth
import com.dooques.myapplication.model.UserProfile
import com.dooques.myapplication.ui.reusableComponents.NavigationIconButton
import com.dooques.myapplication.ui.screens.account.offlineMode.loginOffline
import com.dooques.myapplication.ui.screens.account.offlineMode.loginOnline
import com.dooques.myapplication.util.ACCOUNT_SCREEN_TAG
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSignInScreen(
    offlineMode: Boolean,
    onCreateAccount: (UserProfile) -> Unit,
    onLogin: (UserLoginAuth) -> Unit,
    onLoginOffline: (UserLoginAuth) -> Unit,
    loginAuthState: LoginAuthState,
    loginAuthStateOffline: LoginAuthState,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_account)) },
                navigationIcon = {
                    NavigationIconButton(Icons.AutoMirrored.Default.ArrowBack, { navigateUp() })
                },
                actions = {
                    NavigationIconButton(Icons.Default.ShoppingCart, {})
                    NavigationIconButton(Icons.Default.Share, {})
                },
                windowInsets = WindowInsets(top = 40.dp)
            )
        },
    ) { padding ->

        var loginType by remember { mutableIntStateOf(0) }

        Box(
            modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier.align(Alignment.TopStart).padding(top = 16.dp, start = 16.dp)) {
                Text("Sign in or create an account", style = MaterialTheme.typography.titleLarge)
                when (loginType) {
                    1, 2 -> {
                        LoginForm(
                            offlineMode,
                            loginType,
                            onLogin,
                            onLoginOffline,
                            loginAuthState,
                            loginAuthStateOffline,
                            snackbarHostState,
                            scope,
                            onCreateAccount,
                            navigateUp,
                        )
                    }
                    else -> null
                }
            }
            Box(modifier.align(Alignment.BottomStart)) {
                LoginButtons(
                    changeLoginType = { loginType = it },
                )
            }
        }
    }
}

@Composable
fun LoginForm(
    offlineMode: Boolean,
    loginType: Int,
    onLogin: (UserLoginAuth) -> Unit,
    onLoginOffline: (UserLoginAuth) -> Unit,
    loginAuthState: LoginAuthState,
    loginAuthStateOffline: LoginAuthState,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onCreateAccount: (UserProfile) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val loginCondition = username.isNotEmpty() && password.isNotEmpty()
    val signInCondition = username.isNotEmpty() && email.isNotEmpty() &&
            password.isNotEmpty() && confirmPassword.isNotEmpty()

    Column {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            maxLines = 1,
            shape = RoundedCornerShape(100.dp),
            modifier = modifier.fillMaxWidth().padding(16.dp)
        )
        if (loginType == 2) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                maxLines = 1,
                shape = RoundedCornerShape(100.dp),
                modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            maxLines = 1,
            shape = RoundedCornerShape(100.dp),
            modifier = modifier.fillMaxWidth().padding(16.dp)
        )
        if (loginType == 2) {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                maxLines = 1,
                shape = RoundedCornerShape(100.dp),
                modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
        when (loginType) {
            1 -> OutlinedButton(
                onClick = {
                    if (!offlineMode)
                        loginOnline(
                            username = username,
                            password = password,
                            loginAuthState = loginAuthState,
                            onLogin = onLogin,
                            scope = scope,
                            snackbarHostState = snackbarHostState,
                            navigateUp = navigateUp
                        )
                    else
                        loginOffline(
                            username = username,
                            password = password,
                            loginAuthStateOffline = loginAuthStateOffline,
                            onLoginOffline = onLoginOffline,
                            scope = scope,
                            snackbarHostState = snackbarHostState,
                            navigateUp = navigateUp
                        )
                },
                enabled = loginCondition,
                modifier = modifier.fillMaxWidth().padding(16.dp)
            ) { Text("Login") }

            2 -> OutlinedButton(
                onClick = {
                    Log.d(ACCOUNT_SCREEN_TAG, "\nAttempting to create a new account...")
                    onCreateAccount(UserProfile(username = username, password = password, email = email))
                    navigateUp()
                },
                enabled = confirmPassword == password && signInCondition,
                modifier = modifier.fillMaxWidth().padding(32.dp)
            ) { Text("Create Account") }

            else -> null
        }
    }
}

@Composable
fun LoginButtons(
    changeLoginType: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
    modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
        FilledTonalButton(onClick = { changeLoginType(1) }, modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.Email, contentDescription = "", modifier = modifier.align(Alignment.CenterStart))
                Text("Sign in", modifier.align(Alignment.Center))
            }
        }
        FilledTonalButton(onClick = { changeLoginType(2) }, modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.Email, contentDescription = "", modifier = modifier.align(Alignment.CenterStart))
                Text("Create an Account", modifier.align(Alignment.Center))
            }
        }
        FilledTonalButton(onClick = { changeLoginType(3) }, modifier.fillMaxWidth()) {
            Box(modifier = modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "", modifier = modifier.align(Alignment.CenterStart))
                Text("Sign in with Google", modifier = modifier.align(Alignment.Center))
            }
        }
        FilledTonalButton(onClick = { changeLoginType(4) }, modifier.fillMaxWidth()) {
            Box(modifier = modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.Face, contentDescription = "", modifier = modifier.align(Alignment.CenterStart))
                Text("Sign in with Facebook", modifier = modifier.align(Alignment.Center))
            }
        }
    }

}