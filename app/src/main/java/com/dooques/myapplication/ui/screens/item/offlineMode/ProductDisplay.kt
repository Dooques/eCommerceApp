package com.dooques.myapplication.ui.screens.item.offlineMode

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dooques.myapplication.R
import com.dooques.myapplication.model.ImageGalleryItem
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.ui.reusableComponents.UiError
import com.dooques.myapplication.ui.screens.home.ProductListNetworkState
import com.dooques.myapplication.ui.screens.item.ItemDescription
import com.dooques.myapplication.ui.screens.item.PriceAndDelivery
import com.dooques.myapplication.ui.screens.item.ProductNetworkState
import com.dooques.myapplication.ui.screens.item.PurchaseOptions
import com.dooques.myapplication.ui.screens.item.SeeSimilar
import com.dooques.myapplication.ui.screens.item.SellerDetails
import com.dooques.myapplication.util.ITEM_SCREEN_TAG
import com.dooques.myapplication.util.ImageGalleryUiState

@Composable
fun ProductDisplayOnline(
    offlineMode: Boolean,
    productUiState: ProductNetworkState,
    productListUiState: ProductListNetworkState,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        Modifier.fillMaxWidth()
    ) {
        when (productUiState) {
            is ProductNetworkState.Success -> {
                item {
                    Log.d(
                        ITEM_SCREEN_TAG,
                        "\nItem network request success, loading item page..."
                    )
                    Log.d(ITEM_SCREEN_TAG, "Displaying Item: ${productUiState.product}")
                    ImageGallery(offlineMode, productUiState.product)
                    Row(modifier.fillMaxWidth()) {
                        Text(
                            text = productUiState.product.title,
                            fontSize = 35.sp,
                            style = MaterialTheme.typography.displayMedium,
                            modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                    SellerDetails()
                    PriceAndDelivery(productUiState.product)
                    PurchaseOptions(productUiState.product, {}, {}, {})
                    ItemDescription(productUiState.product)
                    when (productListUiState) {
                        is ProductListNetworkState.Success -> {
                            Log.d(ITEM_SCREEN_TAG, "Network request successful, displaying online data")
                            SeeSimilar(
                                offlineMode,
                                productUiState.product,
                                productListUiState.products,
                                onProductClick
                            )
                        }

                        is ProductListNetworkState.Error -> {
                            Log.d(ITEM_SCREEN_TAG, "Network failure, displaying offline data...")
                            UiError(productListUiState.e.toString())

                        }

                        else -> null
                    }
                }
            }

            is ProductNetworkState.Error ->
                item {
                    UiError(productUiState.e.toString())
                }

            else -> null
        }
    }
}

@Composable
fun ProductDisplayOffline(
    offlineMode: Boolean,
    product: Product,
    offlineProducts: List<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        Modifier.fillMaxWidth()
    ) {
        item {
            ImageGallery(offlineMode, product)
            Row(modifier.fillMaxWidth()) {
                Text(
                    text = product.title,
                    fontSize = 35.sp,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
            SellerDetails()
            PriceAndDelivery(product)
            PurchaseOptions(product, {}, {}, {})
            ItemDescription(product)
        }
        item {
            SeeSimilar(
                offlineMode,
                product,
                offlineProducts,
                onProductClick
            )
        }
    }
}

@Composable
fun ImageGallery(
    offlineMode: Boolean,
    product: Product,
    modifier: Modifier = Modifier
) {
    var imageList = listOf(
        ImageGalleryUiState(
            imageGalleryItem = ImageGalleryItem(
                image = product.image,
                title = "Image 1",
                selected = true
            )
        ),
        ImageGalleryUiState(
            imageGalleryItem = ImageGalleryItem(
                image = product.image,
                title = "Image 2"
            )
        ),
        ImageGalleryUiState(
            imageGalleryItem = ImageGalleryItem(
                image = product.image,
                title = "Image 3"
            )
        )
    )

    var selectedImage by remember { mutableStateOf(imageList[0]) }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Main Image
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            if (!offlineMode) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(imageList[0].imageGalleryItem.image)
                        .build(),
                    placeholder = painterResource(R.drawable.img_url_broken),
                    contentDescription = product.title,
                )
            }
            else {
                Image(
                    painter = painterResource(R.drawable.img_url_broken),
                    contentDescription = "Image failed to load..."
                )
            }
        }
    }

    // Extra Images
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        items(items = imageList) {
            val imageModifier =
                if (it.imageGalleryItem.selected)
                    modifier
                        .size(size = 100.dp)
                        .border(width = 2.dp, color = Color.Black)
                        .clickable {
                            imageList.forEach { it.imageGalleryItem.selected = false }
                            selectedImage = it
                            selectedImage.imageGalleryItem.selected = true
                        }
                else
                    modifier
                        .size(size = 100.dp)
                        .clickable {
                            selectedImage = it
                            imageList.forEach { it.imageGalleryItem.selected = false }
                        }
            Box(imageModifier) {
                if (!offlineMode)
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(product.image)
                            .build(),
                        contentDescription = product.title,
                        placeholder = painterResource(R.drawable.img_url_broken),
                        modifier = modifier
                            .padding(4.dp)
                            .align(Alignment.Center)
                    )
                else
                    Image(painter = painterResource(R.drawable.img_url_broken), contentDescription = "Image failed to load...")
                }
        }
    }
}