package com.dooques.myapplication.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dooques.myapplication.R
import com.dooques.myapplication.ui.reusableComponents.NavigationIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    offlineModeState: Boolean,
    updateOfflineModeState: (Boolean) -> Unit,
    navigateUp: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
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

        LazyColumn(modifier.fillMaxWidth().padding(padding)) {
            item {
                Column {
                    HorizontalDivider()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 80.dp, vertical = 16.dp)
                    ) {
                        Text("Offline Mode", style = MaterialTheme.typography.bodyLarge, modifier = modifier.align(Alignment.CenterStart))
                        Switch(
                            checked = offlineModeState,
                            onCheckedChange = {
                                updateOfflineModeState(offlineModeState)
                            },
                            modifier = modifier.size(20.dp).align(Alignment.CenterEnd)
                        )
                    }
                }
            }
        }
    }
}

