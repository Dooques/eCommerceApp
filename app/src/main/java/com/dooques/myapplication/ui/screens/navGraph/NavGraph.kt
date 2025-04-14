package com.dooques.myapplication.ui.screens.navGraph

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dooques.myapplication.R
import com.dooques.myapplication.ui.screens.home.HomeScreen
import com.dooques.myapplication.ui.screens.home.HomeViewModel
import com.dooques.myapplication.ui.screens.listing.ListingScreen
import com.dooques.myapplication.util.NavigationIconButton
import org.koin.compose.koinInject

enum class AppScreen(@StringRes title: Int) {
    Home(title = R.string.home),
    Search(title = R.string.search),
    Item(title = R.string.listing),
    Account(title = R.string.account),
    Inbox(title = R.string.inbox)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    var currentPageTitle by remember { mutableStateOf(AppScreen.Home.name) }

    val homeViewModel = koinInject<HomeViewModel>()
    val fakeProductData = homeViewModel.productListUiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentPageTitle) },
                navigationIcon = {
                    if (currentPageTitle != "Home") {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Navigate Back"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "")
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar() {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    NavigationIconButton(Icons.Default.Home, { navController.navigate(AppScreen.Home.name) })
                    NavigationIconButton(Icons.Default.AccountBox, {})
                    NavigationIconButton(Icons.Default.Search, {})
                    NavigationIconButton(Icons.Default.Email, {})
                    NavigationIconButton(Icons.Default.Discount, {})
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Home.name,
            modifier = Modifier.padding(padding)
        ) {
            /* Home Screen */
            composable(route = AppScreen.Home.name) {
                HomeScreen(fakeProductData)
            }
            /* Search Screen */
            composable(route = AppScreen.Search.name) {

            }
            /* Item Screen */
            composable(route = AppScreen.Item.name) {
                currentPageTitle = AppScreen.Item.name
                ListingScreen()
            }
            /* Account Screen */
            composable(route = AppScreen.Account.name) {

            }
            /* Inbox Screen */
            composable(route = AppScreen.Inbox.name) { }
        }
    }
}