package com.dooques.myapplication.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dooques.myapplication.ui.screens.account.UserProfileListNetworkState

@Composable
fun UserDetailsTest(
    userProfileList: UserProfileListNetworkState
) {
    when (userProfileList) {
        is UserProfileListNetworkState.Success -> {
            LazyColumn(Modifier.fillMaxSize().padding(32.dp)) {
                items(items = userProfileList.userList) { user ->
                    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(user.toString())
                    }
                }
            }
        }
        is UserProfileListNetworkState.Error ->
            Box(Modifier.fillMaxWidth()) {
                Text(userProfileList.e.toString())
            }
        else -> null
    }
}