package com.dooques.myapplication.ui.reusableComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun NavigationIconButton(icon: ImageVector, link: () -> Unit) {
    IconButton(onClick = link) {
        Icon(
            imageVector = icon,
            contentDescription = ""
        )
    }
}
