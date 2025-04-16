package com.dooques.myapplication.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dooques.myapplication.R
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.ui.reusableComponents.NavigationIconButton
import com.dooques.myapplication.ui.reusableComponents.UiError
import com.dooques.myapplication.util.HOME_SCREEN_TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productListNetworkState: ProductListNetworkState,
    onProductClick: (Product) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d(HOME_SCREEN_TAG, "\nLoading HomeScreen")
    val categoryLabels = listOf(
        "Selling", "Saved", "Local", "Explore (New)", "Fashion", "Motors", "Home & Garden",
        "Refurbished", "Sneakers", "Handbags", "Watches", "Categories", "Deals"
    )

    val productCategories = listOf("Men's Clothing", "jewelery", "electronics", "women's clothing")
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home)) },
                actions = {
                    NavigationIconButton(Icons.Default.ShoppingCart, {})
                    NavigationIconButton(Icons.Default.Share, {})
                },
                windowInsets = WindowInsets(top = 40.dp)
            )
        },
    ) { padding ->
        Column(modifier = modifier.fillMaxSize().padding(padding)) {
            Log.d(HOME_SCREEN_TAG, "Loading Search Bar")
            SearchBar()
            Log.d(HOME_SCREEN_TAG, "Loading Category Labels")
            CategoryLabels(categoryLabels)
            Log.d(HOME_SCREEN_TAG, "Loading Deals Block")
            DealsBlock()
            Log.d(HOME_SCREEN_TAG, "Loading Category Item Galleries")
            LazyColumn {
                items(items = productCategories) { category ->
                    when (productListNetworkState) {
                        is ProductListNetworkState.Success -> {
                            Log.d(
                                HOME_SCREEN_TAG,
                                "Product Data loaded: ${productListNetworkState.products}"
                            )
                            CategoryItems(
                                category,
                                productListNetworkState.products,
                                onProductClick
                            )
                        }

                        is ProductListNetworkState.Error -> {
                            Log.d(HOME_SCREEN_TAG, "Error loading products.")
                            UiError(productListNetworkState.e.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var searchValue by remember { mutableStateOf("") }
    OutlinedTextField(
        value = searchValue,
        onValueChange = { searchValue = it },
        shape = RoundedCornerShape(100.dp),
        singleLine = true,
        placeholder = { Text("Search for deals") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
        trailingIcon = {
            Row(horizontalArrangement = Arrangement.Center) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Mic,
                        contentDescription = "Search with your voice"
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        "Search by Image"
                    )
                }
            }
        },
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

@Composable
fun CategoryLabels(
    categories: List<String>,
    modifier: Modifier = Modifier
) {
    LazyRow {
        items(items = categories) { item ->
            OutlinedButton(onClick = {}, modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Store, "", modifier.size(15.dp))
                    Spacer(modifier.width(8.dp))
                    Text(item, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun CategoryItems(
    category: String,
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d(HOME_SCREEN_TAG, "Loading Category: $category")
    Text(
        text = "Trending in $category",
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp))
    ImageGalleryList(
        products = products,
        categoryFilter = category,
        onProductClick = onProductClick,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun ImageGalleryList(
    products: List<Product>,
    categoryFilter: String,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(8.dp)) {
        Log.d(HOME_SCREEN_TAG, "Loading Image Gallery for $categoryFilter")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.padding(horizontal = 8.dp)
        ) {
            Log.d(HOME_SCREEN_TAG, "Loading items...")
            val filteredItems = products.filter {it.category == categoryFilter.lowercase()}

            items(items = filteredItems) { product ->
                Log.d(HOME_SCREEN_TAG, "ID: ${product.id}, title: ${product.title}")
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(product.image)
                        .build(),
                    contentDescription = product.title,
                    modifier = modifier.size(80.dp)
                        .clickable { onProductClick(product) }
                )
            }
        }
    }
}

@Composable
fun DealsBlock(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer)) {
        Column {
            Text("Get 10% off your first purchase", fontWeight = FontWeight.Bold, modifier =  modifier.padding(top = 4.dp, start = 16.dp))
            Text("Click here to learn more...", style = MaterialTheme.typography.bodySmall, modifier =  modifier.padding(horizontal =  16.dp))
            Button(onClick = {}, modifier.padding(vertical = 4.dp, horizontal = 16.dp)) { Text("Learn More") }
        }
    }
}
