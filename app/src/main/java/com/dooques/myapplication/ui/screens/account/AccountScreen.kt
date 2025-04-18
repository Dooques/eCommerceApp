package com.dooques.myapplication.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dooques.myapplication.R
import com.dooques.myapplication.model.UserProfile
import com.dooques.myapplication.ui.reusableComponents.NavigationIconButton
import com.dooques.myapplication.util.IconSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    offlineMode: Boolean,
    profile: UserProfile,
    signedInState: Boolean,
    onSignIn: () -> Unit,
    navigateUp: () -> Unit,
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_account)) },
                navigationIcon = {
                    NavigationIconButton(Icons.AutoMirrored.Default.ArrowBack) { navigateUp() }
                },
                actions = {
                    NavigationIconButton(Icons.Default.ShoppingCart) {}
                    NavigationIconButton(Icons.Default.Share) {}
                },
                windowInsets = WindowInsets(top = 40.dp)
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            item {
                if (signedInState) {
                    ProfileSummary(profile)
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .clickable(onClick = { onSignIn() })
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "",
                            modifier = modifier
                                .size(IconSize.Large.size.dp)
                        )
                        Text(
                            stringResource(R.string.sign_in),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = modifier
                                .padding(start = 16.dp)
                        )
                    }
                }
            }
            item {
                Revenue()
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AccountNavigator(
                        icon = Icons.Default.Favorite,
                        title = stringResource(R.string.favorites),
                        description = stringResource(R.string.take_another_look_at_your_favourite_items),
                        iconSize = IconSize.Small.size,
                        navigateTo = { navigateTo }
                    )
                    AccountNavigator(
                        icon =Icons.Default.Backpack,
                        title = stringResource(R.string.purchases),
                        description = stringResource(R.string.have_a_look_at_your_recent_purchases),
                        iconSize = IconSize.Small.size,
                        navigateTo = { navigateTo }
                    )
                    AccountNavigator(
                        icon = Icons.Default.Gavel,
                        title = stringResource(R.string.offers),
                        description = stringResource(R.string.your_offer_history),
                        iconSize = IconSize.Small.size,
                        navigateTo = { navigateTo }
                    )
                    HorizontalDivider(modifier.padding(vertical = 8.dp))
                }
            }
            item {
                Column(modifier.padding(horizontal = 16.dp)) {
                    Text("Account", style = MaterialTheme.typography.titleLarge, modifier = modifier.padding(start = 32.dp, top = 16.dp))
                    Row(modifier
                        .fillMaxWidth()
                        .clickable(onClick = { navigateTo("Support") })) {
                        Text("Help & Contact", modifier = modifier.padding(horizontal = 32.dp, vertical = 8.dp))
                    }
                    Row(modifier
                        .fillMaxWidth()
                        .clickable(onClick = { navigateTo("Settings") })) {
                        Text("Settings", modifier = modifier.padding(horizontal = 32.dp, vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileSummary(
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Image(
            painterResource(R.drawable.seller_image_placeholder),
            contentDescription = stringResource(R.string.profile_picture),
            Modifier
                .clip(CircleShape)
                .size(IconSize.Large.size.dp)
        )
        Column(modifier.padding(start = 16.dp)) {
            Text(profile.username, style = MaterialTheme.typography.bodyLarge, modifier = modifier.padding(bottom = 8.dp))
            Text(stringResource(R.string.selling_since_2025), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun Revenue(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(32.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Text("Total Revenue:", modifier.align(Alignment.CenterStart))
            Text("£100.00", modifier = modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
fun AccountNavigator(
    icon: ImageVector,
    title: String,
    description: String,
    iconSize: Int,
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp, vertical = 16.dp)
        .clickable(onClick = { navigateTo(title) })) {
        Box(
            modifier
                .size(iconSize.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = modifier.align(Alignment.Center)
            )
        }
        Column(modifier.padding(start = 16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
