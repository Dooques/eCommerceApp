package com.dooques.myapplication.ui.reusableComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UiError(error: String, modifier: Modifier = Modifier) {
    Row(modifier.height(80.dp)) {
        Text(error)
    }
}