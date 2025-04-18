package com.dooques.myapplication.ui.screens.home.offlineMode

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dooques.myapplication.R
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.ui.reusableComponents.UiError
import com.dooques.myapplication.ui.screens.home.ProductListNetworkState
import com.dooques.myapplication.util.HOME_SCREEN_TAG

@Composable
fun OnlineMode(
    offlineMode: Boolean,
    productListNetworkState: ProductListNetworkState,
    category: String,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    when (productListNetworkState) {
        is ProductListNetworkState.Success -> {
            Log.d(
                HOME_SCREEN_TAG,
                "Product Data loaded: ${productListNetworkState.products}"
            )
            CategoryItems(
                offlineMode = offlineMode,
                category = category,
                products = productListNetworkState.products,
                onProductClick = onProductClick
            )
        }

        is ProductListNetworkState.Error -> {
            Log.d(
                HOME_SCREEN_TAG,
                "Network failure, displaying offline products..."
            )
            UiError(
                productListNetworkState.e.toString(),
                modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }
        else -> null
    }
}


@Composable
fun OfflineMode(
    offlineMode: Boolean,
    category: String,
    offlineProducts: List<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d(
        HOME_SCREEN_TAG,
        "Product Data loaded: $offlineProducts"
    )
    CategoryItems(
        offlineMode = offlineMode,
        category = category,
        products = offlineProducts,
        onProductClick = onProductClick
    )
}

@Composable
fun CategoryItems(
    offlineMode: Boolean,
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
        offlineMode = offlineMode,
        productList = products,
        categoryFilter = category,
        onProductClick = onProductClick,
        modifier = modifier.padding(bottom = 8.dp)
    )
}
@Composable
fun ImageGalleryList(
    offlineMode: Boolean,
    productList: List<Product>,
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
            val filteredItems = productList.filter { it.category == categoryFilter.lowercase() }
            items(items = filteredItems) { product ->
                Log.d(HOME_SCREEN_TAG, "ID: ${product.id}, title: ${product.title}")
                if (!offlineMode) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(product.image)
                            .build(),
                        contentDescription = product.title,
                        modifier = modifier
                            .size(80.dp)
                            .clickable { onProductClick(product) }
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.img_url_broken),
                        contentDescription = product.title,
                        modifier = modifier
                            .size(80.dp)
                            .clickable { onProductClick(product)}
                    )
                }
            }
        }
    }
}

