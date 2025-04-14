package com.dooques.myapplication.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.ProductListUiState

@Composable
fun DataTestPage(
    fakeProductDataState: ProductListUiState
) {
    when (fakeProductDataState) {
        is ProductListUiState.Loading ->
            LoadingScreen()
        is ProductListUiState.Success ->
            ResultsScreen(fakeProductDataState.products)
        is ProductListUiState.Error ->
            ErrorScreen()
    }
}

@Composable
fun ResultsScreen(
    results: List<Product>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(results) { result ->
            Column(modifier.fillMaxWidth()) {
                Row {
                    Text("ID:")
                    Text(result.id.toString())
                }
                Spacer(modifier.size(8.dp))
                Row {
                    Text("Title:")
                    Text(result.title)
                }
                Spacer(modifier.size(8.dp))
                Row {
                    Text("Price:")
                    Text(result.price.toString())
                }
                Spacer(modifier.size(8.dp))
                Row {
                    Text("Category: ")
                    Text(result.category)
                }
                Spacer(modifier.size(8.dp))
                Row {
                    Text("Rating: ")
                    Text(result.rating.toString())
                }
                Spacer(modifier.size(8.dp))
                Row {
                    Text("Image: ")
                    Text(result.image)
                }
                Spacer(modifier.size(8.dp))
                Row {
                    Text("Description: ")
                    Text(result.description)
                }
            }
            Spacer(modifier.padding(16.dp))
        }
    }
}

@Composable
fun LoadingScreen() {
    Column {
        Text("Waiting for data to load from server")
    }
}

@Composable
fun ErrorScreen() {
    Text("Something has gone wrong, check you are connected to the internet.")
}