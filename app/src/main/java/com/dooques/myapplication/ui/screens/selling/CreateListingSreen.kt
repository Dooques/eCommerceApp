package com.dooques.myapplication.ui.screens.selling

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dooques.myapplication.R
import com.dooques.myapplication.model.Product
import com.dooques.myapplication.model.Rating
import com.dooques.myapplication.ui.reusableComponents.NavigationIconButton
import com.dooques.myapplication.util.SELLING_VM_TAG
import com.dooques.myapplication.util.VisualTransformationForCurrencyFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    createListingViewModel: CreateListingViewModel,
    scope: CoroutineScope,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(listOf()) }
//    var photoTakenUri by remember { mutableStateOf<Uri?>(null) }

    val pickImage =
        rememberLauncherForActivityResult(PickVisualMedia(), { uri -> selectedImageUri = uri })
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create a Listing", style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    NavigationIconButton(Icons.AutoMirrored.Default.ArrowBack, navigateUp)
                },
                windowInsets = WindowInsets(top = 40.dp)
            )
        },
    ) { contentPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(contentPadding)) {
            item {
                AddListingPics(pickImage, selectedImageUri, selectedImageUris)
            }
            item {
                ListingDetailsForm(createListingViewModel, scope, selectedImageUri, navigateUp)
            }
        }
    }
}

@Composable
fun AddListingPics(
    pickImage: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    selectedImageUri: Uri?,
    selectedImageUris: List<Uri>,
    modifier: Modifier = Modifier
) {
    Log.d(SELLING_VM_TAG, "\nSelectedImage: $selectedImageUri")
    Column {
        OutlinedButton(
            onClick = {
                Log.d(SELLING_VM_TAG, "\nOpening Photo picker dialog...")
                pickImage.launch(PickVisualMediaRequest(ImageOnly))
                Log.d(SELLING_VM_TAG, "Image selected.")
                      },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Add Pic")
        }
        if (selectedImageUri != null) {
            Box(
                modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "First uploaded image",
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp)
                )
            }
        }
    }
}

@Composable
fun ListingDetailsForm(
    createListingViewModel: CreateListingViewModel,
    scope: CoroutineScope,
    selectedImageUri: Uri?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        /* Title */
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(16.dp))
        /* Category */
        OutlinedTextField(
            value = category.replaceFirstChar { it.uppercase() },
            onValueChange = { category = it.lowercase() },
            label = { Text("Category") },
            singleLine = true,
            modifier = modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(16.dp))
        /* Price */
        OutlinedTextField(
            value = price.toString(),
            onValueChange = {
                val digitsOnly = it.filter { char -> char.isDigit() }
                price = when {
                    digitsOnly.isEmpty() -> "0"
                    digitsOnly.all { char -> char == '0' } -> "0"
                    else -> digitsOnly.trimStart('0')
                }
            },
            label = { Text("Price") },
            singleLine = true,
            prefix = { Text("£") },
            visualTransformation = VisualTransformationForCurrencyFormat(fixedCursorAtTheEnd = true),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.label_10_cut),
            style = MaterialTheme.typography.labelSmall,
            modifier = modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.size(16.dp))
        /* Description */
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            minLines = 5,
            modifier = modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(16.dp))
        /* Confirm Listing */
        OutlinedButton(
            onClick = {
                val product = Product(
                    id = 0,
                    title = title,
                    price = price.toFloat(),
                    description = description,
                    category = category,
                    image = selectedImageUri.toString(),
                    rating = Rating()
                )
                scope.launch {
                    Log.d(
                        SELLING_VM_TAG,
                        "\nPreparing to update server with Listing Information"
                    )
                    createListingViewModel.postProductDetails(product)
                }
                navigateBack()
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = modifier.fillMaxWidth()
        ) { Text("Confirm Listing") }
    }
}


