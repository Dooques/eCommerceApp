package com.dooques.myapplication.ui.screens.listing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dooques.myapplication.R
import com.dooques.myapplication.model.ImageGalleryItem
import com.dooques.myapplication.util.ImageGalleryUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingScreen(
    modifier: Modifier = Modifier,
) {
    Column(Modifier.fillMaxWidth()) {
        ImageGallery()
        Row(modifier.fillMaxWidth()) {
            Text(
                text = "Nintendo Switch 2 Bundle With Mario Kart World",
                fontSize = 35.sp,
                style = MaterialTheme.typography.displayMedium,
                modifier = modifier.padding(8.dp)
            )
        }
        SellerDetails()
    }
}

@Composable
fun ImageGallery(modifier: Modifier = Modifier) {
    val imageList = listOf(
        ImageGalleryUiState(
            imageGalleryItem = ImageGalleryItem(image = R.drawable.switch_2, title = "Image 1", selected = true)
        ),
        ImageGalleryUiState(
            imageGalleryItem = ImageGalleryItem(image = R.drawable.switch_2, title = "Image 2")
        ),
        ImageGalleryUiState(
            imageGalleryItem = ImageGalleryItem(image = R.drawable.switch_2, title = "Image 3")
        )
    )
    var selectedImage by remember { mutableStateOf(imageList[0]) }
    val borderOn = Pair(10.dp, Color.Black)


    Column {
        Image(
            painter = painterResource(selectedImage.imageGalleryItem.image),
            contentDescription = selectedImage.imageGalleryItem.title,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
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
                Image(
                    painter = painterResource(it.imageGalleryItem.image),
                    contentDescription = it.imageGalleryItem.title,
                    modifier = imageModifier
                )
            }
        }
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

