package com.dooques.myapplication.ui.navMap

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dooques.myapplication.R
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.UserProfile
import com.dooques.myapplication.ui.reusableComponents.NavigationIconButton
import com.dooques.myapplication.ui.screens.account.AccountScreen
import com.dooques.myapplication.ui.screens.account.AccountSignInScreen
import com.dooques.myapplication.ui.screens.account.AccountViewModel
import com.dooques.myapplication.ui.screens.home.HomeScreen
import com.dooques.myapplication.ui.screens.home.HomeViewModel
import com.dooques.myapplication.ui.screens.item.ItemScreen
import com.dooques.myapplication.ui.screens.item.ItemViewModel
import com.dooques.myapplication.ui.screens.selling.CreateListingScreen
import com.dooques.myapplication.ui.screens.selling.CreateListingViewModel
import com.dooques.myapplication.ui.screens.settings.SettingsScreen
import com.dooques.myapplication.ui.screens.settings.SettingsViewModel
import com.dooques.myapplication.util.NAV_TAG
import com.dooques.myapplication.util.UserDetailsTest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

enum class AppScreen(@StringRes title: Int) {
    Home(title = R.string.home),
    Search(title = R.string.search),
    Item(title = R.string.listing),
    Account(title = R.string.account),
    SignIn(title = R.string.sign_in),
    Inbox(title = R.string.inbox),
    Selling(title = R.string.selling),
    Settings(title = R.string.settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    var currentPageTitle by remember { mutableStateOf(AppScreen.Home.name) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val homeViewModel = koinInject<HomeViewModel>()
    val itemViewModel = koinInject<ItemViewModel>()
    val createListingViewModel = koinInject<CreateListingViewModel>()
    val accountViewModel = koinInject<AccountViewModel>()
    val settingsViewModel = koinInject<SettingsViewModel>()

    /* Product Flows */
    val fakeProductListUiState by homeViewModel.productListNetworkState.collectAsStateWithLifecycle()
    val fakeProductUiState by itemViewModel.productState.collectAsStateWithLifecycle()

    /* Account Flows */
    val userProfileList by accountViewModel.userList.collectAsStateWithLifecycle()
    val userProfile by accountViewModel.user.collectAsStateWithLifecycle()
    val loginAuthState by accountViewModel.loginAuthState.collectAsStateWithLifecycle()

    /* Account Datastore Flows */
    val userDetails by accountViewModel.userDetails.collectAsStateWithLifecycle()
    val signedInState by accountViewModel.signedInState.collectAsStateWithLifecycle()

    /* Offline Data */
    val offlineProducts = homeViewModel.getProductsFromJSON(context)
    var offlineProduct by remember { mutableStateOf(Product()) }
    val offlineUsers = accountViewModel.getOfflineUsers(context)
    val loginAuthStateOffline by accountViewModel.loginAuthStateOffline.collectAsStateWithLifecycle()

    /* Settings Flows */
    val offlineMode by settingsViewModel.offlineMode.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }

    Log.d(NAV_TAG, "\nLoading Nav Graph")
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    NavigationIconButton(Icons.Default.Home) {
                        navController.navigate(AppScreen.Home.name)
                    }
                    NavigationIconButton(Icons.Default.AccountBox) {
                        navController.navigate(AppScreen.Account.name)
                    }
                    NavigationIconButton(Icons.Default.Search) {
                        navController.navigate(AppScreen.Search.name)
                    }
                    NavigationIconButton(Icons.Default.Email) {
                    }
                    NavigationIconButton(Icons.Default.Discount) {
                        navController.navigate(AppScreen.Selling.name)
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(bottom = 40.dp)
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.SignIn.name,
            modifier = Modifier.padding(padding)
        ) {
            /* Home Screen */
            composable(AppScreen.Home.name) {
                HomeScreen(
                    offlineMode = offlineMode,
                    onProductClick = {
                        itemViewModel.getProduct(it.id)
                        offlineProduct = itemViewModel.getProductFromJSON(context, it.id)
                        navController.navigate(AppScreen.Item.name)
                    },
                    productListNetworkState = fakeProductListUiState,
                    offlineProducts = offlineProducts,
                    navigateUp = navController::navigateUp
                )
            }
            /* Search Screen */
            composable(AppScreen.Search.name) {
                UserDetailsTest(
                    userProfileList = userProfileList
                )
            }
            /* Item Screen */
            composable(AppScreen.Item.name) {
                currentPageTitle = AppScreen.Item.name
                ItemScreen(
                    offlineMode = offlineMode,
                    productUiState = fakeProductUiState,
                    productListUiState = fakeProductListUiState,
                    offlineProduct = offlineProduct,
                    offlineProducts = offlineProducts,
                    onProductClick = {
                        itemViewModel.getProduct(it.id)
                        navController.navigate(AppScreen.Item.name)
                    },
                    getOfflineProduct = { offlineProduct = itemViewModel.getProductFromJSON(context, it) },
                    getProduct = itemViewModel::getProduct,
                    navigateUp = navController::navigateUp

                )
            }
            /* Account Screen */
            composable(AppScreen.Account.name) {
                AccountScreen(
                    offlineMode = offlineMode,
                    profile = UserProfile(),
                    signedInState = signedInState,
                    onSignIn = { navController.navigate(AppScreen.SignIn.name) },
                    navigateUp = navController::navigateUp,
                    navigateTo = navController::navigate,
                )
            }
            composable(AppScreen.SignIn.name) {
                AccountSignInScreen(
                    offlineMode = offlineMode,
                    onLogin =  {
                        scope.launch {
                            accountViewModel.loginUser(it)
                        } },
                    onLoginOffline = {
                        scope.launch {
                            accountViewModel.signInOfflineUser(context, it)
                        } },
                    onCreateAccount = { scope.launch {
                        accountViewModel.createUserProfile(it)
                        snackBarHostState.showSnackbar("Account Creation Successful", duration = SnackbarDuration.Short)
                    } },
                    loginAuthState = loginAuthState,
                    loginAuthStateOffline = loginAuthStateOffline,
                    snackbarHostState = snackBarHostState,
                    scope = scope,
                    navigateUp = navController::navigateUp,
                )
            }
            /* Inbox Screen */
            composable(AppScreen.Inbox.name) {

            }
            /* Selling */
            composable(AppScreen.Selling.name) {
                currentPageTitle = AppScreen.Selling.name
                CreateListingScreen(
                    offlineMode = offlineMode,
                    createListingViewModel = createListingViewModel,
                    scope = scope,
                    navigateUp = { navController.navigate(AppScreen.Home.name) }
                )
            }

            /* Settings */
            composable(route = AppScreen.Settings.name) {
                SettingsScreen(
                    offlineModeState = offlineMode,
                    updateOfflineModeState = {
                        scope.launch {
                            settingsViewModel.updateOfflineState(!offlineMode)
                            snackBarHostState.showSnackbar(
                                "Offline Mode set to ${if (!offlineMode) "off" else "on"}.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    navigateUp = navController::navigateUp,
                    snackbarHostState = snackBarHostState,
                )
            }
        }
    }
}