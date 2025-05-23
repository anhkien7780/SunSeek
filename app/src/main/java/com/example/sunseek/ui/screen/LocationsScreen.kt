package com.example.sunseek.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunseek.R
import com.example.sunseek.model.Address
import com.example.sunseek.model.LocationWithID
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.model.LoadingUIState
import com.example.sunseek.viewmodel.LocationViewModel
import com.example.sunseek.viewmodel.OpenWeatherViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object FavoriteLocation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteLocationScreen(
    locationViewModel: LocationViewModel,
    openWeatherViewModel: OpenWeatherViewModel,
    onBack: () -> Unit,
    onAddressSelected: () -> Unit,
    onAddLocation: () -> Unit,
) {
    val locationUIState by locationViewModel.loadingUIState.collectAsState()
    val context = LocalContext.current
    val deleteLocationScope = rememberCoroutineScope()
    var openAlertDialog by remember { mutableStateOf(false) }
    var selectedAddress by remember { mutableStateOf<Address?>(null) }
    val listLocation by locationViewModel.listLocation.collectAsState()
    val weatherUIState by openWeatherViewModel.weatherUIState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back_button
                            )
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.my_favorite_locations),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        when (listLocation.isEmpty()) {
            true -> EmptyBody {
                onAddLocation()
            }

            false -> LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 5.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listLocation) { item ->
                    LocationBar(
                        address = item.toAddress(),
                        imageSource = R.drawable.hoanghon,
                        onClick = {
                            openWeatherViewModel.getWeatherReportByCoordinates(
                                item.latitude,
                                item.longitude,
                                item.toAddress()
                            )
                        }, onConfigurationClick = {
                            locationViewModel.selectedLocationID.value = item.id
                            openAlertDialog = !openAlertDialog
                        })
                    selectedAddress = item.toAddress()
                }
            }
        }
        when (weatherUIState) {
            LoadingUIState.Idle -> {}
            LoadingUIState.Loading -> FullScreenLoading("Tải thông tin thời tiết", innerPadding)
            LoadingUIState.Failed -> {}
            LoadingUIState.Success -> {}
        }
        LaunchedEffect(weatherUIState) {
            if (weatherUIState == LoadingUIState.Success) {
                onAddressSelected()
                openWeatherViewModel.setIdle()
            }
        }
        when {
            openAlertDialog -> {
                CustomDialog(
                    title = selectedAddress?.detailedAddress,
                    supportText = selectedAddress?.streetAddress,
                    onDismissRequest = { openAlertDialog = false },
                    onDelete = {
                        deleteLocationScope.launch {
                            locationViewModel.deleteAddress(
                                context,
                                locationViewModel.selectedLocationID.value
                            )
                            openAlertDialog = false
                        }
                    }
                )
            }
        }
        when (locationUIState) {
            LoadingUIState.Failed -> {}
            LoadingUIState.Idle -> {}
            LoadingUIState.Loading -> FullScreenLoading(title = "Xóa địa chỉ")
            LoadingUIState.Success -> {}
        }

    }
}

@Composable
private fun EmptyBody(
    modifier: Modifier = Modifier,
    onAddLocation: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_list),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.size(5.dp))
        Button(
            onClick = { onAddLocation() },
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                stringResource(R.string.add_location),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
fun FavoriteLocationScreenPreview() {
    SunSeekTheme {
        FavoriteLocationScreen(
            onBack = {},
            onAddressSelected = {}, locationViewModel = viewModel(),
            onAddLocation = {},
            openWeatherViewModel = viewModel()
        )
    }
}

@Stable
@Composable
fun LocationBar(
    modifier: Modifier = Modifier,
    imageSource: Int,
    address: Address,
    onClick: () -> Unit,
    onConfigurationClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(width = 400.dp, height = 100.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(imageSource),
            contentDescription = stringResource(R.string.favorite_image),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(80.dp),
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(
                text = address.detailedAddress,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = address.streetAddress,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = {
                onConfigurationClick()
            },
            modifier = Modifier.padding(end = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.pending_button),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationBarPreview() {
    SunSeekTheme {
        LocationBar(
            address = Address(
                detailedAddress = "Hồ Tây",
                streetAddress = "Tây hồ, Hà Nội, Việt Nam"
            ),
            imageSource = R.drawable.hoanghon, onClick = {}, onConfigurationClick = {},
        )
    }
}

@Composable
fun FullScreenLoading(
    title: String = "",
    innerPadding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.5f)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(innerPadding),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicatorWithText(title = title)
    }
}

@Composable
fun CircularProgressIndicatorWithText(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(bottom = 5.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun CustomDialog(
    title: String? = null,
    supportText: String? = null,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        iconContentColor = MaterialTheme.colorScheme.onPrimary,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            if (title != null) {
                Text(text = title, style = MaterialTheme.typography.headlineSmall)
            }
        },
        text = {
            if (supportText != null) {
                Text(text = supportText, style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            MyCustomButton(
                onClick = { onDelete() },
                painter = painterResource(R.drawable.check_circle),
                text = stringResource(R.string.confirm),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentDescription = stringResource(R.string.confirm),
            )

        },
        dismissButton = {
            MyCustomButton(
                onClick = { onDismissRequest() },
                painter = painterResource(R.drawable.cancel),
                text = stringResource(R.string.cancel),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                contentDescription = stringResource(R.string.cancel),
            )
        }
    )
}

@Composable
fun MyCustomButton(
    onClick: () -> Unit,
    painter: Painter,
    text: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentDescription: String
) {
    Button(onClick = { onClick() }, colors = colors) {
        Icon(
            painter = painter,
            contentDescription = contentDescription
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview
@Composable
fun CustomDialogPreview() {
    SunSeekTheme {
        CustomDialog(
            title = "Hồ Tây",
            supportText = "Bạn muốn thay đổi địa chỉ?",
            onDismissRequest = {},
            onDelete = {},
        )
    }
}

fun LocationWithID.toAddress() = Address(
    detailedAddress = this.name.split(',')[0],
    streetAddress = this.name
)