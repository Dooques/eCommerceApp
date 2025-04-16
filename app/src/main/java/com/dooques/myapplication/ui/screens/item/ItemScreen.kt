package com.dooques.myapplication.ui.screens.item

import android.icu.text.DecimalFormat
import android.util.Log
import android.view.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dooques.myapplication.R
import com.dooques.myapplication.model.ImageGalleryItem
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.ui.reusableComponents.NavigationIconButton
import com.dooques.myapplication.ui.reusableComponents.UiError
import com.dooques.myapplication.ui.screens.home.ProductListNetworkState
import com.dooques.myapplication.util.ITEM_SCREEN_TAG
import com.dooques.myapplication.util.ImageGalleryUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    productUiState: ProductNetworkState,
    productListUiState: ProductListNetworkState,
    onProductClick: (Product) -> Unit,
    getProduct: (Int) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d(ITEM_SCREEN_TAG, "\nLoading Item Screen")

    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf<Boolean>(false) }
    var onRefresh = {
        isRefreshing = true
        scope.launch {
            delay(1_000L)
            when (productUiState) {
                is ProductNetworkState.Success -> getProduct(productUiState.product.id)
                else -> null
            }
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    NavigationIconButton(Icons.AutoMirrored.Default.ArrowBack, navigateUp)
                },
                actions = {
                    NavigationIconButton(Icons.Default.ShoppingCart, {})
                    NavigationIconButton(Icons.Default.Share, {})
                    NavigationIconButton(Icons.Default.Search, {})
                },
                windowInsets = WindowInsets(top = 40.dp)
            )
        },
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = state,
            onRefresh = { onRefresh() },
            modifier = modifier.padding(padding)
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
                            ImageGallery(productUiState.product)
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
                                is ProductListNetworkState.Success ->
                                    SeeSimilar(
                                        productUiState.product,
                                        productListUiState.products,
                                        onProductClick
                                    )

                                is ProductListNetworkState.Error ->
                                    UiError(productListUiState.e.toString())
                            }
                        }
                    }

                    is ProductNetworkState.Error ->
                        item {
                            UiError(productUiState.e.toString())
                        }
                }
            }
        }
    }
}

@Composable
fun ImageGallery(
    product: Product,
    modifier: Modifier = Modifier
) {
    val imageList = listOf(
        ImageGalleryUiState(imageGalleryItem = ImageGalleryItem(image = product.image, title = "Image 1", selected = true)),
        ImageGalleryUiState(imageGalleryItem = ImageGalleryItem(image = product.image, title = "Image 2")),
        ImageGalleryUiState(imageGalleryItem = ImageGalleryItem(image = product.image, title = "Image 3"))
    )

    var selectedImage by remember { mutableStateOf(imageList[0]) }
    val borderOn = Pair(10.dp, Color.Black)

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Main Image
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imageList[0].imageGalleryItem.image)
                    .build(),
                contentDescription = product.title,
            )
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
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(product.image)
                            .build(),
                        contentDescription = product.title,
                        modifier = modifier.padding(4.dp).align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun PriceAndDelivery(product: Product, modifier: Modifier = Modifier) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            val formattedPrice = DecimalFormat("#,###.00").format(product.price)
            Text("£${formattedPrice}", style = MaterialTheme.typography.displaySmall, modifier = modifier.padding(start = 16.dp))
            Text(" + £3.99 Courier delivery", style = MaterialTheme.typography.bodySmall)
            IconButton(onClick = {}) {Icon(imageVector = Icons.Outlined.Info, contentDescription = "Check for more details", modifier.size(20.dp))}
        }
        Row(modifier.padding(start = 16.dp)) {
            Column {
                Text(
                    "Estimated delivery 7 working days.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text("Includes postage to and from sender.", style = MaterialTheme.typography.bodySmall)
            }
        }
        Row(modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text ("Condition: New with Box")
        }
    }
}

@Composable
fun PurchaseOptions(
    product: Product,
    addToBasket: (Product) -> Unit,
    makeOffer: (Product) -> Unit,
    saveFavorite: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(Modifier.fillMaxWidth()) {
        FilledTonalButton(
            onClick = { addToBasket },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) { Text("Add to Basket") }
        FilledTonalButton(
            onClick = { makeOffer },
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) { Text("Make an offer") }
        FilledTonalButton(
            onClick = { saveFavorite },
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) { Text("Add to favorites") }
    }
}

@Composable
fun ItemDescription(product: Product, modifier: Modifier = Modifier) {
    Column(modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row { Text("Item Description", style = MaterialTheme.typography.bodyLarge, modifier = modifier.padding(vertical = 8.dp)) }
        Row { Text(product.description, style = MaterialTheme.typography.bodyMedium) }
    }
}

@Composable
fun SellerDetails(modifier: Modifier = Modifier) {
    val labelTitle = MaterialTheme.typography.bodyLarge
    val labelStyle = MaterialTheme.typography.bodyMedium
    Box(modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterStart)
        ) {
            Image(
                painter = painterResource(R.drawable.seller_image_placeholder),
                contentDescription = "Profile Photo",
                modifier =
                    modifier
                        .clip(CircleShape)
                        .size(80.dp)
            )
            Spacer(modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.Center,) {
                Text("NintenGamer420", style = labelStyle)
                Text("30 more items for sale", style = labelStyle)
                Text("95% Positive Feedback", style = labelStyle)
            }
        }
        Box(
            modifier
                .padding(16.dp)
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.25f))
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Contact Seller",
                modifier = modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun SeeSimilar(
    product: Product,
    productListUiState: List<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(Modifier.fillMaxWidth()) {
                ImageGallerySimilarItems(productListUiState, product.category, onProductClick)
    }
}

@Composable
fun ImageGallerySimilarItems(
    products: List<Product>,
    category: String,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text("See Similar Items", modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Log.d(ITEM_SCREEN_TAG, "Loading items...")
            val filteredItems = products.filter { it.category == category.lowercase() }

            items(items = filteredItems) { product ->
                Log.d(ITEM_SCREEN_TAG, "ID: ${product.id}, title: ${product.title}")
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