package com.example.sunseek.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunseek.R
import com.example.sunseek.model.Address
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.viewmodel.FavoriteLocationViewModel
import kotlinx.serialization.Serializable

@Serializable
object SelectLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteLocationScreen(
    favoriteLocationViewModel: FavoriteLocationViewModel,
    onBack: () -> Unit,
    onAddressSelected: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var openAlertDialog by remember { mutableStateOf(false) }
    var selectedAddress by remember { mutableStateOf<Address?>(null) }
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(favoriteLocationViewModel.listLocation, key = {
                it.id
            }) { item ->
                LocationBar(
                    address = item,
                    imageSource = R.drawable.hoanghon,
                    onClick = { onAddressSelected() }) {
                    openAlertDialog = !openAlertDialog
                    selectedAddress = item
                }
            }
        }
        when {
            openAlertDialog -> {
                CustomDialog(
                    title = selectedAddress?.detailedAddress,
                    supportText = selectedAddress?.streetAddress,
                    onDismissRequest = { openAlertDialog = false },
                    onEdit = { onEdit() },
                    onDelete = { onDelete() }
                )
            }
        }
    }
}

@Preview
@Composable
fun FavoriteLocationScreenPreview() {
    SunSeekTheme {
        FavoriteLocationScreen(
            favoriteLocationViewModel = viewModel(),
            onBack = {},
            onAddressSelected = {},
            onEdit = {},
            onDelete = {},
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
                painter = painterResource(R.drawable.pending),
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
fun CustomDialog(
    title: String? = null,
    supportText: String? = null,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
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
            Row {
                Button(onClick = { onDelete() }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Edit Button")
                    Text(
                        stringResource(R.string.delete),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(onClick = { onEdit() }) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit Button")
                    Text(
                        stringResource(R.string.edit_information),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        },
    )
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
            onEdit = {},

            )
    }
}