package com.dooques.myapplication.util

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun NavigationIconButton(icon: ImageVector, link: () -> Unit) {
    IconButton(onClick = link) {
        Icon(
            imageVector = icon,
            contentDescription = ""
        )
    }
}