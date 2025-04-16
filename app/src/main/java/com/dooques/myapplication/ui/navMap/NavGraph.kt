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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dooques.myapplication.R
import com.dooques.myapplication.model.UserProfile
import com.dooques.myapplication.ui.reusableComponents.NavigationIconButton
import com.dooques.myapplication.ui.screens.account.AccountScreen
import com.dooques.myapplication.ui.screens.account.AccountViewModel
import com.dooques.myapplication.ui.screens.account.UserProfileListNetworkState
import com.dooques.myapplication.ui.screens.account.UserProfileNetworkState
import com.dooques.myapplication.ui.screens.home.HomeScreen
import com.dooques.myapplication.ui.screens.home.HomeViewModel
import com.dooques.myapplication.ui.screens.home.ProductListNetworkState
import com.dooques.myapplication.ui.screens.item.ItemScreen
import com.dooques.myapplication.ui.screens.item.ItemViewModel
import com.dooques.myapplication.ui.screens.item.ProductNetworkState
import com.dooques.myapplication.ui.screens.selling.CreateListingScreen
import com.dooques.myapplication.ui.screens.selling.CreateListingViewModel
import com.dooques.myapplication.util.NAV_TAG
import org.koin.compose.koinInject

enum class AppScreen(@StringRes title: Int) {
    Home(title = R.string.home),
    Search(title = R.string.search),
    Item(title = R.string.listing),
    Account(title = R.string.account),
    Inbox(title = R.string.inbox),
    Selling(title = R.string.selling),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    var currentPageTitle by remember { mutableStateOf(AppScreen.Home.name) }
    val scope = rememberCoroutineScope()

    val homeViewModel = koinInject<HomeViewModel>()
    val itemViewModel = koinInject<ItemViewModel>()
    val createListingViewModel = koinInject<CreateListingViewModel>()
    val accountViewModel = koinInject<AccountViewModel>()

    /* Product */
    val fakeProductListUiState by homeViewModel.productListNetworkState.collectAsStateWithLifecycle()
    val fakeProductUiState by itemViewModel.productState.collectAsStateWithLifecycle()
    /* Account */
    val userProfileList by accountViewModel.userList.collectAsStateWithLifecycle()
    val userProfile by accountViewModel.currentUser.collectAsStateWithLifecycle()
    val username by accountViewModel.username.collectAsStateWithLifecycle()
    val email by accountViewModel.email.collectAsStateWithLifecycle()
    val signedInState by accountViewModel.signedInState.collectAsStateWithLifecycle()

    when (fakeProductListUiState) {
        is ProductListNetworkState.Success ->
            Log.d(
                NAV_TAG, "\nFlows Collected: " +
                        "\nProduct List: ${(fakeProductListUiState as ProductListNetworkState.Success).products}" +
                        "\nProduct: ${(fakeProductUiState as ProductNetworkState.Success).product}" +
                        "\nUser List: ${(userProfileList as UserProfileListNetworkState.Success).userList}" +
                        "\nProduct: ${(userProfile as UserProfileNetworkState.Success).user}" +
                        "\nUsername: $username" +
                        "\nEmail: $email" +
                        "\nSigned In State: $signedInState"
            )
        is ProductListNetworkState.Error ->
            Log.d(
                NAV_TAG, "\nFlows Collected: " +
                        "\nProduct List: ${(fakeProductListUiState as ProductListNetworkState.Error).e}" +
                        "\nProduct: ${(fakeProductUiState as ProductNetworkState.Error).e}" +
                        "\nUser List: ${(userProfileList as UserProfileListNetworkState.Error).e}" +
                        "\nProduct: ${(userProfile as UserProfileNetworkState.Error).e}"
            )
    }

    Log.d(NAV_TAG, "\nLoading Nav Graph")
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    NavigationIconButton(Icons.Default.Home, { navController.navigate(AppScreen.Home.name) })
                    NavigationIconButton(Icons.Default.AccountBox, {navController.navigate(AppScreen.Account.name)})
                    NavigationIconButton(Icons.Default.Search, {})
                    NavigationIconButton(Icons.Default.Email, {})
                    NavigationIconButton(Icons.Default.Discount, {navController.navigate(AppScreen.Selling.name)})
                }
            }
        },
        contentWindowInsets = WindowInsets(bottom = 40.dp)
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Account.name,
            modifier = Modifier.padding(padding)
        ) {
            /* Home Screen */
            composable(route = AppScreen.Home.name) {
                HomeScreen(
                    onProductClick = {
                        itemViewModel.getProduct(it.id)
                        navController.navigate(AppScreen.Item.name)
                                     },
                    productListNetworkState = fakeProductListUiState,
                    navigateUp = navController::navigateUp
                )
            }
            /* Search Screen */
            composable(route = AppScreen.Search.name) {

            }
            /* Item Screen */
            composable(route = AppScreen.Item.name) {
                currentPageTitle = AppScreen.Item.name
                ItemScreen(
                    productUiState = fakeProductUiState,
                    productListUiState = fakeProductListUiState,
                    onProductClick = {
                        itemViewModel.getProduct(it.id)
                        navController.navigate(AppScreen.Item.name)
                    },
                    getProduct = itemViewModel::getProduct,
                    navigateUp = navController::navigateUp

                )
            }
            /* Account Screen */
            composable(route = AppScreen.Account.name) {
                AccountScreen(
                    profile = UserProfile(),
                    navigateUp = navController::navigateUp,
                    navigateTo = navController::navigate,
                )
            }
            /* Inbox Screen */
            composable(route = AppScreen.Inbox.name) {

            }
            /* Selling */
            composable(AppScreen.Selling.name) {
                currentPageTitle = AppScreen.Selling.name
                CreateListingScreen(
                    createListingViewModel = createListingViewModel,
                    scope = scope,
                    navigateUp = { navController.navigate(AppScreen.Home.name) }
                )
            }
        }
    }
}